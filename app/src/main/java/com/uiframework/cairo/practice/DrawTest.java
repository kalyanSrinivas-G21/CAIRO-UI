package com.uiframework.cairo.practice;

import org.teavm.jso.browser.Window;
import org.teavm.jso.canvas.CanvasRenderingContext2D;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;

/**
 * A standalone practice class to verify native HTML5 Canvas 2D rendering via TeaVM.
 * Runs on the JS compilation target for 100% stable DOM interop.
 */
public class DrawTest {

    public static void main(String[] args) {
        HTMLDocument document = Window.current().getDocument();
        HTMLElement element = document.getElementById("ui-canvas");

        if (element == null) {
            System.out.println("Error: Could not find HTML element with ID 'ui-canvas'.");
            return;
        }

        HTMLCanvasElement canvas = element.cast();
        CanvasRenderingContext2D ctx = (CanvasRenderingContext2D) canvas.getContext("2d");

        // 1. Clear the entire canvas
        ctx.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // 2. Fill the background with a light gray color
        ctx.setFillStyle("lightgray");
        ctx.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // 3. Draw a blue rounded rectangle (simulating a button)
        double x = 50.0, y = 50.0, w = 150.0, h = 50.0, r = 10.0;

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

        // 4. Draw the white text "Click Me" inside the rounded rectangle
        ctx.setFillStyle("white");
        ctx.setFont("bold 16px Arial");

        // Adjust alignment and baseline to easily center text relative to our coordinates
        ctx.setTextAlign("center");
        ctx.setTextBaseline("middle");

        // Calculate the exact center of the button for our text anchor
        double centerX = x + (w / 2.0);
        double centerY = y + (h / 2.0);

        ctx.fillText("Click Me", centerX, centerY);

        System.out.println("Canvas render test completed successfully.");
    }
}