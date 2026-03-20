package com.uiframework.cairo.components;

import com.uiframework.cairo.core.Container;
import com.uiframework.cairo.core.LayoutManager;
import com.uiframework.cairo.core.Size;
import com.uiframework.cairo.layout.FlowLayout;
import org.teavm.jso.canvas.CanvasRenderingContext2D;

/**
 * A versatile container component that supports background colors,
 * customizable borders, and rounded corners. Now features automated
 * flow-based layout management by default.
 */
public class Panel extends Container {

    private String backgroundColor;
    private String borderColor;
    private int borderWidth = 0;
    private int cornerRadius = 0;

    /**
     * Constructs a Panel with initial bounds and initializes a default FlowLayout.
     *
     * @param x The initial local X coordinate.
     * @param y The initial local Y coordinate.
     * @param w The initial width.
     * @param h The initial height.
     */
    public Panel(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        // Initialize with default FlowLayout as per Day 27 requirements
        setLayoutManager(new FlowLayout(5, 5));
    }

    /**
     * Ensures the component layout is up to date. If the layout is invalid,
     * it triggers the LayoutManager to reposition children.
     */
    public void validate() {
        if (!layoutValid) {
            LayoutManager manager = getLayoutManager();
            if (manager != null) {
                manager.layout(this);
            }
            this.layoutValid = true;
        }
    }

    /**
     * Triggers the layout manager to reposition all children.
     * This method explicitly marks the layout as valid upon completion.
     */
    public void doLayout() {
        if (getLayoutManager() != null) {
            getLayoutManager().layout(this);
            this.layoutValid = true;
        }
    }

    /**
     * Fluent setter for background color.
     * @param color The CSS color string.
     * @return This Panel instance.
     */
    public Panel withBackground(String color) {
        this.backgroundColor = color;
        markDirty();
        return this;
    }

    /**
     * Fluent setter for border properties.
     * @param color The CSS color string.
     * @param width The border width in pixels.
     * @return This Panel instance.
     */
    public Panel withBorder(String color, int width) {
        this.borderColor = color;
        this.borderWidth = width;
        markDirty();
        return this;
    }

    /**
     * Fluent setter for corner radius.
     * @param radius The radius in pixels.
     * @return This Panel instance.
     */
    public Panel withRadius(int radius) {
        this.cornerRadius = radius;
        markDirty();
        return this;
    }

    @Override
    public Size getPreferredSize() {
        LayoutManager lm = getLayoutManager();
        if (lm != null) {
            return new Size(lm.getPreferredWidth(this), lm.getPreferredHeight(this));
        }
        return new Size(width, height);
    }

    @Override
    public void paint(CanvasRenderingContext2D ctx) {
        if (!visible) return;

        double absX = (double) getAbsoluteX();
        double absY = (double) getAbsoluteY();
        double w = (double) width;
        double h = (double) height;

        if (backgroundColor != null) {
            ctx.setFillStyle(backgroundColor);
            if (cornerRadius > 0) {
                drawRoundedPath(ctx, absX, absY, w, h);
                ctx.fill();
            } else {
                ctx.fillRect(absX, absY, w, h);
            }
        }

        if (borderColor != null && borderWidth > 0) {
            ctx.setStrokeStyle(borderColor);
            ctx.setLineWidth((double) borderWidth);

            double strokeW = w - 1;
            double strokeH = h - 1;

            if (cornerRadius > 0) {
                drawRoundedPath(ctx, absX, absY, strokeW, strokeH);
                ctx.stroke();
            } else {
                ctx.strokeRect(absX, absY, strokeW, strokeH);
            }
        }
    }

    private void drawRoundedPath(CanvasRenderingContext2D ctx, double x, double y, double w, double h) {
        double r = (double) cornerRadius;
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
    }

    /**
     * @return The corner radius of this panel.
     */
    public int getCornerRadius() {
        return this.cornerRadius;
    }
}