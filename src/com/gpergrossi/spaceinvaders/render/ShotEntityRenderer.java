package com.gpergrossi.spaceinvaders.render;

import com.gpergrossi.spaceinvaders.assets.Sprite;
import com.gpergrossi.spaceinvaders.entity.ShotEntity;
import com.gpergrossi.spaceinvaders.entity.SpriteEntity;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class ShotEntityRenderer implements Renderer<ShotEntity> {

    private static ShotEntityRenderer single = new ShotEntityRenderer();

    public static ShotEntityRenderer get() { return single; }

    private static AffineTransform identity = new AffineTransform();

    @Override
    public void render(Graphics2D g, ShotEntity entity) {
        Sprite sprite = entity.getSprite();
        float x = entity.getX();
        float y = entity.getY();

        float size = entity.getSize();

        if (sprite != null) {
            AffineTransform transform = AffineTransform.getTranslateInstance(x + 6, y + 5);
            transform.concatenate(AffineTransform.getScaleInstance(size, size));
            transform.concatenate(AffineTransform.getTranslateInstance(-6, -5));

            g.setTransform(transform);
            sprite.draw(g, 0, 0);
            g.setTransform(identity);
        }
    }

}
