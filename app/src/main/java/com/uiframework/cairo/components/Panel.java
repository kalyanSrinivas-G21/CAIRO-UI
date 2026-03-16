package com.uiframework.cairo.components;

import com.uiframework.cairo.core.Container;
import org.teavm.jso.canvas.CanvasRenderingContext2D;

/**
 * A versatile container component that supports background colors,
 * customizable borders, and rounded corners.
 */
public class Panel extends Container {

    private String backgroundColor;
    private String borderColor;
    private int borderWidth = 0;
    private int cornerRadius = 0;

    /**
     * Constructs a Panel with initial bounds.
     */
    public Panel(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
    }

    // --- Fluent Setters (Method Chaining) ---

    public Panel withBackground(String color) {
        this.backgroundColor = color;
        markDirty();
        return this;
    }

    public Panel withBorder(String color, int width) {
        this.borderColor = color;
        this.borderWidth = width;
        markDirty();
        return this;
    }

    public Panel withRadius(int radius) {
        this.cornerRadius = radius;
        markDirty();
        return this;
    }

    @Override
    public void paint(CanvasRenderingContext2D ctx) {
        if (!visible) return;

        double absX = (double) getAbsoluteX();
        double absY = (double) getAbsoluteY();
        double w = (double) width;
        double h = (double) height;

        // 1. Render Background
        if (backgroundColor != null) {
            ctx.setFillStyle(backgroundColor);
            if (cornerRadius > 0) {
                drawRoundedPath(ctx, absX, absY, w, h);
                ctx.fill();
            } else {
                ctx.fillRect(absX, absY, w, h);
            }
        }

        // 2. Render Border
        if (borderColor != null && borderWidth > 0) {
            ctx.setStrokeStyle(borderColor);
            ctx.setLineWidth((double) borderWidth);

            // Adjust bounds by 1px to ensure border stays within component limits
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

    /**
     * Manual path implementation for rounded corners, ensuring
     * compatibility across TeaVM JSO versions.
     */
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
}