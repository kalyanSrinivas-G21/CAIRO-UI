package com.uiframework.cairo.layout;

import com.uiframework.cairo.core.Anchor;
import com.uiframework.cairo.core.Component;
import com.uiframework.cairo.core.Container;
import com.uiframework.cairo.core.LayoutManager;
import com.uiframework.cairo.core.Size;

/**
 * A LayoutManager that arranges components into five regions: North, South,
 * East, West, and Center. North and South take the full width, while East
 * and West take the remaining vertical height. Center fills the remaining hole.
 */
public class BorderLayout implements LayoutManager {

    private int hGap;
    private int vGap;

    /**
     * Constructs a BorderLayout with specified gaps between regions.
     *
     * @param hGap Horizontal gap in pixels.
     * @param vGap Vertical gap in pixels.
     */
    public BorderLayout(int hGap, int vGap) {
        this.hGap = hGap;
        this.vGap = vGap;
    }

    /**
     * Positions components according to their Anchor constraints.
     *
     * @param parent The container being laid out.
     */
    @Override
    public void layout(Container parent) {
        int parentWidth = parent.getWidth();
        int parentHeight = parent.getHeight();

        // Bounds of the "remaining hole" for the center
        int top = 0;
        int bottom = parentHeight;
        int left = 0;
        int right = parentWidth;

        Component north = null;
        Component south = null;
        Component west = null;
        Component east = null;
        Component center = null;

        // Categorize visible children by their anchored region
        for (Component child : parent.getChildren()) {
            if (!child.isVisible()) continue;
            Anchor anchor = child.getConstraints().anchor;

            if (anchor == Anchor.NORTH) north = child;
            else if (anchor == Anchor.SOUTH) south = child;
            else if (anchor == Anchor.WEST) west = child;
            else if (anchor == Anchor.EAST) east = child;
            else if (anchor == Anchor.CENTER) center = child;
        }

        // 1. Layout North: Full width at the top
        if (north != null) {
            int prefHeight = north.getPreferredSize().height;
            north.setBounds(0, 0, parentWidth, prefHeight);
            top += prefHeight + vGap;
        }

        // 2. Layout South: Full width at the bottom
        if (south != null) {
            int prefHeight = south.getPreferredSize().height;
            south.setBounds(0, parentHeight - prefHeight, parentWidth, prefHeight);
            bottom -= prefHeight + vGap;
        }

        // 3. Layout West: Fixed width between North and South
        if (west != null) {
            int prefWidth = west.getPreferredSize().width;
            int h = Math.max(0, bottom - top);
            west.setBounds(0, top, prefWidth, h);
            left += prefWidth + hGap;
        }

        // 4. Layout East: Fixed width between North and South
        if (east != null) {
            int prefWidth = east.getPreferredSize().width;
            int h = Math.max(0, bottom - top);
            east.setBounds(parentWidth - prefWidth, top, prefWidth, h);
            right -= prefWidth + hGap;
        }

        // 5. Layout Center: Fills the remaining space
        if (center != null) {
            int w = Math.max(0, right - left);
            int h = Math.max(0, bottom - top);
            center.setBounds(left, top, w, h);
        }
    }

    @Override
    public int getPreferredWidth(Container parent) {
        int nWidth = 0, sWidth = 0, wWidth = 0, eWidth = 0, cWidth = 0;

        for (Component child : parent.getChildren()) {
            if (!child.isVisible()) continue;
            Size s = child.getPreferredSize();
            Anchor a = child.getConstraints().anchor;
            if (a == Anchor.NORTH) nWidth = s.width;
            else if (a == Anchor.SOUTH) sWidth = s.width;
            else if (a == Anchor.WEST) wWidth = s.width;
            else if (a == Anchor.EAST) eWidth = s.width;
            else if (a == Anchor.CENTER) cWidth = s.width;
        }

        int middleWidth = wWidth + cWidth + eWidth + (2 * hGap);
        return Math.max(nWidth, Math.max(sWidth, middleWidth));
    }

    @Override
    public int getPreferredHeight(Container parent) {
        int nHeight = 0, sHeight = 0, wHeight = 0, eHeight = 0, cHeight = 0;

        for (Component child : parent.getChildren()) {
            if (!child.isVisible()) continue;
            Size s = child.getPreferredSize();
            Anchor a = child.getConstraints().anchor;
            if (a == Anchor.NORTH) nHeight = s.height;
            else if (a == Anchor.SOUTH) sHeight = s.height;
            else if (a == Anchor.WEST) wHeight = s.height;
            else if (a == Anchor.EAST) eHeight = s.height;
            else if (a == Anchor.CENTER) cHeight = s.height;
        }

        int middleHeight = Math.max(wHeight, Math.max(eHeight, cHeight));
        return nHeight + middleHeight + sHeight + (2 * vGap);
    }
}