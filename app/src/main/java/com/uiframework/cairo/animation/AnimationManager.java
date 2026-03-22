package com.uiframework.cairo.animation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The global registry that manages all active animations in the CAIRO framework.
 * Updates tweens via the main render loop and automatically cleans up finished tasks.
 */
public class AnimationManager {

    private static final AnimationManager INSTANCE = new AnimationManager();

    private final List<Tween> activeTweens = new ArrayList<>();
    private final List<Tween> pendingAdditions = new ArrayList<>();

    private AnimationManager() {
        // Private constructor for Singleton
    }

    /**
     * Retrieves the singleton instance of the AnimationManager.
     *
     * @return The active AnimationManager.
     */
    public static AnimationManager getInstance() {
        return INSTANCE;
    }

    /**
     * Queues a new Tween to begin animating on the next engine tick.
     *
     * @param t The Tween to add.
     */
    public void add(Tween t) {
        pendingAdditions.add(t);
    }

    /**
     * Processes all active animations, advancing them by the delta time.
     * Finished or canceled tweens are automatically removed from the active list.
     *
     * @param deltaTime The time elapsed since the last frame in milliseconds.
     */
    public void tick(long deltaTime) {
        // Safely migrate newly queued tweens into the active rotation
        activeTweens.addAll(pendingAdditions);
        pendingAdditions.clear();

        Iterator<Tween> iterator = activeTweens.iterator();
        while (iterator.hasNext()) {
            Tween t = iterator.next();
            if (t.update(deltaTime)) {
                iterator.remove();
            }
        }
    }

    /**
     * @return True if there are tweens actively running or queued to run.
     */
    public boolean hasActiveTweens() {
        return !activeTweens.isEmpty() || !pendingAdditions.isEmpty();
    }
}