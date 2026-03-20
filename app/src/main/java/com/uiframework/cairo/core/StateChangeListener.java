package com.uiframework.cairo.core;

/**
 * Functional interface for observing state changes within UI components.
 * This decouples the component logic from the rendering engine.
 */
@FunctionalInterface
public interface StateChangeListener {

    /**
     * Invoked when a component's internal state changes, requiring a UI update.
     * * @param source The component that triggered the change.
     */
    void onStateChanged(Component source);
}