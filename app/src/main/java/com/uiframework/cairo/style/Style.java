package com.uiframework.cairo.style;

import java.util.EnumMap;

/**
 * A container that maps StyleKey attributes to specific values (Colors, Integers, etc.).
 * Acts as the payload for component visual definitions.
 */
public class Style {

    private final EnumMap<StyleKey, Object> properties = new EnumMap<>(StyleKey.class);

    /**
     * Stores a visual property value.
     *
     * @param key   The attribute to set.
     * @param value The value to apply (e.g., "#FF0000" or 14).
     */
    public void set(StyleKey key, Object value) {
        properties.put(key, value);
    }

    /**
     * Retrieves a visual property value.
     *
     * @param key The attribute to fetch.
     * @return The raw object value, or null if not set.
     */
    public Object get(StyleKey key) {
        return properties.get(key);
    }

    /**
     * Retrieves a visual property safely cast to a String.
     *
     * @param key The attribute to fetch.
     * @return The string representation, or null if not set.
     */
    public String getString(StyleKey key) {
        Object val = get(key);
        return val != null ? String.valueOf(val) : null;
    }

    /**
     * Retrieves a visual property safely cast to an integer.
     *
     * @param key The attribute to fetch.
     * @return The integer value, or 0 if not set or not a number.
     */
    public int getInt(StyleKey key) {
        Object val = get(key);
        if (val instanceof Number n) {
            return n.intValue();
        }
        return 0;
    }
}