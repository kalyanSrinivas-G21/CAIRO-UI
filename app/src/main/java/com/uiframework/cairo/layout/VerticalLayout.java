package com.uiframework.cairo.layout;

import com.uiframework.cairo.core.Component;
import com.uiframework.cairo.core.Container;
import com.uiframework.cairo.core.LayoutManager;
import com.uiframework.cairo.core.Size;

/**
 * A LayoutManager that stacks components vertically in a single column.
 * All visible components are stretched horizontally to fill the container's
 * available width, minus the specified horizontal gaps.
 */
public class VerticalLayout implements LayoutManager {

    private int hGap;
    private int vGap;

    /**
     * Constructs a VerticalLayout with the specified gaps.
     *
     * @param hGap The horizontal padding on both left and right sides.
     * @param vGap The vertical gap between components.
     */
    public VerticalLayout(int hGap, int vGap) {
        this.hGap = hGap;
        this.vGap = vGap;
    }

    /**
     * Positions and resizes the children of the container into a vertical stack.
     *
     * @param parent The container whose children are being laid out.
     */
    @Override
    public void layout(Container parent) {
        int currentY = vGap;
        // Calculate uniform width: total parent width minus the gaps on both sides
        int availableWidth = parent.getWidth() - (2 * hGap);

        for (Component child : parent.getChildren()) {
            if (!child.isVisible()) {
                continue;
            }

            Size pref = child.getPreferredSize();
            int childPreferredHeight = pref.height;

            // Set bounds: use hGap for x, currentY for y, availableWidth for w
            child.setBounds(hGap, currentY, availableWidth, childPreferredHeight);

            // Increment the vertical tracker for the next component
            currentY += childPreferredHeight + vGap;
        }
    }

    /**
     * Calculates the width required for a single-column stack.
     *
     * @param parent The container to measure.
     * @return The maximum child preferred width plus horizontal gaps.
     */
    @Override
    public int getPreferredWidth(Container parent) {
        int maxChildWidth = 0;
        for (Component child : parent.getChildren()) {
            if (child.isVisible()) {
                maxChildWidth = Math.max(maxChildWidth, child.getPreferredSize().width);
            }
        }
        return maxChildWidth + (2 * hGap);
    }

    /**
     * Calculates the total height required to stack all children vertically.
     *
     * @param parent The container to measure.
     * @return The sum of all children's preferred heights plus total gaps.
     */
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