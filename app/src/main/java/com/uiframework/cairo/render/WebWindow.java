package com.uiframework.cairo.render;

import com.uiframework.cairo.core.Component;
import org.teavm.jso.browser.Window;
import org.teavm.jso.canvas.CanvasRenderingContext2D;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.dom.html.HTMLDocument;

/**
 * Updated WebWindow to utilize the RenderManager for tree-based painting.
 */
public class WebWindow {

    private final HTMLCanvasElement canvasElement;
    private final CanvasRenderingContext2D ctx;
    private Component rootComponent;

    public WebWindow(String canvasId) {
        HTMLDocument document = Window.current().getDocument();
        this.canvasElement = (HTMLCanvasElement) document.getElementById(canvasId);

        if (this.canvasElement == null) {
            throw new RuntimeException("Could not find canvas with ID: " + canvasId);
        }

        this.ctx = (CanvasRenderingContext2D) canvasElement.getContext("2d");
    }

    public void setRoot(Component root) {
        this.rootComponent = root;
    }

    public void startRenderLoop() {
        scheduleFrame();
    }

    private void scheduleFrame() {
        Window.current().requestAnimationFrame(timestamp -> {
            if (rootComponent != null) {
                performFrameRender();
            }
            scheduleFrame();
        });
    }

    private void performFrameRender() {
        // Clear the entire canvas for the new frame
        ctx.clearRect(0, 0, canvasElement.getWidth(), canvasElement.getHeight());

        // Delegate recursive tree rendering to the RenderManager
        RenderManager.render(rootComponent, ctx);
    }
}