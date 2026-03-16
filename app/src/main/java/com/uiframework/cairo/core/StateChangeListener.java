package com.uiframework.cairo.core;

/**
 * A functional interface for observing state changes within a Component.
 * Used to trigger re-layouts or re-renders when a component's properties update.
 */
@FunctionalInterface
public interface StateChangeListener {

    /**
     * Invoked when the observed component's state changes (e.g., bounds updated, marked dirty).
     *
     * @param source The component that triggered the state change.
     */
    void onStateChanged(Component source);
}