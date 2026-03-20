package com.uiframework.cairo.components;

import com.uiframework.cairo.core.LeafComponent;
import com.uiframework.cairo.core.Size;
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

    /**
     * Constructs a TextField with specified bounds.
     *
     * @param x Local X coordinate.
     * @param y Local Y coordinate.
     * @param w Component width.
     * @param h Component height.
     */
    public TextField(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
    }

    /**
     * Appends a character at the current cursor position.
     * @param c Character to add.
     */
    public void appendChar(char c) {
        textBuffer.insert(cursorPos, c);
        cursorPos++;
        invalidate(); // Layout may need to change if text grows
        markDirty();
    }

    /**
     * Deletes the character immediately preceding the cursor.
     */
    public void backspace() {
        if (cursorPos > 0) {
            textBuffer.deleteCharAt(cursorPos - 1);
            cursorPos--;
            invalidate();
            markDirty();
        }
    }

    /**
     * Sets the focus state of the text field.
     * @param f True to focus, false to blur.
     */
    public void setFocused(boolean f) {
        if (this.focused != f) {
            this.focused = f;
            markDirty();
        }
    }

    /**
     * Fulfills the Component contract for preferred sizing.
     * @return The ideal size for the text field.
     */
    @Override
    public Size getPreferredSize() {
        // Monospace approximation: 8px per char + 12px padding
        int textWidth = textBuffer.length() * 8;
        int prefWidth = Math.max(150, textWidth + 12);
        return new Size(prefWidth, 30);
    }

    @Override
    public void paint(CanvasRenderingContext2D ctx) {
        if (!visible) return;

        double absX = (double) getAbsoluteX();
        double absY = (double) getAbsoluteY();
        double w = (double) width;
        double h = (double) height;

        ctx.setFillStyle(backgroundColor);
        ctx.fillRect(absX, absY, w, h);

        ctx.setLineWidth(focused ? 2.0 : 1.0);
        ctx.setStrokeStyle(focused ? focusBorderColor : borderColor);
        ctx.strokeRect(absX, absY, w, h);

        ctx.setFillStyle(textColor);
        ctx.setFont(font);
        ctx.setTextAlign("left");
        ctx.setTextBaseline("middle");

        ctx.fillText(textBuffer.toString(), absX + 6, absY + (h / 2.0));

        if (focused) {
            ctx.setFillStyle(focusBorderColor);
            double cursorX = absX + 6 + (cursorPos * 8.0);
            double cursorY = absY + (h * 0.2);
            double cursorHeight = h * 0.6;
            ctx.fillRect(cursorX, cursorY, 2.0, cursorHeight);
        }

        ctx.setTextBaseline("alphabetic");
    }

    public String getText() { return textBuffer.toString(); }
    public boolean isFocused() { return focused; }
    public int getCursorPos() { return cursorPos; }
}