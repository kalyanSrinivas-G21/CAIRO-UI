package com.uiframework.cairo.components;

import com.uiframework.cairo.core.LeafComponent;
import com.uiframework.cairo.core.Size;
import org.teavm.jso.canvas.CanvasRenderingContext2D;

/**
 * A concrete UI component for rendering text directly to the Canvas.
 * Labels are "Leaf" components; they do not have children or layout managers.
 */
public class Label extends LeafComponent {

    private String text;
    private String color = "black";
    private String font = "14px Arial";

    public Label(int x, int y, int w, int h, String text) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.text = text;
    }

    /**
     * Calculates the preferred size based on text length.
     * Heuristic: ~8px per character width, 20px height.
     */
    @Override
    public Size getPreferredSize() {
        int prefWidth = (text != null) ? text.length() * 8 : 0;
        return new Size(prefWidth, 20);
    }

    @Override
    public void paint(CanvasRenderingContext2D ctx) {
        if (!visible) return;

        int absX = getAbsoluteX();
        int absY = getAbsoluteY();

        ctx.setFont(font);
        ctx.setFillStyle(color);
        ctx.setTextAlign("left");
        ctx.setTextBaseline("alphabetic");

        int fontSize = parseFontSize();
        double textY = absY + (height / 2.0) + (fontSize / 4.0);

        ctx.fillText(text, absX, textY);
    }

    private int parseFontSize() {
        try {
            String[] parts = font.split(" ");
            for (String part : parts) {
                if (part.contains("px")) {
                    return Integer.parseInt(part.replace("px", "").trim());
                }
            }
        } catch (Exception e) {}
        return 14;
    }

    public void setText(String text) {
        this.text = text;
        invalidate(); // Notify layout that our size might have changed
        markDirty();
    }

    public void setColor(String color) {
        this.color = color;
        markDirty();
    }

    public void setFont(String font) {
        this.font = font;
        invalidate();
        markDirty();
    }

    public String getText() { return text; }
}