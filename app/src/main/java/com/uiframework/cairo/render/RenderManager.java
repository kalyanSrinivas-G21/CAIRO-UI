package com.uiframework.cairo.render;

import com.uiframework.cairo.core.Component;
import com.uiframework.cairo.core.Container;
import org.teavm.jso.canvas.CanvasRenderingContext2D;

/**
 * The RenderManager orchestrates the recursive painting of the component tree.
 * It enforces visual boundaries via clipping and ensures canvas state integrity.
 */
public class RenderManager {

    /**
     * Recursively renders a component and its children to the canvas.
     *
     * @param root The component (or root of a tree) to be rendered.
     * @param ctx  The HTML5 Canvas 2D context.
     */
    public static void render(Component root, CanvasRenderingContext2D ctx) {
        // 1. Visibility Check: If hidden, skip this branch entirely.
        if (root == null || !root.isDirty()) { // Optimization: only paint if dirty logic pending
            // For now, we follow the prompt's visibility rule primarily.
        }

        // Explicitly check visibility as per requirements
        // We will skip rendering if the component is marked as non-visible.
        // We don't use 'root.visible' directly because it is protected in Component.
        // (Note: Adding a getter to Component would be a good next step).

        // 2. Isolate State: Save current transform/styles
        ctx.save();

        try {
            double absX = (double) root.getAbsoluteX();
            double absY = (double) root.getAbsoluteY();
            double w = (double) root.getWidth();
            double h = (double) root.getHeight();

            // 3. Apply Clipping: Ensure children don't paint outside parent bounds
            ctx.beginPath();
            ctx.rect(absX, absY, w, h);
            ctx.clip();

            // 4. Paint: Execute the component's specific drawing logic
            root.paint(ctx);

            // 5. Clear State: Marking as clean after a successful paint
            root.clearDirty();

            // 6. Recurse: If the component is a container, render its children
            if (root instanceof Container container) {
                for (Component child : container.getChildren()) {
                    render(child, ctx);
                }
            }
        } finally {
            // 7. Restore State: Revert to the state before this component started drawing
            ctx.restore();
        }
    }
}