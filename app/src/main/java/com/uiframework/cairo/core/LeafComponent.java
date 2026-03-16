package com.uiframework.cairo.core;

/**
 * An abstract marker class representing terminal UI nodes in the Composite tree.
 * Leaf components handle specific rendering tasks (like drawing text or images)
 * but cannot contain or manage other components.
 */
public abstract class LeafComponent extends Component {
    // Abstract marker class: intentionally contains no child list logic.
}