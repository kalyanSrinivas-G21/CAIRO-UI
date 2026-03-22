package com.uiframework.cairo.util;

/**
 * A lightweight utility class representing a 2D coordinate in space.
 * Used primarily for defining start, end, and control points in path animations.
 */
public class Point {

    /** The X coordinate. */
    public double x;

    /** The Y coordinate. */
    public double y;

    /**
     * Constructs a new Point at the specified coordinates.
     *
     * @param x The X coordinate.
     * @param y The Y coordinate.
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
}