package com.uiframework.cairo.demo;

import com.uiframework.cairo.animation.AnimationManager;
import com.uiframework.cairo.animation.Easing;
import com.uiframework.cairo.animation.PathTween;
import com.uiframework.cairo.components.Button;
import com.uiframework.cairo.components.Label;
import com.uiframework.cairo.components.Panel;
import com.uiframework.cairo.core.Anchor;
import com.uiframework.cairo.core.Constraints;
import com.uiframework.cairo.layout.BorderLayout;
import com.uiframework.cairo.render.WebWindow;
import com.uiframework.cairo.util.Point;

/**
 * Demonstrates the organic animation engine by triggering a Toast notification
 * that travels along a Bezier path using an Elastic-Out easing profile.
 */
public class ToastDemo {

    public static void main(String[] args) {
        WebWindow window = new WebWindow("ui-canvas");

        Panel root = new Panel(0, 0, 800, 600);
        root.withBackground("#e2e8f0");
        root.setLayoutManager(null); // Use absolute positioning for the overlay

        Button triggerBtn = new Button(325, 250, 150, 40, "Show Toast");
        root.addChild(triggerBtn);

        // Create the Toast notification off-screen
        Panel toast = new Panel(250, 650, 300, 60);
        toast.withBackground("#2d3436").withRadius(8);
        toast.setLayoutManager(new BorderLayout(0, 0));

        Label toastLabel = new Label(0, 0, 0, 0, "Saved successfully!");
        toastLabel.setColor("white");
        toastLabel.setConstraints(new Constraints(Anchor.CENTER, 1.0));
        toast.addChild(toastLabel);

        toast.setVisible(false);
        root.addChild(toast);

        triggerBtn.setOnClick(() -> {
            toast.setVisible(true);

            // Define the Quadratic Bezier curve coordinates
            Point start = new Point(250, 650);     // Bottom off-screen
            Point control = new Point(250, 200);   // Anchor pulling it swiftly up
            Point end = new Point(250, 450);       // Final resting spot near bottom

            // Create a 1.2 second elastic path animation
            PathTween slideIn = new PathTween(toast, start, control, end, 1200, Easing.ELASTIC_OUT);
            AnimationManager.getInstance().add(slideIn);
        });

        window.setRoot(root);
        window.startRenderLoop();
    }
}