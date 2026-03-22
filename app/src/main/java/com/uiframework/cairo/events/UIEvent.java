package com.uiframework.cairo.event;

import com.uiframework.cairo.core.Component;

/**
 * The immutable base class for all internal framework events.
 * Captures the exact source component and the precise time the interaction occurred,
 * fully decoupled from any specific platform implementation like TeaVM or the DOM.
 */
public class UIEvent {

    private final Component source;
    private final long timestamp;

    /**
     * Constructs a new UIEvent.
     *
     * @param source The component that originated this event.
     */
    public UIEvent(Component source) {
        this.source = source;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * Retrieves the component that triggered this event.
     *
     * @return The source Component.
     */
    public Component getSource() {
        return source;
    }

    /**
     * Retrieves the exact time this event was instantiated.
     *
     * @return The timestamp in milliseconds since the Unix epoch.
     */
    public long getTimestamp() {
        return timestamp;
    }
}