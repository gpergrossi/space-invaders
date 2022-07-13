package com.gpergrossi.spaceinvaders.render;

import com.gpergrossi.spaceinvaders.assets.Sprite;
import com.gpergrossi.spaceinvaders.entity.AlienEntity;
import com.gpergrossi.spaceinvaders.entity.SpriteEntity;

import java.awt.*;

public class AlienEntityRenderer implements Renderer<AlienEntity> {

    private static AlienEntityRenderer single = new AlienEntityRenderer();

    public static AlienEntityRenderer get() { return single; }

    @Override
    public void render(Graphics2D g, AlienEntity entity) {
        double spawn = entity.getSpawnAnimation().getValue();

        if (spawn < 1.0) {
            // Fade in
            g.setComposite(AlphaComposite.SrcOver.derive((float) spawn));
        }

        Sprite sprite = entity.getSprite();
        int x = (int) entity.getX();
        int y = (int) entity.getY() - (int)((1.0f - spawn) * 50f);

        if (sprite != null) {
            sprite.draw(g, x, y);
        }

        if (spawn < 1.0) {
            // Reset alpha composite
            g.setComposite(AlphaComposite.SrcOver);
        }
    }

}
