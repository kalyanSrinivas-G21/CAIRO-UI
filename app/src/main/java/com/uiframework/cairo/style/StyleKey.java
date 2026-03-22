package com.uiframework.cairo.style;

/**
 * An enumeration defining all possible stylable properties within the CAIRO framework.
 * Used as strongly-typed keys for the Style maps.
 */
public enum StyleKey {
    /** The background color or fill. */
    BACKGROUND,

    /** The foreground color, typically used for text or icons. */
    FOREGROUND,

    /** The color of the component's border stroke. */
    BORDER_COLOR,

    /** The thickness of the component's border in pixels. */
    BORDER_WIDTH,

    /** The radius for rounded corners in pixels. */
    CORNER_RADIUS,

    /** The size of the font in pixels. */
    FONT_SIZE,

    /** The CSS font-family string. */
    FONT_FAMILY
}