package com.uiframework.cairo.demo;

import com.uiframework.cairo.components.Button;
import com.uiframework.cairo.components.Label;
import com.uiframework.cairo.components.Panel;
import com.uiframework.cairo.components.TextField;
import com.uiframework.cairo.render.WebWindow;

/**
 * A comprehensive stress-test for the CAIRO UI Framework.
 * Verifies deep nesting, relative coordinate resolution, clipping masks,
 * and state-based rendering across a complex component tree.
 */
public class Week3IntegrationDemo {

    public static void main(String[] args) {

        // 1. Root Window Panel (900x700) - Panel 1
        Panel root = new Panel(0, 0, 900, 700)
                .withBackground("rgb(245, 245, 245)");

        // 2. Main Card Container nested in Root - Panel 2
        Panel card = new Panel(50, 50, 400, 500)
                .withBackground("white")
                .withBorder("#cccccc", 1)
                .withRadius(12);
        root.addChild(card);

        // 3. Header Container nested in Card - Panel 3
        Panel headerPanel = new Panel(20, 20, 360, 50)
                .withBackground("#eef2f5")
                .withRadius(6);
        card.addChild(headerPanel);

        // 4. Visual Divider nested in Card - Panel 4
        Panel divider = new Panel(20, 90, 360, 2)
                .withBackground("#dddddd");
        card.addChild(divider);

        // 5. Footer Container nested in Card - Panel 5
        Panel footerPanel = new Panel(20, 420, 360, 60)
                .withBackground("#fafafa")
                .withBorder("#eeeeee", 1)
                .withRadius(6);
        card.addChild(footerPanel);

        // --- Distribute Labels ---

        // Label 1: Inside Header
        Label titleLabel = new Label(15, 15, 330, 20, "Week 3 Integration Demo");
        titleLabel.setFont("bold 18px Arial");
        titleLabel.setColor("#333333");
        headerPanel.addChild(titleLabel);

        // Label 2: Inside Card Body
        Label descriptionLabel = new Label(20, 110, 360, 20, "Testing recursive layout & clipping regions.");
        descriptionLabel.setFont("italic 14px sans-serif");
        descriptionLabel.setColor("#666666");
        card.addChild(descriptionLabel);

        // Label 3: Inside Card Body (Input prompt)
        Label inputPrompt = new Label(20, 160, 360, 20, "Enter connection string:");
        inputPrompt.setFont("bold 14px Arial");
        card.addChild(inputPrompt);

        // Label 4: Inside Footer
        Label statusLabel = new Label(15, 20, 330, 20, "Status: Engine rendering at 60 FPS.");
        statusLabel.setFont("12px monospace");
        statusLabel.setColor("#007BFF");
        footerPanel.addChild(statusLabel);

        // --- Add TextField ---
        TextField textField = new TextField(20, 190, 360, 40);
        String mockInput = "wss://cairo-engine.local:8080";
        for (char c : mockInput.toCharArray()) {
            textField.appendChar(c);
        }
        card.addChild(textField);

        // --- Add Buttons (Normal, Hovered, Pressed) ---

        Button btnNormal = new Button(20, 260, 110, 40, "Connect");
        card.addChild(btnNormal);

        Button btnHovered = new Button(145, 260, 110, 40, "Settings");
        btnHovered.setState(Button.State.HOVERED);
        card.addChild(btnHovered);

        Button btnPressed = new Button(270, 260, 110, 40, "Disconnect");
        btnPressed.setState(Button.State.PRESSED);
        card.addChild(btnPressed);

        // --- Execution ---

        WebWindow window = new WebWindow("ui-canvas");
        window.setRoot(root);
        window.startRenderLoop();
    }
}