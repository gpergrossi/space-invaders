package com.gpergrossi.spaceinvaders.ui.screens;

import com.gpergrossi.spaceinvaders.animation.AnimationSystem;
import com.gpergrossi.spaceinvaders.game.Input;
import com.gpergrossi.spaceinvaders.ui.Container;

import java.awt.*;
import java.util.function.Consumer;

public abstract class Screen extends Container {

    protected boolean overlay;
    protected boolean ready;

    protected Runnable onReady;
    protected Runnable onCleanup;

    public Screen(boolean overlay) {
        super();
        this.overlay = overlay;
        this.ready = false;
    }

    /** Used to create components like AnimatedText that require a Graphics context. */
    public abstract void init(Graphics2D g);

    public void reset() {
        this.components.clear();
        this.ready = false;
    }

    public void onShow(Graphics2D g, Input input, AnimationSystem animationSystem) {
        this.init(g);
        this.registerInputListeners(input);
        this.registerAnimations(animationSystem);
        ready = true;

        // Call the onReady callback if it exists
        if (onReady != null) {
            Runnable cb = onReady;
            onReady = null;
            cb.run();
        }
    }

    public void onHide(Input input, AnimationSystem animationSystem) {
        this.unregisterInputListeners(input);
        this.unregisterAnimations(animationSystem);
        ready = false;

        // Call the onCleanup callback if it exists
        if (onCleanup != null) {
            Runnable cb = onCleanup;
            onCleanup = null;
            cb.run();
        }
    }

    public boolean isOverlay() {
        return overlay;
    }

    public boolean isReady() {
        return ready;
    }

    public void setOnReady(Runnable onReady) {
        this.onReady = onReady;
    }

    public void setOnCleanup(Runnable onCleanup) {
        this.onCleanup = onCleanup;
    }

}
