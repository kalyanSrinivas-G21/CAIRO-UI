package com.uiframework.cairo.animation;

import java.util.function.Consumer;

/**
 * Represents a single time-based interpolation task.
 * Transitions a value from a start point to an end point over a specified duration.
 */
public class Tween {

    private final double startValue;
    private final double endValue;
    private final long duration;
    private long elapsedTime = 0;

    private final Interpolator interpolator;
    private final Consumer<Double> onUpdate;
    private boolean canceled = false;

    /**
     * Constructs a new Tween animation.
     *
     * @param startValue   The initial numerical value.
     * @param endValue     The target numerical value.
     * @param duration     The duration of the animation in milliseconds.
     * @param interpolator The easing function to apply.
     * @param onUpdate     The callback invoked every frame with the new interpolated value.
     */
    public Tween(double startValue, double endValue, long duration, Interpolator interpolator, Consumer<Double> onUpdate) {
        this.startValue = startValue;
        this.endValue = endValue;
        this.duration = duration;
        this.interpolator = interpolator;
        this.onUpdate = onUpdate;
    }

    /**
     * Flags this tween to be canceled and removed on the next engine tick.
     */
    public void cancel() {
        this.canceled = true;
    }

    /**
     * Steps the animation forward based on the elapsed time since the last frame.
     *
     * @param deltaTime The time elapsed in milliseconds.
     * @return True if the animation is complete or canceled, false otherwise.
     */
    public boolean update(long deltaTime) {
        if (canceled) {
            return true;
        }

        elapsedTime += deltaTime;

        // Clamp fraction to 1.0 to prevent overshooting
        double fraction = Math.min(1.0, (double) elapsedTime / duration);

        // Apply easing and calculate final value
        double easedFraction = interpolator.interpolate(fraction);
        double currentValue = startValue + (endValue - startValue) * easedFraction;

        // Push the new value back to the component
        onUpdate.accept(currentValue);

        return fraction >= 1.0;
    }
}