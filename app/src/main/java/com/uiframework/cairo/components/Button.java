package com.uiframework.cairo.components;

import com.uiframework.cairo.core.LeafComponent;
import org.teavm.jso.canvas.CanvasRenderingContext2D;
import com.uiframework.cairo.core.Size;

/**
 * A concrete UI component representing an interactive button.
 * Uses manual quadratic curves for rounded corners to ensure TeaVM JSO compatibility.
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

    public Button(int x, int y, int w, int h, String label) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.label = label;
    }

    @Override
    public Size getPreferredSize() {
        int prefWidth = (label != null) ? (label.length() * 8) + 20 : 60;
        return new Size(prefWidth, 30);
    }



    @Override
    public void paint(CanvasRenderingContext2D ctx) {
        if (!visible) return;

        double absX = (double) getAbsoluteX();
        double absY = (double) getAbsoluteY();
        double w = (double) width;
        double h = (double) height;

        // 1. Determine background color
        String bgColor = switch (state) {
            case NORMAL -> normalBg;
            case HOVERED -> hoveredBg;
            case PRESSED -> pressedBg;
        };

        // 2. Draw Button Shape (Manual Rounded Path)
        ctx.setFillStyle(bgColor);
        ctx.beginPath();
        double r = 8.0; // corner radius

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

        // 3. Render Label Text
        ctx.setFillStyle(textColor);
        ctx.setFont(font);
        ctx.setTextAlign("center");
        ctx.setTextBaseline("middle");

        ctx.fillText(label, absX + (w / 2.0), absY + (h / 2.0));

        // 4. Reset alignments
        ctx.setTextAlign("left");
        ctx.setTextBaseline("alphabetic");
    }

    public void setState(State s) {
        if (this.state != s) {
            this.state = s;
            markDirty();
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
    public void setLabel(String label) { this.label = label; markDirty(); }
    public State getState() { return state; }
}