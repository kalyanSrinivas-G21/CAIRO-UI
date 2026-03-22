package com.uiframework.cairo.animation;

/**
 * A utility class containing static implementations of standard easing equations.
 * These stateless interpolators provide organic acceleration and deceleration
 * profiles for animations.
 */
public class Easing {

    /**
     * An easing function that accelerates until halfway, then decelerates.
     * Produces a smooth, symmetrical ease.
     */
    public static final Interpolator QUAD_IN_OUT = f ->
            f < 0.5 ? 2 * f * f : 1 - Math.pow(-2 * f + 2, 2) / 2;

    /**
     * An easing function that overshoots its target and snaps back like a rubber band.
     */
    public static final Interpolator ELASTIC_OUT = f -> {
        if (f == 0.0 || f == 1.0) {
            return f;
        }
        return Math.pow(2, -10 * f) * Math.sin((f * 10 - 0.75) * ((2 * Math.PI) / 3)) + 1;
    };

    /**
     * An easing function that mimics a ball dropping and bouncing on the floor.
     * Uses the standard 4-stage bounce threshold logic.
     */
    public static final Interpolator BOUNCE_OUT = f -> {
        double n1 = 7.5625;
        double d1 = 2.75;

        if (f < 1 / d1) {
            return n1 * f * f;
        } else if (f < 2 / d1) {
            f -= 1.5 / d1;
            return n1 * f * f + 0.75;
        } else if (f < 2.5 / d1) {
            f -= 2.25 / d1;
            return n1 * f * f + 0.9375;
        } else {
            f -= 2.625 / d1;
            return n1 * f * f + 0.984375;
        }
    };

    // Private constructor to prevent instantiation of a utility class
    private Easing() {}
}