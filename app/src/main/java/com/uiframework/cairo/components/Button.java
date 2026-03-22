package com.uiframework.cairo.components;

import com.uiframework.cairo.core.LeafComponent;
import com.uiframework.cairo.core.Size;
import com.uiframework.cairo.event.MouseEvent;
import com.uiframework.cairo.event.MouseEventType;
import org.teavm.jso.canvas.CanvasRenderingContext2D;

/**
 * A concrete UI component representing an interactive button.
 * Integrates directly with the CAIRO event architecture to automatically
 * manage visual hover and press states.
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

    /**
     * Constructs a button and attaches internal listeners for state management.
     *
     * @param x     Local X coordinate.
     * @param y     Local Y coordinate.
     * @param w     Width of the button.
     * @param h     Height of the button.
     * @param label The text displayed on the button.
     */
    public Button(int x, int y, int w, int h, String label) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.label = label;

        // Automatically respond to dispatched events to alter visuals
        addEventListener(event -> {
            if (event instanceof MouseEvent mouseEvent) {
                switch (mouseEvent.getType()) {
                    case MOUSE_ENTERED -> setState(State.HOVERED);
                    case MOUSE_EXITED -> setState(State.NORMAL);
                    case MOUSE_PRESSED -> setState(State.PRESSED);
                    case MOUSE_RELEASED -> {
                        // Return to hovered state and fire click action
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

        String bgColor = switch (state) {
            case NORMAL -> normalBg;
            case HOVERED -> hoveredBg;
            case PRESSED -> pressedBg;
        };

        ctx.setFillStyle(bgColor);
        ctx.beginPath();
        double r = 8.0;

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
        ctx.fill();

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
     * Changes the visual state of the button and queues a redraw.
     *
     * @param s The target State.
     */
    public void setState(State s) {
        if (this.state != s) {
            this.state = s;
            markDirty();
        }
    }

    /**
     * Sets the external callback to be invoked upon a successful click.
     *
     * @param handler The logic to execute.
     */
    public void setOnClick(Runnable handler) {
        this.onClickHandler = handler;
    }

    /**
     * Executes the click handler.
     */
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