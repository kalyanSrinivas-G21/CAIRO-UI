package com.uiframework.cairo.layout;

import com.uiframework.cairo.core.Component;
import com.uiframework.cairo.core.Container;
import com.uiframework.cairo.core.LayoutManager;
import com.uiframework.cairo.core.Size;

/**
 * A LayoutManager that arranges components horizontally.
 * When a component exceeds the horizontal width of the container,
 * it wraps to the next row.
 */
public class FlowLayout implements LayoutManager {

    private int hGap;
    private int vGap;

    /**
     * Constructs a FlowLayout with specified gaps.
     *
     * @param hGap Horizontal space between components in pixels.
     * @param vGap Vertical space between rows in pixels.
     */
    public FlowLayout(int hGap, int vGap) {
        this.hGap = hGap;
        this.vGap = vGap;
    }

    /**
     * Executes the flow positioning logic for the children of the container.
     *
     * @param parent The container whose children are being laid out.
     */
    @Override
    public void layout(Container parent) {
        int currentX = hGap;
        int currentY = vGap;
        int rowHeight = 0;
        int parentWidth = parent.getWidth();

        for (Component child : parent.getChildren()) {
            if (!child.isVisible()) {
                continue;
            }

            Size pref = child.getPreferredSize();
            int childWidth = pref.width;
            int childHeight = pref.height;

            // Wrapping logic: check if the component fits in the current row
            if (currentX + childWidth + hGap > parentWidth) {
                currentX = hGap;
                currentY += rowHeight + vGap;
                rowHeight = 0;
            }

            child.setBounds(currentX, currentY, childWidth, childHeight);

            currentX += childWidth + hGap;
            rowHeight = Math.max(rowHeight, childHeight);
        }
    }

    /**
     * Calculates the width required to fit all children in a single line.
     *
     * @param parent The container to measure.
     * @return The preferred width.
     */
    @Override
    public int getPreferredWidth(Container parent) {
        int totalWidth = hGap;
        for (Component child : parent.getChildren()) {
            if (child.isVisible()) {
                totalWidth += child.getPreferredSize().width + hGap;
            }
        }
        return totalWidth;
    }

    /**
     * Calculates the height required for the current children.
     *
     * @param parent The container to measure.
     * @return The preferred height.
     */
    @Override
    public int getPreferredHeight(Container parent) {
        int maxHeight = 0;
        for (Component child : parent.getChildren()) {
            if (child.isVisible()) {
                maxHeight = Math.max(maxHeight, child.getPreferredSize().height);
            }
        }
        return maxHeight + (vGap * 2);
    }
}