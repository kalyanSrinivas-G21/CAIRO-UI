package com.uiframework.cairo.render;

import com.uiframework.cairo.core.Component;
import org.teavm.jso.browser.Window;
import org.teavm.jso.canvas.CanvasRenderingContext2D;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.dom.html.HTMLDocument;

/**
 * The WebWindow serves as the primary host for the UI framework within the browser.
 * It manages the HTML5 Canvas lifecycle, high-DPI scaling, and the animation loop.
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
     * Configures the canvas for High-DPI (Retina) displays to ensure crisp text and vector rendering.
     * Note: HTML5 Canvas natively handles double-buffering, so no manual off-screen
     * buffer management is required for flicker-free rendering.
     */
    private void setupHighDPI() {
        // Fetch the device pixel ratio (fallback to 2.0 if undefined/unsupported)
        double ratio = Window.current().getDevicePixelRatio();
        if (ratio <= 0.0) {
            ratio = 2.0;
        }

        // 1. Get the logical (CSS) dimensions defined in the HTML
        logicalWidth = canvasElement.getClientWidth();
        logicalHeight = canvasElement.getClientHeight();

        // 2. Lock the physical CSS size so the canvas doesn't visually expand on the screen
        canvasElement.getStyle().setProperty("width", logicalWidth + "px");
        canvasElement.getStyle().setProperty("height", logicalHeight + "px");

        // 3. Scale the internal canvas buffer resolution up by the device ratio
        canvasElement.setWidth((int) (logicalWidth * ratio));
        canvasElement.setHeight((int) (logicalHeight * ratio));

        // 4. Scale the rendering context. All subsequent draw calls (e.g., fillRect(10, 10, 50, 50))
        // will automatically be multiplied by the ratio, keeping our framework math simple.
        ctx.scale(ratio, ratio);
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
        // Clear the canvas using the logical CSS dimensions, as the context is currently scaled
        ctx.clearRect(0, 0, logicalWidth, logicalHeight);

        // Delegate recursive tree rendering to the RenderManager
        RenderManager.render(rootComponent, ctx);
    }
}