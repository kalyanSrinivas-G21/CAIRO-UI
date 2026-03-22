package com.uiframework.cairo.core;

/**
 * A centralized utility class that tracks the currently focused component
 * within the CAIRO framework. Ensures exclusive focus behavior for keyboard routing.
 */
public class FocusManager {

    private static Component focusedComponent;

    /**
     * Attempts to set the globally focused component.
     * Triggers a visual update (markDirty) on both the outgoing and incoming components
     * to ensure focus rings and cursors render correctly.
     *
     * @param comp The component requesting focus, or null to clear focus.
     */
    public static void setFocus(Component comp) {
        if (focusedComponent == comp) {
            return;
        }

        Component oldFocus = focusedComponent;

        // Only allow focus if the component explicitly supports it
        if (comp != null && !comp.isFocusable()) {
            focusedComponent = null;
        } else {
            focusedComponent = comp;
        }

        // Notify components that their visual focus state may have changed
        if (oldFocus != null) {
            oldFocus.markDirty();
        }
        if (focusedComponent != null) {
            focusedComponent.markDirty();
        }
    }

    /**
     * Retrieves the component that currently owns the keyboard focus.
     *
     * @return The focused Component, or null if no component has focus.
     */
    public static Component getFocusedComponent() {
        return focusedComponent;
    }
}