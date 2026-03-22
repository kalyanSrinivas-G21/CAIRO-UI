package com.uiframework.cairo.layout;

import com.uiframework.cairo.core.Component;
import com.uiframework.cairo.core.Container;
import com.uiframework.cairo.core.LayoutManager;
import com.uiframework.cairo.core.Size;

/**
 * A LayoutManager that stacks components vertically.
 * Updated to respect a base width to prevent "squashed" sidebars.
 */
public class VerticalLayout implements LayoutManager {

    private int hGap;
    private int vGap;
    private int minWidth = 0;

    /**
     * @param hGap Horizontal padding.
     * @param vGap Vertical padding.
     */
    public VerticalLayout(int hGap, int vGap) {
        this.hGap = hGap;
        this.vGap = vGap;
    }

    /**
     * Convenience constructor with a minimum width constraint.
     */
    public VerticalLayout(int hGap, int vGap, int minWidth) {
        this.hGap = hGap;
        this.vGap = vGap;
        this.minWidth = minWidth;
    }

    @Override
    public void layout(Container parent) {
        int currentY = vGap;
        int availableWidth = parent.getWidth() - (2 * hGap);

        for (Component child : parent.getChildren()) {
            if (!child.isVisible()) continue;
            Size pref = child.getPreferredSize();
            child.setBounds(hGap, currentY, availableWidth, pref.height);
            currentY += pref.height + vGap;
        }
    }

    @Override
    public int getPreferredWidth(Container parent) {
        int maxChildWidth = 0;
        for (Component child : parent.getChildren()) {
            if (child.isVisible()) {
                maxChildWidth = Math.max(maxChildWidth, child.getPreferredSize().width);
            }
        }
        return Math.max(minWidth, maxChildWidth + (2 * hGap));
    }

    @Override
    public int getPreferredHeight(Container parent) {
        int totalHeight = vGap;
        for (Component child : parent.getChildren()) {
            if (child.isVisible()) {
                totalHeight += child.getPreferredSize().height + vGap;
            }
        }
        return totalHeight;
    }
}