package com.uiframework.cairo.core;

import com.uiframework.cairo.components.Label;
import com.uiframework.cairo.components.Panel;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests verifying the structural integrity of the N-ary tree
 * traversal algorithms.
 */
class TreeTraversalTest {

    @Test
    void testDFSPreOrderTraversal() {
        // Arrange: Construct the N-ary tree
        // Root Panel
        Panel root = new Panel(0, 0, 800, 600, "#FFFFFF");

        // Root's first child
        Label label1 = new Label("Title Label");

        // Root's second child (Inner Panel)
        Panel innerPanel = new Panel(10, 10, 200, 200, "#EEEEEE");

        // Inner Panel's child
        Label label2 = new Label("Inner Content");

        // Assemble the hierarchy
        root.addChild(label1);
        root.addChild(innerPanel);
        innerPanel.addChild(label2);

        List<String> visitedOrder = new ArrayList<>();

        // Act: Traverse the tree using Pre-order DFS
        TreeTraversal.visitPreOrder(root, component -> {
            visitedOrder.add(component.getClass().getSimpleName());
        });

        // Assert: Verify the exact traversal sequence
        List<String> expectedOrder = List.of("Panel", "Label", "Panel", "Label");
        assertEquals(expectedOrder, visitedOrder,
                "Pre-order traversal must visit parents before their children in structural order.");
    }
}