package com.uiframework.cairo.event;

import com.uiframework.cairo.core.Component;

/**
 * A specialized UIEvent that encapsulates keyboard interaction data,
 * including the specific key pressed, its code, and modifier states.
 */
public class KeyEvent extends UIEvent {

    private final String key;
    private final int keyCode;
    private final boolean shiftDown;

    /**
     * Constructs a new KeyEvent.
     *
     * @param source    The component that holds focus and originated this event.
     * @param key       The string representation of the key pressed.
     * @param keyCode   The numeric key code.
     * @param shiftDown True if the shift key was held down during the event.
     */
    public KeyEvent(Component source, String key, int keyCode, boolean shiftDown) {
        super(source);
        this.key = key;
        this.keyCode = keyCode;
        this.shiftDown = shiftDown;
    }

    /**
     * Retrieves the string value of the key pressed (e.g., "a", "Enter", "Backspace").
     *
     * @return The key string.
     */
    public String getKey() {
        return key;
    }

    /**
     * Retrieves the numeric key code associated with the key press.
     *
     * @return The integer key code.
     */
    public int getKeyCode() {
        return keyCode;
    }

    /**
     * Checks if the shift modifier was active during the key press.
     *
     * @return True if shift was down, false otherwise.
     */
    public boolean isShiftDown() {
        return shiftDown;
    }
}