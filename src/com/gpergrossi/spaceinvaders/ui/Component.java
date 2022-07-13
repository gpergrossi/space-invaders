package com.gpergrossi.spaceinvaders.ui;

import com.gpergrossi.spaceinvaders.animation.AnimationSystem;
import com.gpergrossi.spaceinvaders.game.Input;

import java.awt.*;

public abstract class Component {

    public abstract void registerInputListeners(Input input);

    public abstract void unregisterInputListeners(Input input);

    public abstract void registerAnimations(AnimationSystem animationSystem);

    public abstract void unregisterAnimations(AnimationSystem animationSystem);

    public abstract void render(Graphics2D g);

    public void doCallbacks() {}

}
