package com.uiframework.cairo.event;

/**
 * Defines the various types of mouse interactions that can occur
 * within the CAIRO UI framework.
 */
public enum MouseEventType {
    /** Triggered when a mouse button is pressed down. */
    MOUSE_PRESSED,
    /** Triggered when a mouse button is released. */
    MOUSE_RELEASED,
    /** Triggered when a mouse button is pressed and released on the same component. */
    MOUSE_CLICKED,
    /** Triggered when the mouse moves over a component. */
    MOUSE_MOVED,
    /** Triggered when the mouse cursor enters a component's bounds. */
    MOUSE_ENTERED,
    /** Triggered when the mouse cursor leaves a component's bounds. */
    MOUSE_EXITED
}