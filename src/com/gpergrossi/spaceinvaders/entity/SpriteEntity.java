package com.gpergrossi.spaceinvaders.entity;

import com.gpergrossi.spaceinvaders.game.Game;
import com.gpergrossi.spaceinvaders.assets.Sprite;
import com.gpergrossi.spaceinvaders.render.Renderer;
import com.gpergrossi.spaceinvaders.render.SpriteEntityRenderer;

public abstract class SpriteEntity extends PhysicsEntity {

    /** The sprite image for this entity */
    protected Sprite sprite;

    /**
     * Construct an entity based on a sprite image and a location.
     *
     * @param game   The game to which this entity belongs.
     * @param sprite The sprite used to render this entity (and determine its size).
     * @param x      The initial x location of this entity.
     * @param y      The initial y location of this entity.
     */
    public SpriteEntity(Game game, Sprite sprite, float x, float y) {
        super(game, x, y, sprite.getWidth(), sprite.getHeight());
        this.sprite = sprite;
    }

    /**
     * @return The sprite associate with this entity.
     */
    public Sprite getSprite() {
        return sprite;
    }

    @Override
    public Renderer getRenderer() {
        return SpriteEntityRenderer.get();
    }
}
