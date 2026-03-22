package com.uiframework.cairo.core;

/**
 * Defines the cardinal and ordinal directions for component anchoring.
 * LayoutManagers use these values to determine where a component should
 * stick or align within its allocated layout cell.
 */
public enum Anchor {
    /** Anchor to the top center. */
    NORTH,
    /** Anchor to the bottom center. */
    SOUTH,
    /** Anchor to the middle right. */
    EAST,
    /** Anchor to the middle left. */
    WEST,
    /** Anchor to the dead center. */
    CENTER,
    /** Anchor to the top-left corner. */
    TOP_LEFT,
    /** Anchor to the top-right corner. */
    TOP_RIGHT,
    /** Anchor to the bottom-left corner. */
    BOTTOM_LEFT,
    /** Anchor to the bottom-right corner. */
    BOTTOM_RIGHT
}