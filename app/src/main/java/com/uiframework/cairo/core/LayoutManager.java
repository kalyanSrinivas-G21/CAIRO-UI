package com.uiframework.cairo.core;

/**
 * Interface defining the contract for automatic component positioning and sizing.
 */
public interface LayoutManager {

    /**
     * Executes the positioning logic for all children within the given container.
     */
    void layout(Container parent);

    /**
     * Calculates the ideal width required by the container based on its children
     * and the layout's specific rules.
     */
    int getPreferredWidth(Container parent);

    /**
     * Calculates the ideal height required by the container based on its children.
     */
    int getPreferredHeight(Container parent);
}