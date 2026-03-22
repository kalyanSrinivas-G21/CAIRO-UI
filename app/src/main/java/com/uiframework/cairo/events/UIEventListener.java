package com.uiframework.cairo.event;

/**
 * A functional interface for handling UI events broadcasted by components.
 * Adheres to the Observer pattern, allowing subscribers to react to user interactions.
 */
@FunctionalInterface
public interface UIEventListener {

    /**
     * Invoked when an event occurs on a subscribed component.
     *
     * @param event The event object containing interaction details and source information.
     */
    void handleEvent(UIEvent event);
}