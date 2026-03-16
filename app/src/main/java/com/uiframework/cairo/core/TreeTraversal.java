package com.uiframework.cairo.core;

import java.util.function.Consumer;

/**
 * Utility class providing Depth-First Search (DFS) traversal algorithms
 * for the CAIRO N-ary component tree.
 */
public final class TreeTraversal {

    // Prevent instantiation of utility class
    private TreeTraversal() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Executes a Pre-order DFS traversal (Root, then Children).
     * Used primarily for rendering (drawing parents before children) and capturing
     * absolute layouts in a top-down manner.
     *
     * @param root    The starting component node.
     * @param visitor The operation to perform on each component.
     */
    public static void visitPreOrder(Component root, Consumer<Component> visitor) {
        if (root == null) {
            return;
        }

        // 1. Visit the current node first
        visitor.accept(root);

        // 2. If it is a Container, recursively visit all children
        if (root instanceof Container container) {
            for (Component child : container.getChildren()) {
                visitPreOrder(child, visitor);
            }
        }
    }

    /**
     * Executes a Post-order DFS traversal (Children, then Root).
     * Used primarily for layout calculations where a parent's size depends on
     * the fully calculated bounds of its children.
     *
     * @param root    The starting component node.
     * @param visitor The operation to perform on each component.
     */
    public static void visitPostOrder(Component root, Consumer<Component> visitor) {
        if (root == null) {
            return;
        }

        // 1. If it is a Container, recursively visit all children first
        if (root instanceof Container container) {
            for (Component child : container.getChildren()) {
                visitPostOrder(child, visitor);
            }
        }

        // 2. Visit the current node last
        visitor.accept(root);
    }
}