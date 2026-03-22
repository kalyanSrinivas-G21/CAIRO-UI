package com.uiframework.cairo.components;

import com.uiframework.cairo.core.LeafComponent;
import com.uiframework.cairo.core.FocusManager;
import com.uiframework.cairo.core.Size;
import com.uiframework.cairo.event.KeyEvent;
import org.teavm.jso.canvas.CanvasRenderingContext2D;

/**
 * A concrete UI component representing a single-line text input field.
 * Manages an internal text buffer and responds natively to focus and keyboard events.
 */
public class TextField extends LeafComponent {

    private StringBuilder textBuffer = new StringBuilder();
    private int cursorPos = 0;

    // Styling properties
    private String backgroundColor = "#FFFFFF";
    private String borderColor = "#CCCCCC";
    private String focusBorderColor = "#007BFF";
    private String textColor = "#333333";
    private String font = "14px monospace";

    /**
     * Constructs a TextField, enables focus tracking, and registers keystroke listeners.
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

        // Ensure this component can capture the keyboard
        this.setFocusable(true);

        // Listen for internal framework events routed by the EventDispatcher
        this.addEventListener(event -> {
            if (event instanceof KeyEvent keyEvent) {
                String key = keyEvent.getKey();

                if ("Backspace".equals(key)) {
                    backspace();
                } else if (key.length() == 1) {
                    // Filter for standard printable characters
                    appendChar(key.charAt(0));
                }
            }
        });
    }

    /**
     * Appends a character at the current cursor position.
     *
     * @param c The character to append.
     */
    public void appendChar(char c) {
        textBuffer.insert(cursorPos, c);
        cursorPos++;
        invalidate(); // Text width may have changed, requires layout check
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

    @Override
    public Size getPreferredSize() {
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

        // Determine if we currently hold the global focus
        boolean isFocused = (FocusManager.getFocusedComponent() == this);

        // 1. Draw Background
        ctx.setFillStyle(backgroundColor);
        ctx.fillRect(absX, absY, w, h);

        // 2. Draw Border
        ctx.setLineWidth(isFocused ? 2.0 : 1.0);
        ctx.setStrokeStyle(isFocused ? focusBorderColor : borderColor);
        ctx.strokeRect(absX, absY, w, h);

        // 3. Draw Text
        ctx.setFillStyle(textColor);
        ctx.setFont(font);
        ctx.setTextAlign("left");
        ctx.setTextBaseline("middle");
        ctx.fillText(textBuffer.toString(), absX + 6, absY + (h / 2.0));

        // 4. Draw Cursor if focused
        if (isFocused) {
            ctx.setFillStyle(focusBorderColor);
            double cursorX = absX + 6 + (cursorPos * 8.0);
            double cursorY = absY + (h * 0.2);
            double cursorHeight = h * 0.6;

            ctx.fillRect(cursorX, cursorY, 2.0, cursorHeight);
        }

        ctx.setTextBaseline("alphabetic");
    }

    /**
     * @return The current text string in the buffer.
     */
    public String getText() { return textBuffer.toString(); }

    /**
     * @return The current integer location of the typing cursor.
     */
    public int getCursorPos() { return cursorPos; }
}