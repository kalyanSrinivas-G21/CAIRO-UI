package com.uiframework.cairo.core;

/**
 * Metadata container that describes how a Component should be handled by
 * its parent's LayoutManager. This allows for per-component behavior
 * overrides within a single layout strategy.
 */
public class Constraints {

    /** The anchoring preference for the component. Defaults to CENTER. */
    public Anchor anchor = Anchor.CENTER;

    /** * The expansion weight or "flex" value.
     * Typically used by layouts to distribute extra space. Defaults to 0.0.
     */
    public double weight = 0.0;

    /**
     * Constructs a new Constraints object with default values.
     */
    public Constraints() {
    }

    /**
     * Convenience constructor to initialize both anchor and weight.
     *
     * @param anchor The desired Anchor direction.
     * @param weight The desired expansion weight.
     */
    public Constraints(Anchor anchor, double weight) {
        this.anchor = anchor;
        this.weight = weight;
    }
}