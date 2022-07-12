package com.gpergrossi.spaceinvaders.render;

import com.gpergrossi.spaceinvaders.assets.Sprite;
import com.gpergrossi.spaceinvaders.entity.SpriteEntity;

import java.awt.*;

public class SpriteEntityRenderer implements Renderer<SpriteEntity> {

    private static SpriteEntityRenderer single = new SpriteEntityRenderer();

    public static SpriteEntityRenderer get() { return single; }

    @Override
    public void render(Graphics2D g, SpriteEntity entity) {
        Sprite sprite = entity.getSprite();
        int x = (int) entity.getX();
        int y = (int) entity.getY();

        if (sprite != null) {
            sprite.draw(g, x, y);
        }
    }

}
