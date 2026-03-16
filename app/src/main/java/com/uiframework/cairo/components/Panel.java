package com.uiframework.cairo.components;

import com.uiframework.cairo.core.Container;
import org.teavm.jso.canvas.CanvasRenderingContext2D;

/**
 * A standard UI container used to group other components and provide a background.
 */
public class Panel extends Container {

    // Using CSS color strings to map directly to Canvas API expectations
    private String backgroundColor;
    private String borderColor;

    /**
     * Constructs a new Panel with defined bounds and a background color.
     *
     * @param x  The local X coordinate.
     * @param y  The local Y coordinate.
     * @param w  The width of the panel.
     * @param h  The height of the panel.
     * @param bg The CSS color string for the background (e.g., "#FFFFFF" or "blue").
     */
    public Panel(int x, int y, int w, int h, String bg) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.backgroundColor = bg;
    }

    @Override
    public void paint(CanvasRenderingContext2D ctx) {
        // Stub: Rendering logic to be implemented in Week 3
    }
}