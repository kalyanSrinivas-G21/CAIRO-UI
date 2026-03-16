package com.uiframework.cairo.components;

import com.uiframework.cairo.core.LeafComponent;
import org.teavm.jso.canvas.CanvasRenderingContext2D;

/**
 * A concrete UI component for rendering text directly to the Canvas.
 * Supports absolute positioning and basic CSS-style font/color properties.
 */
public class Label extends LeafComponent {

    private String text;
    private String color = "black";
    private String font = "14px Arial";

    /**
     * Constructs a new Label with specific bounds and text.
     *
     * @param x      Local X coordinate relative to parent.
     * @param y      Local Y coordinate relative to parent.
     * @param w      Width of the label.
     * @param h      Height of the label.
     * @param text   The content to display.
     */
    public Label(int x, int y, int w, int h, String text) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.text = text;
    }

    @Override
    public void paint(CanvasRenderingContext2D ctx) {
        if (!visible) return;

        int absX = getAbsoluteX();
        int absY = getAbsoluteY();

        // Configure Canvas Context
        ctx.setFont(font);
        ctx.setFillStyle(color);
        ctx.setTextAlign("left");
        ctx.setTextBaseline("alphabetic");

        // Vertical Centering Calculation
        // Since Canvas uses alphabetic baseline, we approximate center using:
        // y_offset = absY + (totalHeight / 2) + (fontSize / 2.5)
        // We use 2.5 as a magic number to account for the baseline being above the descent.
        int fontSize = parseFontSize();
        double textY = absY + (height / 2.0) + (fontSize / 4.0);

        ctx.fillText(text, absX, textY);
    }

    /**
     * Extracts the pixel integer from a CSS font string (e.g., "14px" -> 14).
     * Defaults to 14 if parsing fails.
     */
    private int parseFontSize() {
        try {
            // Regex to find digits followed by 'px'
            String[] parts = font.split(" ");
            for (String part : parts) {
                if (part.contains("px")) {
                    return Integer.parseInt(part.replace("px", "").trim());
                }
            }
        } catch (Exception e) {
            // Fallback for malformed font strings
        }
        return 14;
    }

    // Setters with dirty marking

    public void setText(String text) {
        this.text = text;
        markDirty();
    }

    public void setColor(String color) {
        this.color = color;
        markDirty();
    }

    public void setFont(String font) {
        this.font = font;
        markDirty();
    }

    public String getText() { return text; }
    public String getColor() { return color; }
    public String getFont() { return font; }
}