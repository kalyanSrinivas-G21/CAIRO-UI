package com.uiframework.cairo.render;

import com.uiframework.cairo.animation.AnimationManager;
import com.uiframework.cairo.core.Component;
import com.uiframework.cairo.core.Container;
import com.uiframework.cairo.event.EventDispatcher;
import org.teavm.jso.browser.Window;
import org.teavm.jso.canvas.CanvasRenderingContext2D;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.dom.html.HTMLDocument;

/**
 * The WebWindow serves as the primary host for the UI framework within the browser.
 * Updated to calculate Delta Time and pump the global AnimationManager.
 */
public class WebWindow {

    private final HTMLCanvasElement canvasElement;
    private final CanvasRenderingContext2D ctx;
    private Component rootComponent;
    private final RenderManager renderManager;
    private EventDispatcher eventDispatcher;

    private int logicalWidth;
    private int logicalHeight;
    private long lastTime;

    /**
     * Initializes the WebWindow and binds it to a specific HTML canvas.
     *
     * @param canvasId The ID of the canvas element in the DOM.
     */
    public WebWindow(String canvasId) {
        HTMLDocument document = Window.current().getDocument();
        this.canvasElement = (HTMLCanvasElement) document.getElementById(canvasId);

        if (this.canvasElement == null) {
            throw new RuntimeException("Could not find canvas with ID: " + canvasId);
        }

        this.ctx = (CanvasRenderingContext2D) canvasElement.getContext("2d");
        this.renderManager = new RenderManager();
        this.lastTime = System.currentTimeMillis();

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

    /**
     * Sets the root of the component tree, forces it to match the canvas size,
     * and initializes the interaction routing pipeline.
     *
     * @param root The top-level component.
     */
    public void setRoot(Component root) {
        if (this.rootComponent != null) {
            this.rootComponent.removeObserver(this.renderManager);
        }

        this.rootComponent = root;

        if (this.rootComponent != null) {
            this.rootComponent.setBounds(0, 0, logicalWidth, logicalHeight);
            this.rootComponent.addObserver(this.renderManager);
            this.rootComponent.invalidate();
            this.rootComponent.markDirty();

            if (this.rootComponent instanceof Container container) {
                this.eventDispatcher = new EventDispatcher(container, canvasElement);
            }
        }
    }

    /**
     * Begins the browser-native animation loop.
     */
    public void startRenderLoop() {
        this.lastTime = System.currentTimeMillis();
        scheduleFrame();
    }

    private void scheduleFrame() {
        Window.current().requestAnimationFrame(timestamp -> {
            boolean needsRender = renderManager.isRenderPending() || AnimationManager.getInstance().hasActiveTweens();

            if (rootComponent != null && needsRender) {
                performFrameRender();
            } else {
                // Keep the clock synced even if we skip rendering to prevent huge time jumps
                lastTime = System.currentTimeMillis();
            }
            scheduleFrame();
        });
    }

    /**
     * Orchestrates the full frame lifecycle including animations, layout, and painting.
     */
    private void performFrameRender() {
        // 1. Delta Time Calculation & Animation Pump
        long currentTime = System.currentTimeMillis();
        long delta = currentTime - lastTime;
        lastTime = currentTime;

        AnimationManager.getInstance().tick(delta);

        // 2. Layout Validation
        if (rootComponent instanceof Container container) {
            container.validate();
        }

        // 3. Render Pass
        ctx.clearRect(0, 0, logicalWidth, logicalHeight);
        RenderManager.renderFrame(rootComponent, ctx);
        renderManager.clearRenderPending();
    }
}