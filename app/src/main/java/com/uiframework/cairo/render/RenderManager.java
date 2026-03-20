package com.uiframework.cairo.render;

import com.uiframework.cairo.components.Panel;
import com.uiframework.cairo.core.Component;
import com.uiframework.cairo.core.Container;
import com.uiframework.cairo.core.StateChangeListener;
import org.teavm.jso.canvas.CanvasRenderingContext2D;

/**
 * The RenderManager orchestrates the recursive painting of the component tree.
 * Upgraded to act as a State Observer for high-performance selective rendering.
 */
public class RenderManager implements StateChangeListener {

    // Flag to track if any component in the tree has requested a redraw
    private boolean renderPending = false;

    @Override
    public void onStateChanged(Component source) {
        // A component was marked dirty. Flag the engine to render the next frame.
        this.renderPending = true;
    }

    public boolean isRenderPending() {
        return renderPending;
    }

    public void clearRenderPending() {
        this.renderPending = false;
    }

    /**
     * Recursively renders the tree, pruning branches that are clean or invisible.
     *
     * @param root The component (or root of a tree) to be rendered.
     * @param ctx  The HTML5 Canvas 2D context.
     */
    public static void renderOptimized(Component root, CanvasRenderingContext2D ctx) {
        // 1. Prune: Skip entirely if hidden or if no state has changed in this branch
        if (root == null || !root.isVisible() || !root.isDirty()) {
            return;
        }

        // 2. Isolate: Save current canvas state
        ctx.save();

        try {
            double absX = (double) root.getAbsoluteX();
            double absY = (double) root.getAbsoluteY();
            double w = (double) root.getWidth();
            double h = (double) root.getHeight();

            // 3. Clip: Apply boundaries, respecting rounded corners if it's a Panel
            ctx.beginPath();
            if (root instanceof Panel p && p.getCornerRadius() > 0) {
                double r = (double) p.getCornerRadius();
                ctx.moveTo(absX + r, absY);
                ctx.lineTo(absX + w - r, absY);
                ctx.quadraticCurveTo(absX + w, absY, absX + w, absY + r);
                ctx.lineTo(absX + w, absY + h - r);
                ctx.quadraticCurveTo(absX + w, absY + h, absX + w - r, absY + h);
                ctx.lineTo(absX + r, absY + h);
                ctx.quadraticCurveTo(absX, absY + h, absX, absY + h - r);
                ctx.lineTo(absX, absY + r);
                ctx.quadraticCurveTo(absX, absY, absX + r, absY);
                ctx.closePath();
            } else {
                ctx.rect(absX, absY, w, h);
            }
            ctx.clip();

            // 4. Paint & Clean: Draw the component and instantly clear its dirty flag
            root.paint(ctx);
            root.clearDirty();

            // 5. Recurse: Traverse children if applicable
            if (root instanceof Container container) {
                for (Component child : container.getChildren()) {
                    renderOptimized(child, ctx);
                }
            }
        } finally {
            // 6. Restore: Revert canvas state safely
            ctx.restore();
        }
    }
}