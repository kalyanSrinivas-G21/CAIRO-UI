package com.uiframework.cairo.render;

import com.uiframework.cairo.components.Panel;
import com.uiframework.cairo.core.Component;
import com.uiframework.cairo.core.Container;
import com.uiframework.cairo.core.StateChangeListener;
import org.teavm.jso.canvas.CanvasRenderingContext2D;

/**
 * The RenderManager orchestrates the recursive painting of the component tree.
 * Hardened to prevent opaque overdraw by respecting self vs. child dirty states.
 */
public class RenderManager implements StateChangeListener {

    private boolean renderPending = false;

    // --- TELEMETRY METRICS ---
    public static int paintCallsThisFrame = 0;
    public static int lastFramePaintCount = 0;

    @Override
    public void onStateChanged(Component source) {
        this.renderPending = true;
    }

    public boolean isRenderPending() {
        return renderPending;
    }

    public void clearRenderPending() {
        this.renderPending = false;
    }

    /**
     * Wrapper method to initialize telemetry for the current frame
     * and begin the recursive optimization loop. WebWindow should call this.
     */
    public static void renderFrame(Component root, CanvasRenderingContext2D ctx) {
        paintCallsThisFrame = 0;
        renderOptimized(root, ctx);
        lastFramePaintCount = paintCallsThisFrame;
    }

    /**
     * Recursively renders the tree, pruning branches that are clean or invisible.
     */
    private static void renderOptimized(Component root, CanvasRenderingContext2D ctx) {
        // 1. Prune: Skip entirely if hidden or if no state has changed in this branch
        if (root == null || !root.isVisible() || (!root.isSelfDirty() && !root.isChildDirty())) {
            return;
        }

        // 2. Isolate: Save current canvas state
        ctx.save();

        try {
            double absX = (double) root.getAbsoluteX();
            double absY = (double) root.getAbsoluteY();
            double w = (double) root.getWidth();
            double h = (double) root.getHeight();

            // 3. Clip: Apply boundaries. MUST run even if only children are dirty to constrain them!
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

            // 4. Paint: ONLY paint if this specific component changed
            if (root.isSelfDirty()) {
                // --- TELEMETRY TRACKING ---
                paintCallsThisFrame++;
                root.paint(ctx);
            }

            // 5. Recurse: Traverse children if the container or its children are dirty
            if (root instanceof Container container) {
                for (Component child : container.getChildren()) {
                    renderOptimized(child, ctx);
                }
            }
        } finally {
            // 6. Restore & Clean
            root.clearDirty();
            ctx.restore();
        }
    }
}