package com.uiframework.cairo.components;

import com.uiframework.cairo.core.LeafComponent;
import org.teavm.jso.canvas.CanvasRenderingContext2D;

/**
 * A basic terminal UI component for displaying text.
 * Implemented to satisfy structural testing requirements for the Component tree.
 */
public class Label extends LeafComponent {

    private String text;

    /**
     * Constructs a new Label.
     *
     * @param text The string to display.
     */
    public Label(String text) {
        this.text = text;
    }

    /**
     * @return The text currently displayed by this label.
     */
    public String getText() {
        return text;
    }

    /**
     * Updates the text and marks the component for a redraw.
     *
     * @param text The new text to display.
     */
    public void setText(String text) {
        this.text = text;
        markDirty();
    }

    @Override
    public void paint(CanvasRenderingContext2D ctx) {
        // Stub: Text rendering logic to be implemented in Week 3
    }
}