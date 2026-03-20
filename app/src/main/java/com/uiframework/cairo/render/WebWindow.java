package com.uiframework.cairo.render;

import com.uiframework.cairo.core.Component;
import org.teavm.jso.browser.Window;
import org.teavm.jso.canvas.CanvasRenderingContext2D;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.dom.html.HTMLDocument;

/**
 * The WebWindow serves as the primary host for the UI framework within the browser.
 * * ARCHITECTURE NOTE:
 * WebAssembly and JavaScript execute in a single-threaded environment. Unlike
 * traditional Java desktop frameworks (Swing/AWT) that require an Event Dispatch
 * Thread (EDT), all UI updates, event handling, and rendering in CAIRO happen
 * on the browser's main thread. This eliminates race conditions by design.
 */
public class WebWindow {

    private final HTMLCanvasElement canvasElement;
    private final CanvasRenderingContext2D ctx;
    private Component rootComponent;

    // The instantiated RenderManager now tracks global tree state
    private final RenderManager renderManager;

    private int logicalWidth;
    private int logicalHeight;

    public WebWindow(String canvasId) {
        HTMLDocument document = Window.current().getDocument();
        this.canvasElement = (HTMLCanvasElement) document.getElementById(canvasId);

        if (this.canvasElement == null) {
            throw new RuntimeException("Could not find canvas with ID: " + canvasId);
        }

        this.ctx = (CanvasRenderingContext2D) canvasElement.getContext("2d");
        this.renderManager = new RenderManager();

        setupHighDPI();
    }

    private void setupHighDPI() {
        double ratio = Window.current().getDevicePixelRatio();
        if (ratio <= 0.0) {
            ratio = 2.0;
        }

        logicalWidth = canvasElement.getClientWidth();
        logicalHeight = canvasElement.getClientHeight();

        canvasElement.getStyle().setProperty("width", logicalWidth + "px");
        canvasElement.getStyle().setProperty("height", logicalHeight + "px");

        canvasElement.setWidth((int) (logicalWidth * ratio));
        canvasElement.setHeight((int) (logicalHeight * ratio));

        ctx.scale(ratio, ratio);
    }

    public void setRoot(Component root) {
        // Disconnect old root if one exists
        if (this.rootComponent != null) {
            this.rootComponent.removeObserver(this.renderManager);
        }

        this.rootComponent = root;

        // Connect the new root to our rendering observer
        if (this.rootComponent != null) {
            this.rootComponent.addObserver(this.renderManager);
            this.rootComponent.markDirty(); // Trigger the initial draw
        }
    }

    public void startRenderLoop() {
        scheduleFrame();
    }

    private void scheduleFrame() {
        Window.current().requestAnimationFrame(timestamp -> {
            // Only execute canvas clearing and tree traversal if a change actually occurred
            if (rootComponent != null && renderManager.isRenderPending()) {
                performFrameRender();
            }
            scheduleFrame();
        });
    }

    private void performFrameRender() {
        // Clear the canvas using the logical CSS dimensions
        ctx.clearRect(0, 0, logicalWidth, logicalHeight);

        // Execute the pruned, high-performance rendering pass
        RenderManager.renderFrame(rootComponent, ctx);

        // Reset the global engine flag for the next frame
        renderManager.clearRenderPending();
    }
}