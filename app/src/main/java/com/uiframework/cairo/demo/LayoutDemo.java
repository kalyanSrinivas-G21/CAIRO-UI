package com.uiframework.cairo.demo;

import com.uiframework.cairo.components.Button;
import com.uiframework.cairo.components.Panel;
import com.uiframework.cairo.layout.FlowLayout;
import com.uiframework.cairo.layout.VerticalLayout;
import com.uiframework.cairo.render.WebWindow;
import org.teavm.jso.browser.Window;

/**
 * Demonstrates the responsive Layout system using FlowLayout for a main content
 * area and VerticalLayout for a sidebar navigation.
 */
public class LayoutDemo {

    /**
     * Main entry point for the Layout Demo.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        // Initialize WebWindow
        WebWindow window = new WebWindow("ui-canvas");

        // 1. Root Panel (Acts as the full window)
        Panel root = new Panel(0, 0, 800, 600);
        root.withBackground("#f0f0f0");
        // Root uses FlowLayout to position the Sidebar and Main Content area
        root.setLayoutManager(new FlowLayout(0, 0));

        // 2. Sidebar Panel (Vertical Stacking)
        Panel sidebar = new Panel(0, 0, 200, 600);
        sidebar.withBackground("#2c3e50")
                .withBorder("#1a252f", 1);
        sidebar.setLayoutManager(new VerticalLayout(10, 10));

        // Add Sidebar Buttons of varying text lengths to verify uniform width
        sidebar.addChild(new Button(0, 0, 0, 0, "Home"));
        sidebar.addChild(new Button(0, 0, 0, 0, "Project Settings"));
        sidebar.addChild(new Button(0, 0, 0, 0, "Users"));
        sidebar.addChild(new Button(0, 0, 0, 0, "Help & Support"));

        // 3. Content Panel (Flow Layout)
        Panel content = new Panel(0, 0, 600, 600);
        content.withBackground("white");
        content.setLayoutManager(new FlowLayout(10, 10));

        for (int i = 1; i <= 10; i++) {
            content.addChild(new Button(0, 0, 0, 0, "Card " + i));
        }

        root.addChild(sidebar);
        root.addChild(content);

        // Responsive Simulation: Toggle root width every 2 seconds
        Window.current().setInterval(() -> {
            int currentWidth = root.getWidth();
            int newWidth = (currentWidth == 800) ? 500 : 800;

            // Adjusting the root bounds triggers the recursive validation cycle
            root.setBounds(0, 0, newWidth, 600);

            // Recalculate content width to occupy remaining space
            content.setBounds(0, 0, newWidth - 200, 600);

            System.out.println("Resizing root to: " + newWidth + "px");
        }, 2000);

        // Set root and start loop
        window.setRoot(root);
        window.startRenderLoop();
    }
}