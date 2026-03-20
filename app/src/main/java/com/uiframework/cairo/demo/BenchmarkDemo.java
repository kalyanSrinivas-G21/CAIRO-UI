package com.uiframework.cairo.demo;

import com.uiframework.cairo.components.Button;
import com.uiframework.cairo.components.Panel;
import com.uiframework.cairo.render.RenderManager;
import com.uiframework.cairo.render.WebWindow;
import org.teavm.jso.browser.Window;

/**
 * A UI stress test designed to scientifically prove the efficiency of the
 * selective rendering engine. It generates an 800+ component tree and
 * automatically mutates a single node.
 */
public class BenchmarkDemo {

    public static void main(String[] args) {

        // Root Panel
        Panel root = new Panel(0, 0, 950, 950).withBackground("#121212");

        int rows = 20;
        int cols = 20;
        int cellWidth = 45;
        int cellHeight = 45;

        // We will store a reference to the center button to mutate it
        Button centerButton = null;

        // Generate the 20x20 grid
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Panel cell = new Panel(10 + (c * cellWidth), 10 + (r * cellHeight), cellWidth - 2, cellHeight - 2)
                        .withBackground("#1e1e1e")
                        .withBorder("#333333", 1)
                        .withRadius(4);

                Button btn = new Button(2, 2, cellWidth - 6, cellHeight - 6, "-");
                cell.addChild(btn);
                root.addChild(cell);

                // Capture the button roughly in the center (row 10, col 10)
                if (r == 10 && c == 10) {
                    centerButton = btn;
                }
            }
        }

        // Initialize the Engine
        WebWindow window = new WebWindow("ui-canvas");
        window.setRoot(root);
        window.startRenderLoop();

        // --- AUTOMATED MUTATION ---
        final Button targetBtn = centerButton;
        final boolean[] toggle = {false};

        // Every 500ms, change the state of exactly ONE button in the tree
        Window.setInterval(() -> {
            if (targetBtn != null) {
                toggle[0] = !toggle[0];
                if (toggle[0]) {
                    targetBtn.setState(Button.State.HOVERED);
                    targetBtn.setLabel("O");
                } else {
                    targetBtn.setState(Button.State.NORMAL);
                    targetBtn.setLabel("X");
                }
            }
        }, 500);

        // --- METRIC OUTPUT ---
        // Log the telemetry to the browser console every 1 second
        Window.setInterval(() -> {
            System.out.println("Paint calls last frame: " + RenderManager.lastFramePaintCount);
        }, 1000);
    }
}