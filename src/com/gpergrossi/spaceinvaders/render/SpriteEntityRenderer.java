package com.gpergrossi.spaceinvaders.render;

import com.gpergrossi.spaceinvaders.Sprite;
import com.gpergrossi.spaceinvaders.entity.Entity;
import com.gpergrossi.spaceinvaders.entity.SpriteEntity;

import java.awt.*;

public class SpriteEntityRenderer implements Renderer {

    private static SpriteEntityRenderer single = new SpriteEntityRenderer();

    public static SpriteEntityRenderer get() { return single; }

    @Override
    public void render(Graphics2D g, Entity entity) {

        if (entity instanceof  SpriteEntity) {
            SpriteEntity se = (SpriteEntity) entity;

            Sprite sprite = se.getSprite();
            int x = (int) se.getX();
            int y = (int) se.getY();

            if (sprite != null) {
                sprite.draw(g, x, y);
            }
        }

    }

}
