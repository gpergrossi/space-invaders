package com.gpergrossi.spaceinvaders.entity;

import com.gpergrossi.spaceinvaders.game.Game;
import com.gpergrossi.spaceinvaders.assets.Sprite;
import com.gpergrossi.spaceinvaders.render.Renderer;
import com.gpergrossi.spaceinvaders.render.ShotEntityRenderer;
import com.gpergrossi.spaceinvaders.render.SpriteEntityRenderer;

/**
 * An entity representing a shot fired by the player's ship
 * 
 * @author Kevin Glass
 */
public class ShotEntity extends SpriteEntity {

	/** The vertical speed at which the players shot moves */
	private float moveSpeed = -300;

	/** True if this shot has been "used", i.e. its hit something */
	private boolean used;

	/** The size of the shot will change over time */
	private float size;

	private float particleInterval;
	private float particleTimeRemaining;
	
	/**
	 * Create a new shot from the player
	 *
	 * @param game   The game to which this entity belongs.
	 * @param sprite The sprite used to render this entity (and determine its size).
	 * @param x      The initial x location of this entity.
	 * @param y      The initial y location of this entity.
	 */
	public ShotEntity(Game game, Sprite sprite, float x, float y) {
		super(game, sprite, x, y);
		
		dy = moveSpeed;
		used = false;
		size = 0.75f;

		particleInterval = 0.01f;
		particleTimeRemaining = particleInterval;
	}

	/**
	 * Process any logical updates associated with this entity.
	 * This method will not be called while the game is paused.
	 *
	 * @param delta The amount of time that has passed in milliseconds
	 */
	@Override
	public void updateLogic(long delta) {
		// Proceed with normal move
		super.move(delta);
		
		// If we shot is off the screen, remove it
		if (y < -100) {
			game.removeEntity(this);
			game.notifyShotMissed(this);
		}
	}

	/**
	 * Process any updates associated with this entity's visual animation only.
	 * This method is called even when the game is paused.
	 *
	 * @param delta The amount of time that has passed in milliseconds
	 */
	@Override
	public void updateAnimation(long delta) {
		float timeStep = delta / 1000.0f;

		this.size += timeStep * 0.5;
		if (this.size > 1.1f) this.size = 1.1f;

		particleTimeRemaining -= timeStep;
		if (particleTimeRemaining <= 0f) {
			game.getParticleSystem().spawnBulletTrail(x + 5.5f, y + 5.5f);
			particleTimeRemaining += particleInterval;
		}
	}

	@Override
	public Renderer getRenderer() {
		return ShotEntityRenderer.get();
	}

	/**
	 * Notification that this shot has collided with another entity
	 * 
	 * @parma other The other entity with which we've collided
	 */
	@Override
	public void onCollision(Entity other) {
		// Prevents double kills, if we've already hit something, don't collide
		if (used) {
			return;
		}
		
		// If we've hit an alien, kill it!
		if (other instanceof AlienEntity) {
			AlienEntity alien = (AlienEntity) other;

			// Remove the affected entities
			game.removeEntity(this);
			game.removeEntity(alien);
			
			// notify the game
			game.notifyShotHit(this);
			game.notifyAlienKilled(alien);

			used = true;
		}
	}

	public float getSize() {
		return size;
	}
}