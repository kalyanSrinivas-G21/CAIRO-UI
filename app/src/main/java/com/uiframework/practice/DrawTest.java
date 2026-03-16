package com.uiframework.practice;

import org.teavm.jso.browser.Window;
import org.teavm.jso.canvas.CanvasRenderingContext2D;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;

/**
 * A standalone practice class to verify native HTML5 Canvas 2D rendering via TeaVM.
 * Restored to pure JSO logic without JSBody shims, running over the Wasm interop bridge.
 */
public class DrawTest {

    public static void main(String[] args) {
        HTMLDocument document = Window.current().getDocument();
        HTMLElement element = document.getElementById("ui-canvas");

        if (element == null) {
            System.out.println("Canvas element not found!");
            return;
        }

        HTMLCanvasElement canvas = element.cast();
        CanvasRenderingContext2D ctx = (CanvasRenderingContext2D) canvas.getContext("2d");

        // 1. Clear and Background
        ctx.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        ctx.setFillStyle("#eeeeee");
        ctx.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // 2. Rounded Button (Blue)
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

        // 3. Text
        ctx.setFillStyle("white");
        ctx.setFont("bold 16px sans-serif");
        ctx.setTextAlign("center");
        ctx.setTextBaseline("middle");
        ctx.fillText("Click Me", x + (w / 2.0), y + (h / 2.0));
    }
}