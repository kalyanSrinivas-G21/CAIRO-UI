package com.uiframework.cairo.render;

import com.uiframework.cairo.core.Component;
import org.teavm.jso.browser.Window;
import org.teavm.jso.canvas.CanvasRenderingContext2D;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.dom.html.HTMLDocument;

/**
 * The WebWindow serves as the primary host for the UI framework within the browser.
 * It manages the HTML5 Canvas lifecycle, high-DPI scaling, and the animation loop.
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

    // We store the logical CSS dimensions to correctly clear the canvas each frame
    private int logicalWidth;
    private int logicalHeight;

    public WebWindow(String canvasId) {
        HTMLDocument document = Window.current().getDocument();
        this.canvasElement = (HTMLCanvasElement) document.getElementById(canvasId);

        if (this.canvasElement == null) {
            throw new RuntimeException("Could not find canvas with ID: " + canvasId);
        }

        this.ctx = (CanvasRenderingContext2D) canvasElement.getContext("2d");

        // Initialize High-DPI scaling immediately after context retrieval
        setupHighDPI();
    }

    /**
     * Configures the canvas for High-DPI (Retina) displays.
     * Note: HTML5 Canvas natively handles double-buffering.
     */
    private void setupHighDPI() {
        double ratio = Window.current().getDevicePixelRatio();
        if (ratio <= 0.0) {
            ratio = 2.0;
        }

        // Get logical (CSS) dimensions
        logicalWidth = canvasElement.getClientWidth();
        logicalHeight = canvasElement.getClientHeight();

        // Lock physical CSS size
        canvasElement.getStyle().setProperty("width", logicalWidth + "px");
        canvasElement.getStyle().setProperty("height", logicalHeight + "px");

        // Scale internal buffer
        canvasElement.setWidth((int) (logicalWidth * ratio));
        canvasElement.setHeight((int) (logicalHeight * ratio));

        // Scale context
        ctx.scale(ratio, ratio);
    }

    public void setRoot(Component root) {
        this.rootComponent = root;
    }

    public void startRenderLoop() {
        scheduleFrame();
    }

    /**
     * Schedules the next frame using the browser's native requestAnimationFrame API.
     * The callback is kept lightweight to ensure high performance.
     */
    private void scheduleFrame() {
        Window.current().requestAnimationFrame(timestamp -> {
            // Lightweight null-check before delegating to the rendering engine
            if (rootComponent != null) {
                performFrameRender();
            }
            // Recursive call to maintain the loop
            scheduleFrame();
        });
    }

    private void performFrameRender() {
        // Clear the canvas using the logical CSS dimensions
        ctx.clearRect(0, 0, logicalWidth, logicalHeight);

        // Delegate recursive tree rendering to the RenderManager
        RenderManager.render(rootComponent, ctx);
    }
}