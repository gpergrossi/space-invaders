package com.gpergrossi.spaceinvaders.entity;

import com.gpergrossi.spaceinvaders.Game;

import java.awt.geom.Rectangle2D;

/**
 * A PhysicsEntity represents any physical element that appears in the game.
 * The physics entity is responsible for resolving collisions and movement
 * based on a set of properties defined either by subclass or externally.
 *
 * Note that floats are used for positions. This may seem strange
 * given that pixels locations are integers. However, using floats means
 * that an entity can move a partial pixel. It doesn't of course mean that
 * they will be display half way through a pixel but allows us not lose
 * accuracy as we move.
 *
 * @author Kevin Glass, Gregary Pergrossi
 */
public abstract class PhysicsEntity extends Entity {

    /** The current x location of this entity */
    protected float x;

    /** The current y location of this entity */
    protected float y;

    /** The width of this entity for collision purposes. */
    protected float width;

    /** The height of this entity for collision purposes. */
    protected float height;

    /** The current speed of this entity horizontally (pixels/sec) */
    protected float dx;

    /** The current speed of this entity vertically (pixels/sec) */
    protected float dy;

    /**
     * Construct a physics entity based on initial location and size.
     *
     * @param x The initial x location of this entity.
     * @param y The initial y location of this entity.
     * @param width The width of this entity for collision purposes.
     * @param height The height of this entity for collision purposes.
     */
    public PhysicsEntity(Game game, float x, float y, float width, float height) {
        super(game);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.dx = 0;
        this.dy = 0;
    }

    /**
     * Get the bounds of this entity and store them in the provided Rectangle2D.Float object.
     */
    public void getBounds(Rectangle2D.Float result) {
        if (result != null) {
            result.setRect(x, y, width, height);
        }
    }

    /**
     * @return The x location of this entity
     */
    public float getX() {
        return x;
    }

    /**
     * @return The y location of this entity
     */
    public float getY() {
        return y;
    }

    /**
     * @return The width of this entity
     */
    public float getWidth() {
        return width;
    }

    /**
     * @return The height of this entity
     */
    public float getHeight() {
        return height;
    }

    /**
     * Get the horizontal speed of this entity
     *
     * @return The horizontal speed of this entity (pixels/sec)
     */
    public float getVelocityX() {
        return dx;
    }

    /**
     * Get the vertical speed of this entity
     *
     * @return The vertical speed of this entity (pixels/sec)
     */
    public float getVelocityY() {
        return dy;
    }

    /**
     * Set the horizontal speed of this entity
     *
     * @param dx The horizontal speed of this entity (pixels/sec)
     */
    public void setVelocityX(float dx) {
        this.dx = dx;
    }

    /**
     * Set the vertical speed of this entity
     *
     * @param dy The vertical speed of this entity (pixels/sec)
     */
    public void setVelocityY(float dy) {
        this.dy = dy;
    }

    /**
     * Request that this entity move itself based on a certain amount of time passing.
     *
     * @param delta The amount of time that has passed in milliseconds
     */
    public void move(long delta) {
        // update the location of the entity based on move speeds
        x += (delta * dx) / 1000.0f;
        y += (delta * dy) / 1000.0f;
    }

    /**
     * Do the logic associated with updating this entity.
     * This method will be called once per frame.
     *
     * @param delta The amount of time that has passed in milliseconds
     */
    @Override
    public void updateLogic(long delta) {
        // Physics entities are expected to move each frame.
        this.move(delta);
    }

    /**
     * Notification that this entity collided with another.
     *
     * @param other The entity with which this entity collided.
     */
    public abstract void onCollision(Entity other);
}
