package com.uiframework.cairo.core;

import com.uiframework.cairo.components.Label;
import com.uiframework.cairo.components.Panel;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TreeTraversalTest {

    @Test
    void testDFSPreOrderTraversal() {
        // Updated to use 4-arg constructor + fluent background
        Panel root = new Panel(0, 0, 800, 600).withBackground("#FFFFFF");

        Label label1 = new Label(5, 5, 100, 20, "Title Label");

        Panel innerPanel = new Panel(10, 40, 200, 200).withBackground("#EEEEEE");

        Label label2 = new Label(0, 0, 150, 20, "Inner Content");

        root.addChild(label1);
        root.addChild(innerPanel);
        innerPanel.addChild(label2);

        List<String> visitedOrder = new ArrayList<>();

        TreeTraversal.visitPreOrder(root, component -> {
            visitedOrder.add(component.getClass().getSimpleName());
        });

        List<String> expectedOrder = List.of("Panel", "Label", "Panel", "Label");
        assertEquals(expectedOrder, visitedOrder);
    }
}