package com.gpergrossi.spaceinvaders.render;

import com.gpergrossi.spaceinvaders.entity.Entity;

import java.awt.*;

public interface Renderer<T> {

    void render(Graphics2D g, T obj);

}
