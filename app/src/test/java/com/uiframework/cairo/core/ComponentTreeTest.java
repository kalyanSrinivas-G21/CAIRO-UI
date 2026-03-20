package com.uiframework.cairo.core;

import com.uiframework.cairo.components.Button;
import com.uiframework.cairo.components.Label;
import com.uiframework.cairo.components.Panel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ComponentTreeTest {

    @Test
    void addChildSetsParent() {
        Panel panel = new Panel(0, 0, 100, 100).withBackground("white");
        Label label = new Label(0, 0, 50, 20, "Test");

        panel.addChild(label);
        assertEquals(panel, label.parent);
    }

    @Test
    void removeChildClearsParent() {
        Panel panel = new Panel(0, 0, 100, 100).withBackground("white");
        Label label = new Label(0, 0, 50, 20, "Test");

        panel.addChild(label);
        panel.removeChild(label);
        assertNull(label.parent);
    }

    @Test
    void dirtyPropagatesUp() {
        Panel root = new Panel(0, 0, 500, 500).withBackground("gray");
        Panel inner = new Panel(10, 10, 200, 200).withBackground("white");
        Button button = new Button(5, 5, 80, 30, "Click");

        root.addChild(inner);
        inner.addChild(button);

        // Clear all initial dirty flags from instantiation
        root.clearDirty();
        inner.clearDirty();
        button.clearDirty();

        // Mutate the child button
        button.markDirty();

        // The button itself should be marked for repaint
        assertTrue(button.isSelfDirty(), "Button should be self-dirty");

        // The parents should ONLY flag that a child changed, avoiding a full repaint of themselves
        assertTrue(inner.isChildDirty(), "Inner panel should be child-dirty");
        assertFalse(inner.isSelfDirty(), "Inner panel should NOT be self-dirty");

        assertTrue(root.isChildDirty(), "Root panel should be child-dirty");
        assertFalse(root.isSelfDirty(), "Root panel should NOT be self-dirty");
    }

    @Test
    void absoluteCoordinates() {
        Panel panel = new Panel(100, 50, 300, 300).withBackground("white");
        Button button = new Button(20, 10, 100, 40, "Go");

        panel.addChild(button);

        assertEquals(120, button.getAbsoluteX());
        assertEquals(60, button.getAbsoluteY());
    }
}