package com.uiframework.cairo.components;

import com.uiframework.cairo.core.LeafComponent;
import org.teavm.jso.canvas.CanvasRenderingContext2D;

/**
 * A concrete UI component representing a single-line text input field.
 * Manages an internal text buffer, cursor position, and focus-based styling.
 */
public class TextField extends LeafComponent {

    private StringBuilder textBuffer = new StringBuilder();
    private int cursorPos = 0;
    private boolean focused = false;

    // Styling properties
    private String backgroundColor = "#FFFFFF";
    private String borderColor = "#CCCCCC";
    private String focusBorderColor = "#007BFF";
    private String textColor = "#333333";
    private String font = "14px monospace";

    public TextField(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
    }

    /**
     * Appends a character at the current cursor position.
     */
    public void appendChar(char c) {
        textBuffer.insert(cursorPos, c);
        cursorPos++;
        markDirty();
    }

    /**
     * Deletes the character immediately preceding the cursor.
     */
    public void backspace() {
        if (cursorPos > 0) {
            textBuffer.deleteCharAt(cursorPos - 1);
            cursorPos--;
            markDirty();
        }
    }

    /**
     * Sets the focus state of the text field.
     * Triggers a redraw if the focus state changes (to update the border and cursor).
     */
    public void setFocused(boolean f) {
        if (this.focused != f) {
            this.focused = f;
            markDirty();
        }
    }

    @Override
    public void paint(CanvasRenderingContext2D ctx) {
        if (!visible) return;

        double absX = (double) getAbsoluteX();
        double absY = (double) getAbsoluteY();
        double w = (double) width;
        double h = (double) height;

        // 1. Draw Background
        ctx.setFillStyle(backgroundColor);
        ctx.fillRect(absX, absY, w, h);

        // 2. Draw Border
        ctx.setLineWidth(focused ? 2.0 : 1.0);
        ctx.setStrokeStyle(focused ? focusBorderColor : borderColor);
        ctx.strokeRect(absX, absY, w, h);

        // 3. Draw Text
        ctx.setFillStyle(textColor);
        ctx.setFont(font);
        ctx.setTextAlign("left");
        ctx.setTextBaseline("middle");

        // Add a small horizontal padding of 6px
        ctx.fillText(textBuffer.toString(), absX + 6, absY + (h / 2.0));

        // 4. Draw Cursor if focused
        if (focused) {
            ctx.setFillStyle(focusBorderColor);
            // Estimate x-offset: padding + (cursor index * estimated char width)
            // Using 8px as an approximation for monospace characters
            double cursorX = absX + 6 + (cursorPos * 8.0);
            double cursorY = absY + (h * 0.2); // 20% down from top
            double cursorHeight = h * 0.6;    // 60% of total height

            ctx.fillRect(cursorX, cursorY, 2.0, cursorHeight);
        }

        // Reset baseline
        ctx.setTextBaseline("alphabetic");
    }

    // Getters
    public String getText() { return textBuffer.toString(); }
    public boolean isFocused() { return focused; }
    public int getCursorPos() { return cursorPos; }
}