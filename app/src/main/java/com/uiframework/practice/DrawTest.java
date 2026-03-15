package com.uiframework.practice;

import org.teavm.jso.JSBody;
import org.teavm.jso.browser.Window;
import org.teavm.jso.canvas.CanvasRenderingContext2D;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;

/**
 * A standalone practice class to verify native HTML5 Canvas 2D rendering via TeaVM.
 * Refactored with JSBody shims for strict WebAssembly compatibility.
 */
public class DrawTest {

    /**
     * Shim to safely retrieve the browser window object in a Wasm context.
     * @return The JavaScript 'window' object.
     */
    @JSBody(params = {}, script = "return window;")
    private static native Window getBrowserWindow();

    public static void main(String[] args) {
        render();
    }

    private static void render() {
        // Use our shim instead of Window.current()
        Window window = getBrowserWindow();
        HTMLDocument document = window.getDocument();
        HTMLElement element = document.getElementById("ui-canvas");

        if (element == null) {
            System.out.println("Canvas element not found!");
            return;
        }

        HTMLCanvasElement canvas = element.cast();
        CanvasRenderingContext2D ctx = (CanvasRenderingContext2D) canvas.getContext("2d");

        // Clear and Draw Background
        ctx.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        ctx.setFillStyle("#eeeeee");
        ctx.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Draw Blue Button
        double x = 50, y = 50, w = 150, h = 50, r = 12;
        ctx.setFillStyle("#007BFF");

        ctx.beginPath();
        ctx.moveTo(x + r, y);
        ctx.lineTo(x + w - r, y);
        ctx.quadraticCurveTo(x + w, y, x + w, y + r);
        ctx.lineTo(x + w, y + h - r);
        ctx.quadraticCurveTo(x + w, y + h, x + w - r, y + h);
        ctx.lineTo(x + r, y + h);
        ctx.quadraticCurveTo(x, y + h, x, y + h - r);
        ctx.lineTo(x, y + r);
        ctx.quadraticCurveTo(x, y, x + r, y);
        ctx.closePath();
        ctx.fill();

        // Draw Text
        ctx.setFillStyle("white");
        ctx.setFont("bold 16px sans-serif");
        ctx.setTextAlign("center");
        ctx.setTextBaseline("middle");
        ctx.fillText("Click Me", x + (w / 2.0), y + (h / 2.0));
    }
}