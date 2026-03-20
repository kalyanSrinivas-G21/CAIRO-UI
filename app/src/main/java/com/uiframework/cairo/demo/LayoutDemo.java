package com.uiframework.cairo.demo;

import com.uiframework.cairo.components.Button;
import com.uiframework.cairo.components.Panel;
import com.uiframework.cairo.layout.FlowLayout;
import com.uiframework.cairo.render.WebWindow;
import org.teavm.jso.browser.Window;

/**
 * Demonstrates the responsive FlowLayout and validation system.
 * The UI automatically re-flows its components when the root width changes.
 */
public class LayoutDemo {

    /**
     * Main entry point for the Layout Demo.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        // Initialize WebWindow
        WebWindow window = new WebWindow("ui-canvas");

        // Create root Panel with FlowLayout
        Panel root = new Panel(0, 0, 800, 600);
        root.withBackground("#f0f0f0");
        root.setLayoutManager(new FlowLayout(10, 10));

        // Add 15 buttons without manual bounds
        for (int i = 1; i <= 15; i++) {
            Button btn = new Button(0, 0, 0, 0, "Button " + i);
            root.addChild(btn);
        }

        // Responsive Simulation: Toggle width every 2 seconds
        Window.current().setInterval(() -> {
            int currentWidth = root.getWidth();
            int newWidth = (currentWidth == 800) ? 400 : 800;

            // Setting bounds marks the component as dirty and invalidates layout
            root.setBounds(0, 0, newWidth, 600);

            // Explicitly signal that the layout needs re-flow
            root.invalidate();

            System.out.println("Resizing root to: " + newWidth + "px");
        }, 2000);

        // Set root and start loop
        window.setRoot(root);
        window.startRenderLoop();
    }
}