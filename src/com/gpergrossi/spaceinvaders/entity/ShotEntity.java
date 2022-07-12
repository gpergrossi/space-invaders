package com.gpergrossi.spaceinvaders.entity;

import com.gpergrossi.spaceinvaders.Game;
import com.gpergrossi.spaceinvaders.Sprite;

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
	}

	/**
	 * Request that this shot moved based on time elapsed
	 * 
	 * @param delta The time that has elapsed since last move
	 */
	@Override
	public void updateLogic(long delta) {
		// Proceed with normal move
		super.move(delta);
		
		// If we shot is off the screen, remove it
		if (y < -100) {
			game.removeEntity(this);
		}
	}

	/**
	 * Do the logic associated with updating this entity.
	 * This method will be called once per frame.
	 *
	 * @param delta The amount of time that has passed in milliseconds
	 */
	@Override
	public void updateAnimation(long delta) {
		// Physics entities will process no animation updates by default.
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
			
			// notify the game that the alien has been killed
			game.notifyAlienKilled(alien);

			used = true;
		}
	}
}