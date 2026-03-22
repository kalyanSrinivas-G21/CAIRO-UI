package com.uiframework.cairo.components;

import com.uiframework.cairo.core.Container;
import com.uiframework.cairo.core.LayoutManager;
import com.uiframework.cairo.core.Size;
import com.uiframework.cairo.layout.FlowLayout;
import com.uiframework.cairo.style.StyleKey;
import org.teavm.jso.canvas.CanvasRenderingContext2D;

/**
 * A versatile container component that supports background colors,
 * customizable borders, and rounded corners. Now features automated
 * flow-based layout management and global Skin rendering.
 */
public class Panel extends Container {

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

    // --- Fluent Setters (Method Chaining via Style) ---

    public Panel withBackground(String color) {
        getLocalStyle().set(StyleKey.BACKGROUND, color);
        markDirty();
        return this;
    }

    public Panel withBorder(String color, int width) {
        getLocalStyle().set(StyleKey.BORDER_COLOR, color);
        getLocalStyle().set(StyleKey.BORDER_WIDTH, width);
        markDirty();
        return this;
    }

    public Panel withRadius(int radius) {
        getLocalStyle().set(StyleKey.CORNER_RADIUS, radius);
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

        // Resolve attributes from the Style engine
        String bgColor = resolveString(StyleKey.BACKGROUND);
        String borderColor = resolveString(StyleKey.BORDER_COLOR);
        int borderWidth = resolveInt(StyleKey.BORDER_WIDTH);
        int cornerRadius = resolveInt(StyleKey.CORNER_RADIUS);

        if (bgColor != null && !bgColor.equals("null") && !bgColor.isEmpty()) {
            ctx.setFillStyle(bgColor);
            if (cornerRadius > 0) {
                drawRoundedPath(ctx, absX, absY, w, h, cornerRadius);
                ctx.fill();
            } else {
                ctx.fillRect(absX, absY, w, h);
            }
        }

        if (borderColor != null && !borderColor.equals("null") && borderWidth > 0) {
            ctx.setStrokeStyle(borderColor);
            ctx.setLineWidth((double) borderWidth);

            double strokeW = w - 1;
            double strokeH = h - 1;

            if (cornerRadius > 0) {
                drawRoundedPath(ctx, absX, absY, strokeW, strokeH, cornerRadius);
                ctx.stroke();
            } else {
                ctx.strokeRect(absX, absY, strokeW, strokeH);
            }
        }
    }

    private void drawRoundedPath(CanvasRenderingContext2D ctx, double x, double y, double w, double h, double r) {
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
        return resolveInt(StyleKey.CORNER_RADIUS);
    }
}