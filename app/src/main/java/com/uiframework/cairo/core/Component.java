package com.uiframework.cairo.core;

import java.util.ArrayList;
import java.util.List;
import org.teavm.jso.canvas.CanvasRenderingContext2D;

/**
 * The abstract base class for all UI elements in the CAIRO framework.
 * Defines spatial properties, the composite tree structure, state management,
 * and the core rendering contract.
 */
public abstract class Component {

    // Spatial fields (relative to parent)
    protected int x;
    protected int y;
    protected int width;
    protected int height;

    // State flags
    protected boolean dirty = true;
    protected boolean visible = true;

    // Tree structure
    protected Component parent;

    // Observers
    protected List<StateChangeListener> observers = new ArrayList<>();

    /**
     * @return The local X coordinate relative to the parent container.
     */
    public int getX() {
        return x;
    }

    /**
     * @return The local Y coordinate relative to the parent container.
     */
    public int getY() {
        return y;
    }

    /**
     * @return The width of the component in pixels.
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return The height of the component in pixels.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets the local bounds of this component and marks it as dirty.
     *
     * @param x      The new local X coordinate.
     * @param y      The new local Y coordinate.
     * @param width  The new width.
     * @param height The new height.
     */
    public void setBounds(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        markDirty();
    }

    /**
     * @return True if the component needs to be redrawn or recalculated.
     */
    public boolean isDirty() {
        return dirty;
    }

    /**
     * Clears the dirty flag, indicating the component is fully updated and rendered.
     */
    public void clearDirty() {
        this.dirty = false;
    }

    /**
     * Marks this component as dirty, requiring a redraw.
     * This propagates the dirty state up the component tree to the root and
     * notifies all registered observers.
     */
    public void markDirty() {
        this.dirty = true;

        if (parent != null) {
            parent.markDirty();
        }

        for (StateChangeListener observer : observers) {
            observer.onStateChanged(this);
        }
    }

    /**
     * Recursively calculates the absolute X coordinate on the canvas by
     * adding this component's local X to its parent's absolute X.
     *
     * @return The absolute X coordinate on the canvas.
     */
    public int getAbsoluteX() {
        if (parent == null) {
            return x;
        }
        return x + parent.getAbsoluteX();
    }

    /**
     * Recursively calculates the absolute Y coordinate on the canvas by
     * adding this component's local Y to its parent's absolute Y.
     *
     * @return The absolute Y coordinate on the canvas.
     */
    public int getAbsoluteY() {
        if (parent == null) {
            return y;
        }
        return y + parent.getAbsoluteY();
    }

    /**
     * The core rendering contract. All subclasses must implement their specific
     * rendering logic using the provided 2D canvas context.
     *
     * @param ctx The HTML5 Canvas 2D rendering context.
     */
    public abstract void paint(CanvasRenderingContext2D ctx);

    /**
     * @return true if the component is currently flagged to be drawn, false otherwise.
     */
    public boolean isVisible() {
        return visible;
    }
}