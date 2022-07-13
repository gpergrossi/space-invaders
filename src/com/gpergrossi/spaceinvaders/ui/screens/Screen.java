package com.gpergrossi.spaceinvaders.ui.screens;

import com.gpergrossi.spaceinvaders.animation.AnimationSystem;
import com.gpergrossi.spaceinvaders.game.Input;
import com.gpergrossi.spaceinvaders.ui.Container;

public class Screen extends Container {

    protected boolean overlay;

    public Screen(boolean overlay) {
        super();
        this.overlay = overlay;
    }

    public void onShow(Input input, AnimationSystem animationSystem) {
        this.registerInputListeners(input);
        this.registerAnimations(animationSystem);
    }

    public void onHide(Input input, AnimationSystem animationSystem) {
        this.unregisterInputListeners(input);
        this.unregisterAnimations(animationSystem);
    }

    public boolean isOverlay() {
        return overlay;
    }

}
