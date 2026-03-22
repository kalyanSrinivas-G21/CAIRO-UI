package com.uiframework.cairo.event;

import com.uiframework.cairo.core.Component;

/**
 * A specialized UIEvent that encapsulates mouse interaction data,
 * including the exact screen coordinates and the type of mouse action.
 */
public class MouseEvent extends UIEvent {

    private final int x;
    private final int y;
    private final MouseEventType type;

    /**
     * Constructs a new MouseEvent.
     *
     * @param source The component that originated this event.
     * @param type   The specific type of mouse action.
     * @param x      The absolute X coordinate of the mouse on the screen/canvas.
     * @param y      The absolute Y coordinate of the mouse on the screen/canvas.
     */
    public MouseEvent(Component source, MouseEventType type, int x, int y) {
        super(source);
        this.type = type;
        this.x = x;
        this.y = y;
    }

    /**
     * Retrieves the absolute X coordinate of the mouse event.
     *
     * @return The absolute X coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Retrieves the absolute Y coordinate of the mouse event.
     *
     * @return The absolute Y coordinate.
     */
    public int getY() {
        return y;
    }

    /**
     * Retrieves the type of mouse action that triggered this event.
     *
     * @return The MouseEventType enum value.
     */
    public MouseEventType getType() {
        return type;
    }
}