package com.uiframework.cairo.components;

import com.uiframework.cairo.animation.AnimationManager;
import com.uiframework.cairo.animation.Interpolator;
import com.uiframework.cairo.animation.Tween;
import com.uiframework.cairo.core.LeafComponent;
import com.uiframework.cairo.core.Size;
import com.uiframework.cairo.event.MouseEvent;
import com.uiframework.cairo.event.MouseEventType;
import org.teavm.jso.canvas.CanvasRenderingContext2D;

/**
 * A concrete UI component representing an interactive button.
 * Uses the Tween engine to perform smooth, 300ms visual transitions on hover.
 */
public class Button extends LeafComponent {

    public enum State {
        NORMAL,
        HOVERED,
        PRESSED
    }

    private String label;
    private State state = State.NORMAL;
    private Runnable onClickHandler;

    private String normalBg = "#007BFF";
    private String hoveredBg = "#0056b3";
    private String pressedBg = "#004085";
    private String textColor = "white";
    private String font = "bold 14px sans-serif";

    // Animation state
    private double hoverAlpha = 0.0;
    private Tween activeTween;

    public Button(int x, int y, int w, int h, String label) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.label = label;

        addEventListener(event -> {
            if (event instanceof MouseEvent mouseEvent) {
                switch (mouseEvent.getType()) {
                    case MOUSE_ENTERED -> setState(State.HOVERED);
                    case MOUSE_EXITED -> setState(State.NORMAL);
                    case MOUSE_PRESSED -> setState(State.PRESSED);
                    case MOUSE_RELEASED -> {
                        setState(State.HOVERED);
                        click();
                    }
                    default -> {}
                }
            }
        });
    }

    @Override
    public void paint(CanvasRenderingContext2D ctx) {
        if (!visible) return;

        double absX = (double) getAbsoluteX();
        double absY = (double) getAbsoluteY();
        double w = (double) width;
        double h = (double) height;
        double r = 8.0;

        // Base Path
        ctx.beginPath();
        ctx.moveTo(absX + r, absY);
        ctx.lineTo(absX + w - r, absY);
        ctx.quadraticCurveTo(absX + w, absY, absX + w, absY + r);
        ctx.lineTo(absX + w, absY + h - r);
        ctx.quadraticCurveTo(absX + w, absY + h, absX + w - r, absY + h);
        ctx.lineTo(absX + r, absY + h);
        ctx.quadraticCurveTo(absX, absY + h, absX, absY + h - r);
        ctx.lineTo(absX, absY + r);
        ctx.quadraticCurveTo(absX, absY, absX + r, absY);
        ctx.closePath();

        // 1. Draw Normal Background underneath
        ctx.setFillStyle(normalBg);
        ctx.fill();

        // 2. Draw Hover/Press Overlay with interpolated Alpha
        if (hoverAlpha > 0.0) {
            ctx.setGlobalAlpha(hoverAlpha);
            ctx.setFillStyle(state == State.PRESSED ? pressedBg : hoveredBg);
            ctx.fill();
            ctx.setGlobalAlpha(1.0); // Reset for child/text rendering
        }

        // 3. Draw Label Text
        ctx.setFillStyle(textColor);
        ctx.setFont(font);
        ctx.setTextAlign("center");
        ctx.setTextBaseline("middle");
        ctx.fillText(label, absX + (w / 2.0), absY + (h / 2.0));

        ctx.setTextAlign("left");
        ctx.setTextBaseline("alphabetic");
    }

    @Override
    public Size getPreferredSize() {
        int prefWidth = (label != null) ? (label.length() * 8) + 20 : 60;
        return new Size(prefWidth, 30);
    }

    /**
     * Changes the state of the button, initiating a smooth Tween to the new visual overlay.
     *
     * @param s The new target state.
     */
    public void setState(State s) {
        if (this.state != s) {
            this.state = s;

            // Cancel any ongoing transition to prevent visual tug-of-war
            if (activeTween != null) {
                activeTween.cancel();
            }

            double targetAlpha = (s == State.NORMAL) ? 0.0 : 1.0;

            // Initiate a 300ms transition using Ease-Out Quad for a natural feel
            activeTween = new Tween(hoverAlpha, targetAlpha, 300, Interpolator.EASE_OUT_QUAD, val -> {
                this.hoverAlpha = val;
                this.markDirty(); // Triggers a repaint pipeline every frame during the animation
            });

            AnimationManager.getInstance().add(activeTween);
        }
    }

    public void setOnClick(Runnable handler) {
        this.onClickHandler = handler;
    }

    public void click() {
        if (onClickHandler != null) {
            onClickHandler.run();
        }
    }

    public String getLabel() { return label; }

    public void setLabel(String label) {
        this.label = label;
        invalidate();
        markDirty();
    }

    public State getState() { return state; }
}