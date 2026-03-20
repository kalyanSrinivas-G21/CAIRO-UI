package com.uiframework.cairo.demo;

import com.uiframework.cairo.components.Button;
import com.uiframework.cairo.components.Label;
import com.uiframework.cairo.components.Panel;
import com.uiframework.cairo.components.TextField;
import com.uiframework.cairo.render.WebWindow;

/**
 * A comprehensive stress-test for the CAIRO UI Framework.
 * Verifies deep nesting, relative coordinate resolution, clipping masks,
 * and state-based rendering across ~30 active components.
 */
public class Week3IntegrationDemo {

    public static void main(String[] args) {

        // 1. Root Window Panel (900x700, Dark Theme)
        Panel root = new Panel(0, 0, 900, 700).withBackground("#1e1e1e");

        // 2. Sidebar Panel (Nested in Root)
        Panel sidebar = new Panel(0, 0, 220, 700)
                .withBackground("#252526")
                .withBorder("#333333", 1);
        root.addChild(sidebar);

        // Sidebar Title
        Label sidebarTitle = new Label(20, 20, 180, 30, "CAIRO STUDIO");
        sidebarTitle.setFont("bold 18px sans-serif");
        sidebarTitle.setColor("#cccccc");
        sidebar.addChild(sidebarTitle);

        // Generate Sidebar Navigation using a loop
        for (int i = 0; i < 5; i++) {
            Button navBtn = new Button(20, 70 + (i * 50), 180, 35, "Menu Item " + (i + 1));
            sidebar.addChild(navBtn);
        }

        // 3. Main Content Panel (Nested in Root, offset by Sidebar)
        Panel mainContent = new Panel(220, 0, 680, 700).withBackground("#1e1e1e");
        root.addChild(mainContent);

        // 4. Inner Card 1: Dashboard Overview (Deeply Nested: Root -> Main -> Card 1)
        Panel card1 = new Panel(30, 30, 620, 150)
                .withBackground("#2d2d30")
                .withRadius(12)
                .withBorder("#007acc", 2);
        mainContent.addChild(card1);

        Label card1Title = new Label(20, 20, 300, 30, "System Status");
        card1Title.setFont("bold 24px sans-serif");
        card1Title.setColor("#ffffff");
        card1.addChild(card1Title);

        // 5. Visual State Verification (Normal, Hovered, Pressed)
        Button btnNormal = new Button(20, 80, 150, 40, "Status: Online");

        Button btnHovered = new Button(190, 80, 150, 40, "Hover Simulator");
        btnHovered.setState(Button.State.HOVERED);

        Button btnPressed = new Button(360, 80, 150, 40, "Pressed State");
        btnPressed.setState(Button.State.PRESSED);

        card1.addChild(btnNormal);
        card1.addChild(btnHovered);
        card1.addChild(btnPressed);

        // 6. Inner Card 2: Data Entry & Grid (Deeply Nested)
        Panel card2 = new Panel(30, 210, 620, 450)
                .withBackground("#252526")
                .withRadius(12)
                .withBorder("#333333", 1);
        mainContent.addChild(card2);

        Label card2Title = new Label(20, 20, 300, 30, "Data Entry Node");
        card2Title.setFont("16px monospace");
        card2Title.setColor("#4ec9b0");
        card2.addChild(card2Title);

        TextField inputField = new TextField(20, 60, 300, 40);
        String mockInput = "admin@cairo-ui.local";
        for (char c : mockInput.toCharArray()) {
            inputField.appendChar(c);
        }
        card2.addChild(inputField);

        // 7. Grid of Dummy Items to push component count over 25
        Label gridLabel = new Label(20, 130, 200, 20, "Recent Activity:");
        gridLabel.setFont("italic 14px sans-serif");
        gridLabel.setColor("#aaaaaa");
        card2.addChild(gridLabel);

        for (int i = 0; i < 12; i++) {
            // Calculate grid positions (3 columns)
            int col = i % 3;
            int row = i / 3;
            int itemX = 20 + (col * 190);
            int itemY = 160 + (row * 40);

            Label itemLabel = new Label(itemX, itemY, 170, 30, "Log Entry #" + (1040 + i));
            itemLabel.setFont("13px sans-serif");
            itemLabel.setColor("#dcdcaa");
            card2.addChild(itemLabel);
        }

        // 8. Fire up the Engine
        WebWindow window = new WebWindow("ui-canvas");
        window.setRoot(root);
        window.startRenderLoop();
    }
}