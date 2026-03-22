package com.uiframework.cairo.event;

import com.uiframework.cairo.core.Component;
import com.uiframework.cairo.core.Container;
import com.uiframework.cairo.core.FocusManager;
import org.teavm.jso.JSProperty;
import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.events.Event;
import org.teavm.jso.dom.html.HTMLCanvasElement;

/**
 * Acts as the bridge between raw browser JavaScript events and the CAIRO
 * component-based event system. Normalizes coordinates, hit-tests for mouse
 * targets, and routes keyboard events to the focused component.
 */
public class EventDispatcher {

    private final Container root;
    private Component lastHovered;

    /**
     * Private interface to map standard DOM mouse properties cleanly.
     */
    private interface NativeMouseEvent extends Event {
        @JSProperty("offsetX")
        int getOffsetX();

        @JSProperty("offsetY")
        int getOffsetY();
    }

    /**
     * Private interface to map standard DOM keyboard properties cleanly.
     */
    private interface NativeKeyboardEvent extends Event {
        @JSProperty("key")
        String getKey();

        @JSProperty("keyCode")
        int getKeyCode();

        @JSProperty("shiftKey")
        boolean getShiftKey();
    }

    /**
     * Initializes the EventDispatcher and binds native browser listeners.
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

        Window.current().addEventListener("keydown", evt ->
                handleNativeKey((NativeKeyboardEvent) evt));
    }

    /**
     * Processes a raw browser mouse event and manages focus transition upon clicks.
     *
     * @param evt  The native browser mouse event.
     * @param type The mapped CAIRO framework event type.
     */
    private void handleNativeMouse(NativeMouseEvent evt, MouseEventType type) {
        int x = evt.getOffsetX();
        int y = evt.getOffsetY();

        Component target = root.getComponentAt(x, y);

        // Manage Focus on Click
        if (type == MouseEventType.MOUSE_PRESSED) {
            if (target != null && target.isFocusable()) {
                target.requestFocus();
            } else {
                FocusManager.setFocus(null); // Clicked dead space or un-focusable component
            }
        }

        // Hover Lifecycle Management
        if (target != lastHovered) {
            if (lastHovered != null) {
                lastHovered.fireEvent(new MouseEvent(lastHovered, MouseEventType.MOUSE_EXITED, x, y));
            }
            if (target != null) {
                target.fireEvent(new MouseEvent(target, MouseEventType.MOUSE_ENTERED, x, y));
            }
            lastHovered = target;
        }

        // Dispatch the core action event
        if (target != null) {
            target.fireEvent(new MouseEvent(target, type, x, y));
        }
    }

    /**
     * Processes a raw browser keyboard event and routes it to the focused component.
     *
     * @param evt The native browser keyboard event.
     */
    private void handleNativeKey(NativeKeyboardEvent evt) {
        Component focused = FocusManager.getFocusedComponent();
        if (focused != null) {
            KeyEvent keyEvent = new KeyEvent(focused, evt.getKey(), evt.getKeyCode(), evt.getShiftKey());
            focused.fireEvent(keyEvent);
        }
    }
}