package com.gpergrossi.spaceinvaders.ui;

import com.gpergrossi.spaceinvaders.animation.AnimationSystem;
import com.gpergrossi.spaceinvaders.game.Input;

import java.awt.*;
import java.util.ArrayList;

public class Container extends Component {

    protected ArrayList<Component> components;

    public Container() {
        this.components = new ArrayList<>();
    }

    public void add(Component c) {
        this.components.add(c);
    }

    public void remove(Component c) {
        this.components.remove(c);
    }

    @Override
    public void registerInputListeners(Input input) {
        for (Component c : components) {
            c.registerInputListeners(input);
        }
    }

    @Override
    public void unregisterInputListeners(Input input) {
        for (Component c : components) {
            c.unregisterInputListeners(input);
        }
    }

    @Override
    public void registerAnimations(AnimationSystem animationSystem) {
        for (Component c : components) {
            c.registerAnimations(animationSystem);
        }
    }

    @Override
    public void unregisterAnimations(AnimationSystem animationSystem) {
        for (Component c : components) {
            c.unregisterAnimations(animationSystem);
        }
    }

    @Override
    public void render(Graphics2D g) {
        for (Component c : components) {
            c.render(g);
        }
    }

}
