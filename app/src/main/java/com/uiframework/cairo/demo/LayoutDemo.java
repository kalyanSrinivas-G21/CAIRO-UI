package com.uiframework.cairo.demo;

import com.uiframework.cairo.components.Button;
import com.uiframework.cairo.components.Panel;
import com.uiframework.cairo.core.Anchor;
import com.uiframework.cairo.core.Constraints;
import com.uiframework.cairo.layout.BorderLayout;
import com.uiframework.cairo.render.WebWindow;

/**
 * Demonstrates the BorderLayout by creating a classic application shell
 * with a header, sidebar, and a flexible main workspace.
 */
public class LayoutDemo {

    public static void main(String[] args) {
        WebWindow window = new WebWindow("ui-canvas");

        // 1. Root Panel with BorderLayout
        Panel root = new Panel(0, 0, 800, 600);
        root.withBackground("#f5f6fa");
        root.setLayoutManager(new BorderLayout(5, 5));

        // 2. Header (NORTH)
        Button header = new Button(0, 0, 0, 40, "CAIRO UI FRAMEWORK - HEADER");
        header.setConstraints(new Constraints(Anchor.NORTH, 0.0));
        root.addChild(header);

        // 3. Sidebar (WEST)
        Panel sidebar = new Panel(0, 0, 180, 0);
        sidebar.withBackground("#2f3640")
                .withBorder("#191e24", 1);
        sidebar.setConstraints(new Constraints(Anchor.WEST, 0.0));
        root.addChild(sidebar);

        // 4. Main Workspace (CENTER)
        Panel workspace = new Panel(0, 0, 0, 0);
        workspace.withBackground("#ffffff")
                .withBorder("#dcdde1", 1);
        workspace.setConstraints(new Constraints(Anchor.CENTER, 1.0));
        root.addChild(workspace);

        // 5. Footer (SOUTH)
        Button footer = new Button(0, 0, 0, 25, "Ready. v0.1.0-alpha");
        footer.setConstraints(new Constraints(Anchor.SOUTH, 0.0));
        root.addChild(footer);

        window.setRoot(root);
        window.startRenderLoop();
    }
}