package com.uiframework.cairo.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An abstract Component that can contain other components.
 * Manages the N-ary tree structure for the Composite design pattern
 * and coordinates with a LayoutManager for automated positioning.
 */
public abstract class Container extends Component {

    private final List<Component> children = new ArrayList<>();
    private LayoutManager layoutManager;

    /**
     * Adds a child component to this container, establishing the parent-child relationship.
     *
     * @param child The component to add.
     * @throws IllegalArgumentException if the child is null.
     */
    public void addChild(Component child) {
        if (child == null) {
            throw new IllegalArgumentException("Cannot add a null child to a Container.");
        }
        child.parent = this;
        children.add(child);

        // A structural change requires both a new layout pass and a repaint
        invalidate();
        markDirty();
    }

    /**
     * Removes a child component from this container.
     * Crucially severs the parent reference to prevent ghost dirty-state propagation.
     *
     * @param child The component to remove.
     */
    public void removeChild(Component child) {
        if (children.remove(child)) {
            child.parent = null;
            invalidate();
            markDirty();
        }
    }

    /**
     * Returns an unmodifiable view of the children to prevent external mutation
     * of the component tree without triggering markDirty().
     *
     * @return A read-only list of child components.
     */
    public List<Component> getChildren() {
        return Collections.unmodifiableList(children);
    }

    /**
     * @return The number of direct children inside this container.
     */
    public int getChildCount() {
        return children.size();
    }

    /**
     * Sets the strategy used to position children within this container.
     */
    public void setLayoutManager(LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        invalidate();
    }

    public LayoutManager getLayoutManager() {
        return layoutManager;
    }

    /**
     * Calculates the ideal size for this container.
     * If a LayoutManager is present, it negotiates the size based on children;
     * otherwise, it defaults to the current dimensions.
     */
    @Override
    public Size getPreferredSize() {
        if (layoutManager != null) {
            return new Size(
                    layoutManager.getPreferredWidth(this),
                    layoutManager.getPreferredHeight(this)
            );
        }
        return new Size(width, height);
    }

    @Override
    public void invalidate() {
        // First, invalidate our own layout state
        this.layoutValid = false;

        // Then, propagate to our parent so the entire branch knows it is "dirty"
        // from a layout perspective.
        if (parent != null) {
            parent.invalidate();
        }
    }
}