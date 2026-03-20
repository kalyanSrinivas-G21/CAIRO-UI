package com.uiframework.cairo.demo;

import com.uiframework.cairo.components.Button;
import com.uiframework.cairo.components.Label;
import com.uiframework.cairo.components.Panel;
import com.uiframework.cairo.components.TextField;
import com.uiframework.cairo.render.WebWindow;

/**
 * The main entry point for the CAIRO UI Framework demonstration.
 * Assembles a nested component tree to verify rendering, layout, and state logic.
 */
public class VisualDemo {

    public static void main(String[] args) {

        // 1. Create the Root Application Background
        Panel root = new Panel(0, 0, 900, 700)
                .withBackground("rgb(245, 245, 245)");

        // 2. Create a "Card" container using the fluent API
        Panel card = new Panel(50, 50, 350, 400)
                .withBackground("white")
                .withBorder("gray", 1)
                .withRadius(12);

        root.addChild(card);

        // 3. Add a Title Label (Relative to Card)
        Label title = new Label(20, 20, 310, 30, "Web UI Framework Demo");
        title.setFont("bold 20px sans-serif");
        card.addChild(title);

        // 4. Add a standard Button
        Button btnNormal = new Button(20, 80, 120, 40, "Click Me");
        card.addChild(btnNormal);

        // 5. Add a hovered Button to verify state-based color switching
        Button btnHovered = new Button(160, 80, 120, 40, "Hovered");
        btnHovered.setState(Button.State.HOVERED);
        card.addChild(btnHovered);

        // 6. Add a TextField
        TextField textField = new TextField(20, 150, 310, 40);
        // Populate initial text using the appendChar method we built
        String initialText = "Type something here...";
        for (char c : initialText.toCharArray()) {
            textField.appendChar(c);
        }
        card.addChild(textField);

        // 7. Initialize the Web Rendering Engine
        WebWindow window = new WebWindow("ui-canvas");
        window.setRoot(root);
        window.startRenderLoop();
    }
}