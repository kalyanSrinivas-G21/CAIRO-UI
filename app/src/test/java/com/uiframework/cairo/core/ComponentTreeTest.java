package com.uiframework.cairo.core;

import com.uiframework.cairo.components.Button;
import com.uiframework.cairo.components.Label;
import com.uiframework.cairo.components.Panel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Structural validation suite for the CAIRO Component Tree.
 * Verifies hierarchy management, dirty state propagation, and coordinate resolution.
 */
class ComponentTreeTest {

    @Test
    void addChildSetsParent() {
        Panel panel = new Panel(0, 0, 100, 100, "white");
        Label label = new Label(0, 0, 50, 20, "Test");

        panel.addChild(label);

        // We need to add a getParent() to Component.java or access the protected field
        // Since we are in the same package 'core', we can access the protected 'parent' field.
        assertEquals(panel, label.parent, "Adding a child must set its parent reference.");
    }

    @Test
    void removeChildClearsParent() {
        Panel panel = new Panel(0, 0, 100, 100, "white");
        Label label = new Label(0, 0, 50, 20, "Test");

        panel.addChild(label);
        panel.removeChild(label);

        assertNull(label.parent, "Removing a child must nullify its parent reference to prevent ghost updates.");
    }

    @Test
    void dirtyPropagatesUp() {
        // Build 3-level tree: Root -> Inner -> Button
        Panel root = new Panel(0, 0, 500, 500, "gray");
        Panel inner = new Panel(10, 10, 200, 200, "white");
        Button button = new Button(5, 5, 80, 30, "Click");

        root.addChild(inner);
        inner.addChild(button);

        // Reset state: clearDirty() is only implemented in Component.java
        // We must visit the tree or manually clear them for this test
        root.clearDirty();
        inner.clearDirty();
        button.clearDirty();

        assertFalse(button.isDirty());
        assertFalse(root.isDirty());

        // Trigger dirty state at the leaf
        button.markDirty();

        // Verify propagation
        assertTrue(button.isDirty(), "Leaf should be dirty.");
        assertTrue(inner.isDirty(), "Immediate parent should be dirty.");
        assertTrue(root.isDirty(), "Root should be dirty.");
    }

    @Test
    void absoluteCoordinates() {
        // Place Panel at (100, 50)
        Panel panel = new Panel(100, 50, 300, 300, "white");

        // Place Button at (20, 10) inside that Panel
        Button button = new Button(20, 10, 100, 40, "Go");

        panel.addChild(button);

        // Assert button.getAbsoluteX() is 100 + 20 = 120
        assertEquals(120, button.getAbsoluteX(), "Absolute X should be the sum of relative X and parent's absolute X.");
        // Assert button.getAbsoluteY() is 50 + 10 = 60
        assertEquals(60, button.getAbsoluteY(), "Absolute Y should be the sum of relative Y and parent's absolute Y.");
    }
}