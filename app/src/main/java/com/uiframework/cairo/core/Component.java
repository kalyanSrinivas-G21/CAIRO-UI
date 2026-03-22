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

    // Split State flags for optimized rendering
    protected boolean selfDirty = true;
    protected boolean childDirty = true;
    protected boolean visible = true;
    protected boolean layoutValid = false;

    // Tree structure
    protected Component parent;

    /** Layout metadata describing how this component should be positioned. */
    private Constraints constraints = new Constraints();

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
     * @return True if this specific component's visual state has changed.
     */
    public boolean isSelfDirty() {
        return selfDirty;
    }

    /**
     * @return True if a descendant of this component's visual state has changed.
     */
    public boolean isChildDirty() {
        return childDirty;
    }

    /**
     * Clears both dirty flags, indicating the component and its subtree are fully updated.
     */
    public void clearDirty() {
        this.selfDirty = false;
        this.childDirty = false;
    }

    /**
     * Subscribes a listener to receive state change notifications from this component.
     *
     * @param listener The observer to add.
     */
    public void addObserver(StateChangeListener listener) {
        if (!observers.contains(listener)) {
            observers.add(listener);
        }
    }

    /**
     * Unsubscribes a listener from this component.
     *
     * @param listener The observer to remove.
     */
    public void removeObserver(StateChangeListener listener) {
        observers.remove(listener);
    }

    /**
     * Marks this component as dirty, requiring a redraw.
     * This propagates the child-dirty state up the component tree to the root and
     * notifies all registered observers.
     */
    public void markDirty() {
        this.selfDirty = true;

        for (StateChangeListener observer : observers) {
            observer.onStateChanged(this);
        }

        if (parent != null) {
            parent.markChildDirty();
        }
    }

    /**
     * Marks that a descendant of this component needs a repaint.
     * Short-circuits if already flagged to optimize deep tree traversal.
     */
    protected void markChildDirty() {
        if (this.childDirty) {
            return; // Optimization: Ancestors are already aware
        }

        this.childDirty = true;

        if (parent != null) {
            parent.markChildDirty();
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

    /**
     * Invalidates the current layout state of this component and propagates
     * the invalidation up to the root of the component tree.
     */
    public void invalidate() {
        this.layoutValid = false;
        if (parent != null) {
            parent.invalidate();
        }
    }

    /**
     * Subclasses must implement this to return their desired size
     * based on their internal content (e.g., text length or image dimensions).
     */
    public abstract Size getPreferredSize();

    /**
     * @return The layout constraints applied to this component.
     */
    public Constraints getConstraints() {
        return constraints;
    }

    /**
     * Updates the layout constraints for this component.
     * Calling this method automatically triggers a layout invalidation up the tree,
     * ensuring that the parent container re-evaluates the component's position.
     *
     * @param constraints The new constraints to apply.
     */
    public void setConstraints(Constraints constraints) {
        this.constraints = constraints;
        this.invalidate();
    }
}