package com.uiframework.cairo.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An abstract Component that can contain other components.
 * Manages the N-ary tree structure for the Composite design pattern.
 */
public abstract class Container extends Component {

    private final List<Component> children = new ArrayList<>();

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
        markDirty(); // The tree has changed, require a re-layout/re-render
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
}