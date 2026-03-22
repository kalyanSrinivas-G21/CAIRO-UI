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
     *
     * @param layoutManager The new LayoutManager to use.
     */
    public void setLayoutManager(LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        invalidate();
    }

    /**
     * @return The current LayoutManager assigned to this container.
     */
    public LayoutManager getLayoutManager() {
        return layoutManager;
    }

    /**
     * Executes the layout logic if the current layout is flagged as invalid.
     * Recursively validates all child containers to ensure the entire tree
     * is correctly positioned before rendering.
     */
    public void validate() {
        if (layoutValid) {
            return;
        }

        if (layoutManager != null) {
            layoutManager.layout(this);
        }

        this.layoutValid = true;

        for (Component child : children) {
            if (child instanceof Container container) {
                container.validate();
            }
        }
    }

    /**
     * Calculates the ideal size for this container.
     * If a LayoutManager is present, it negotiates the size based on children;
     * otherwise, it defaults to the current dimensions.
     *
     * @return The preferred Size of the container.
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

    /**
     * Invalidates the current layout state of this component and propagates
     * the invalidation up to the root of the component tree.
     */
    @Override
    public void invalidate() {
        this.layoutValid = false;
        if (parent != null) {
            parent.invalidate();
        }
    }

    /**
     * Performs a recursive hit-test to find the deepest visible child component
     * that contains the given screen coordinates.
     *
     * @param screenX The absolute X coordinate to test.
     * @param screenY The absolute Y coordinate to test.
     * @return The deepest Component at the coordinates, or null if none is hit.
     */
    public Component getComponentAt(int screenX, int screenY) {
        // If the coordinate isn't within this container at all, abort early.
        if (!isVisible() || !contains(screenX, screenY)) {
            return null;
        }

        // Iterate through children in reverse order (top-most visual layer first).
        for (int i = children.size() - 1; i >= 0; i--) {
            Component child = children.get(i);

            if (!child.isVisible()) {
                continue;
            }

            if (child instanceof Container container) {
                // Recursively search deeper into nested containers
                Component hit = container.getComponentAt(screenX, screenY);
                if (hit != null) {
                    return hit; // Found a leaf deep inside
                }
            } else if (child.contains(screenX, screenY)) {
                return child; // Found a direct leaf child (like a Button)
            }
        }

        // The point is inside this container, but not over any of its children
        return this;
    }
}