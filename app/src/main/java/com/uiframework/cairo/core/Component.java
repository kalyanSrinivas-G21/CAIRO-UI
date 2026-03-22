package com.uiframework.cairo.core;

import com.uiframework.cairo.event.UIEvent;
import com.uiframework.cairo.event.UIEventListener;
import java.util.ArrayList;
import java.util.List;
import org.teavm.jso.canvas.CanvasRenderingContext2D;

/**
 * The abstract base class for all UI elements in the CAIRO framework.
 * Defines spatial properties, the composite tree structure, state management,
 * layout constraints, hit testing, focus handling, and the core event broadcasting mechanism.
 */
public abstract class Component {

    protected int x;
    protected int y;
    protected int width;
    protected int height;

    protected boolean selfDirty = true;
    protected boolean childDirty = true;
    protected boolean visible = true;
    protected boolean layoutValid = false;
    protected boolean focusable = false;

    protected Component parent;
    private Constraints constraints = new Constraints();

    protected List<StateChangeListener> observers = new ArrayList<>();
    private List<UIEventListener> eventListeners = new ArrayList<>();

    /**
     * @return The local X coordinate relative to the parent container.
     */
    public int getX() { return x; }

    /**
     * @return The local Y coordinate relative to the parent container.
     */
    public int getY() { return y; }

    /**
     * @return The width of the component in pixels.
     */
    public int getWidth() { return width; }

    /**
     * @return The height of the component in pixels.
     */
    public int getHeight() { return height; }

    /**
     * Sets the local bounds of this component and marks it as dirty.
     * Triggers layout invalidation and visual repainting.
     *
     * @param x      The new local X coordinate.
     * @param y      The new local Y coordinate.
     * @param width  The new width.
     * @param height The new height.
     */
    public void setBounds(int x, int y, int width, int height) {
        if (this.x == x && this.y == y && this.width == width && this.height == height) {
            return;
        }
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        invalidate();
        markDirty();
    }

    /**
     * Checks if the specified absolute screen coordinates fall within
     * the bounds of this component.
     *
     * @param screenX The absolute X coordinate to test.
     * @param screenY The absolute Y coordinate to test.
     * @return True if the point is inside the component, false otherwise.
     */
    public boolean contains(int screenX, int screenY) {
        if (!visible) return false;
        int absX = getAbsoluteX();
        int absY = getAbsoluteY();
        return screenX >= absX && screenX <= absX + width &&
                screenY >= absY && screenY <= absY + height;
    }

    /**
     * @return True if this specific component's visual state has changed.
     */
    public boolean isSelfDirty() { return selfDirty; }

    /**
     * @return True if a descendant of this component's visual state has changed.
     */
    public boolean isChildDirty() { return childDirty; }

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
        if (!observers.contains(listener)) observers.add(listener);
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
     * Registers an event listener to respond to interactions on this component.
     *
     * @param listener The UIEventListener to attach.
     */
    public void addEventListener(UIEventListener listener) {
        if (!eventListeners.contains(listener)) {
            eventListeners.add(listener);
        }
    }

    /**
     * Removes a previously registered event listener.
     *
     * @param listener The UIEventListener to detach.
     */
    public void removeEventListener(UIEventListener listener) {
        eventListeners.remove(listener);
    }

    /**
     * Broadcasts a UI event to all registered event listeners.
     *
     * @param event The event object containing interaction details.
     */
    public void fireEvent(UIEvent event) {
        for (UIEventListener listener : eventListeners) {
            listener.handleEvent(event);
        }
    }

    /**
     * Marks this component as dirty, requiring a redraw.
     * Propagates the child-dirty state up the component tree.
     */
    public void markDirty() {
        this.selfDirty = true;
        for (StateChangeListener observer : observers) {
            observer.onStateChanged(this);
        }
        if (parent != null) parent.markChildDirty();
    }

    /**
     * Marks that a descendant of this component needs a repaint.
     */
    protected void markChildDirty() {
        if (this.childDirty) return;
        this.childDirty = true;
        if (parent != null) parent.markChildDirty();
    }

    /**
     * Calculates the absolute X coordinate on the canvas.
     *
     * @return The absolute X coordinate.
     */
    public int getAbsoluteX() {
        return (parent == null) ? x : x + parent.getAbsoluteX();
    }

    /**
     * Calculates the absolute Y coordinate on the canvas.
     *
     * @return The absolute Y coordinate.
     */
    public int getAbsoluteY() {
        return (parent == null) ? y : y + parent.getAbsoluteY();
    }

    /**
     * The core rendering contract. Subclasses must implement specific rendering logic.
     *
     * @param ctx The HTML5 Canvas 2D rendering context.
     */
    public abstract void paint(CanvasRenderingContext2D ctx);

    /**
     * @return true if the component is currently flagged to be drawn.
     */
    public boolean isVisible() { return visible; }

    /**
     * Invalidates the current layout state of this component and its ancestors.
     */
    public void invalidate() {
        this.layoutValid = false;
        if (parent != null) parent.invalidate();
    }

    /**
     * Subclasses must implement this to return their desired size based on content.
     *
     * @return The preferred Size of the component.
     */
    public abstract Size getPreferredSize();

    /**
     * @return The layout constraints applied to this component.
     */
    public Constraints getConstraints() { return constraints; }

    /**
     * Updates the layout constraints for this component and triggers invalidation.
     *
     * @param constraints The new constraints to apply.
     */
    public void setConstraints(Constraints constraints) {
        this.constraints = constraints;
        this.invalidate();
    }

    /**
     * @return True if this component is allowed to receive keyboard focus.
     */
    public boolean isFocusable() {
        return focusable;
    }

    /**
     * Sets whether this component can receive keyboard focus.
     *
     * @param focusable True to allow focus, false to deny.
     */
    public void setFocusable(boolean focusable) {
        this.focusable = focusable;
    }

    /**
     * Requests the FocusManager to assign the global keyboard focus to this component.
     */
    public void requestFocus() {
        FocusManager.setFocus(this);
    }
}