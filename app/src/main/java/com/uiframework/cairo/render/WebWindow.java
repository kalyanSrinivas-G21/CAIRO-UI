package com.uiframework.cairo.render;

import com.uiframework.cairo.core.Component;
import com.uiframework.cairo.core.Container;
import com.uiframework.cairo.event.EventDispatcher;
import org.teavm.jso.browser.Window;
import org.teavm.jso.canvas.CanvasRenderingContext2D;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.dom.html.HTMLDocument;

/**
 * The WebWindow serves as the primary host for the UI framework within the browser.
 * Updated to initialize the EventDispatcher and pipe raw interactions into the UI tree.
 */
public class WebWindow {

    private final HTMLCanvasElement canvasElement;
    private final CanvasRenderingContext2D ctx;
    private Component rootComponent;
    private final RenderManager renderManager;
    private EventDispatcher eventDispatcher;

    private int logicalWidth;
    private int logicalHeight;

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

            // Establish the event pipeline if the root is a container capable of hit-testing
            if (this.rootComponent instanceof Container container) {
                this.eventDispatcher = new EventDispatcher(container, canvasElement);
            }
        }
    }

    /**
     * Begins the browser-native animation loop.
     */
    public void startRenderLoop() {
        scheduleFrame();
    }

    private void scheduleFrame() {
        Window.current().requestAnimationFrame(timestamp -> {
            if (rootComponent != null && renderManager.isRenderPending()) {
                performFrameRender();
            }
            scheduleFrame();
        });
    }

    /**
     * Orchestrates the full frame lifecycle.
     */
    private void performFrameRender() {
        if (rootComponent instanceof Container container) {
            container.validate();
        }

        ctx.clearRect(0, 0, logicalWidth, logicalHeight);
        RenderManager.renderFrame(rootComponent, ctx);
        renderManager.clearRenderPending();
    }
}