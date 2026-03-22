package com.uiframework.cairo.style;

import com.uiframework.cairo.components.Button;
import com.uiframework.cairo.components.Label;
import com.uiframework.cairo.components.Panel;
import com.uiframework.cairo.core.Component;
import java.util.HashMap;
import java.util.Map;

/**
 * A global registry that acts as a singleton to store and provide default styles
 * for every component type. Implements the Flyweight pattern for memory efficiency.
 */
public class Skin {

    private static final Map<Class<? extends Component>, Style> defaultStyles = new HashMap<>();

    // Static initializer establishing the "Light Mode" baseline for the framework
    static {
        Style panelStyle = new Style();
        // Panels default to transparent unless explicitly styled
        panelStyle.set(StyleKey.BACKGROUND, null);
        panelStyle.set(StyleKey.BORDER_COLOR, null);
        panelStyle.set(StyleKey.BORDER_WIDTH, 0);
        panelStyle.set(StyleKey.CORNER_RADIUS, 0);
        setDefaults(Panel.class, panelStyle);

        Style buttonStyle = new Style();
        buttonStyle.set(StyleKey.BACKGROUND, "#007BFF");
        buttonStyle.set(StyleKey.FOREGROUND, "white");
        buttonStyle.set(StyleKey.FONT_SIZE, 14);
        buttonStyle.set(StyleKey.FONT_FAMILY, "sans-serif");
        buttonStyle.set(StyleKey.CORNER_RADIUS, 8);
        setDefaults(Button.class, buttonStyle);

        Style labelStyle = new Style();
        labelStyle.set(StyleKey.FOREGROUND, "#333333");
        labelStyle.set(StyleKey.FONT_SIZE, 14);
        labelStyle.set(StyleKey.FONT_FAMILY, "sans-serif");
        setDefaults(Label.class, labelStyle);
    }

    /**
     * Registers a baseline style for a specific component class.
     *
     * @param clazz The component class type.
     * @param style The baseline style to apply.
     */
    public static void setDefaults(Class<? extends Component> clazz, Style style) {
        defaultStyles.put(clazz, style);
    }

    /**
     * Retrieves the baseline style for a specific component class.
     *
     * @param clazz The component class type.
     * @return The default style, or null if none registered.
     */
    public static Style getDefaults(Class<? extends Component> clazz) {
        return defaultStyles.get(clazz);
    }
}