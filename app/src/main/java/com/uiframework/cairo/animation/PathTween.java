package com.uiframework.cairo.animation;

import com.uiframework.cairo.core.Component;
import com.uiframework.cairo.util.Point;

/**
 * A specialized Tween that interpolates a component's position along a
 * Quadratic Bezier curve.
 */
public class PathTween extends Tween {

    private final Point start;
    private final Point control;
    private final Point end;
    private final Component target;

    /**
     * Constructs a new PathTween.
     *
     * @param target       The Component to move.
     * @param start        The starting origin point.
     * @param control      The Bezier control point that "pulls" the curve.
     * @param end          The final destination point.
     * @param duration     The duration of the animation in milliseconds.
     * @param interpolator The easing function to apply to the path traversal.
     */
    public PathTween(Component target, Point start, Point control, Point end, long duration, Interpolator interpolator) {
        // We leverage the parent Tween's time-tracking and easing math by interpolating
        // a pure fraction from 0.0 to 1.0. The callback then applies the Bezier math.
        super(0.0, 1.0, duration, interpolator, fraction -> {

            // Apply the Quadratic Bezier formula: B(t) = (1-t)^2 * P0 + 2(1-t)*t * P1 + t^2 * P2
            double invT = 1.0 - fraction;
            double invT2 = invT * invT;
            double t2 = fraction * fraction;

            double x = (invT2 * start.x) + (2 * invT * fraction * control.x) + (t2 * end.x);
            double y = (invT2 * start.y) + (2 * invT * fraction * control.y) + (t2 * end.y);

            // setBounds implicitly calls target.markDirty() and invalidates layout
            target.setBounds((int) x, (int) y, target.getWidth(), target.getHeight());
        });

        this.start = start;
        this.control = control;
        this.end = end;
        this.target = target;
    }

    /**
     * Steps the animation forward. Delegates to the core Tween engine to calculate
     * delta time, apply easing, and trigger our Bezier callback.
     *
     * @param deltaTime The time elapsed in milliseconds.
     * @return True if the animation is complete.
     */
    @Override
    public boolean update(long deltaTime) {
        return super.update(deltaTime);
    }
}