package com.uiframework.cairo.animation;

/**
 * A functional interface defining an easing function for animations.
 * Takes a linear time fraction and transforms it to control the pacing of a Tween.
 */
@FunctionalInterface
public interface Interpolator {

    /**
     * Calculates the interpolated value for a given fraction.
     *
     * @param fraction The elapsed linear fraction of the animation (0.0 to 1.0).
     * @return The eased fraction to apply to the value interpolation.
     */
    double interpolate(double fraction);

    /**
     * A standard linear interpolator that maps time directly to progress without easing.
     */
    Interpolator LINEAR = f -> f;

    /**
     * A smooth ease-out quadratic interpolator for natural deceleration.
     */
    Interpolator EASE_OUT_QUAD = f -> f * (2 - f);
}