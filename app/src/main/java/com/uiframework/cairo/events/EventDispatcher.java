package com.uiframework.cairo.event;

import com.uiframework.cairo.core.Component;
import com.uiframework.cairo.core.Container;
import org.teavm.jso.JSProperty;
import org.teavm.jso.dom.events.Event;
import org.teavm.jso.dom.html.HTMLCanvasElement;

/**
 * Acts as the bridge between raw browser JavaScript events and the CAIRO
 * component-based event system. It normalizes coordinates and performs
 * hit-testing to route interactions to the correct UI targets.
 */
public class EventDispatcher {

    private final Container root;
    private Component lastHovered;

    /**
     * Private interface to map standard DOM offsetX/offsetY properties safely
     * within the TeaVM JavaScript object ecosystem.
     */
    private interface NativeMouseEvent extends Event {
        @JSProperty("offsetX")
        int getOffsetX();

        @JSProperty("offsetY")
        int getOffsetY();
    }

    /**
     * Initializes the EventDispatcher and binds native browser listeners to the Canvas.
     *
     * @param root   The root Container of the UI tree.
     * @param canvas The physical HTML5 Canvas element to listen on.
     */
    public EventDispatcher(Container root, HTMLCanvasElement canvas) {
        this.root = root;

        canvas.addEventListener("mousedown", evt ->
                handleNativeMouse((NativeMouseEvent) evt, MouseEventType.MOUSE_PRESSED));

        canvas.addEventListener("mouseup", evt ->
                handleNativeMouse((NativeMouseEvent) evt, MouseEventType.MOUSE_RELEASED));

        canvas.addEventListener("mousemove", evt ->
                handleNativeMouse((NativeMouseEvent) evt, MouseEventType.MOUSE_MOVED));
    }

    /**
     * Processes a raw browser event, identifies the target component, and dispatches
     * framework-safe UIEvent objects. Also manages the enter/exit hover lifecycle.
     *
     * @param evt  The native browser mouse event.
     * @param type The mapped CAIRO framework event type.
     */
    private void handleNativeMouse(NativeMouseEvent evt, MouseEventType type) {
        int x = evt.getOffsetX();
        int y = evt.getOffsetY();

        // 1. Perform a hit-test to find the exact leaf component under the cursor
        Component target = root.getComponentAt(x, y);

        // 2. Hover Lifecycle Management
        if (target != lastHovered) {
            if (lastHovered != null) {
                lastHovered.fireEvent(new MouseEvent(lastHovered, MouseEventType.MOUSE_EXITED, x, y));
            }
            if (target != null) {
                target.fireEvent(new MouseEvent(target, MouseEventType.MOUSE_ENTERED, x, y));
            }
            lastHovered = target;
        }

        // 3. Dispatch the core action event (Click, Move, Press, Release)
        if (target != null) {
            target.fireEvent(new MouseEvent(target, type, x, y));
        }
    }
}