package com.gpergrossi.spaceinvaders.entity;

import com.gpergrossi.spaceinvaders.Game;
import com.gpergrossi.spaceinvaders.Sprite;

/**
 * The entity that represents the players ship
 * 
 * @author Kevin Glass
 */
public class ShipEntity extends SpriteEntity {
	/** The game in which the ship exists */
	private Game game;
	
	/**
	 * Create a new entity to represent the players ship
	 *
	 * @param game   The game to which this entity belongs.
	 * @param sprite The sprite used to render this entity (and determine its size).
	 * @param x      The initial x location of this entity.
	 * @param y      The initial y location of this entity.
	 */
	public ShipEntity(Game game, Sprite sprite, float x, float y) {
		super(game, sprite, x, y);
	}
	
	/**
	 * Request that the ship move itself based on an elapsed amount of time
	 * 
	 * @param delta The time that has elapsed since last move (ms)
	 */
	@Override
	public void updateLogic(long delta) {
		super.move(delta);

		// If we're moving left and have reached the left hand side of the screen, don't move
		if ((dx < 0) && (x < 10)) {
			x = 10;
			return;
		}

		// If we're moving right and have reached the right hand side of the screen, don't move
		if ((dx > 0) && (x > 750)) {
			x = 750;
			return;
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
	 * Notification that the player's ship has collided with something
	 * 
	 * @param other The entity with which the ship has collided
	 */
	public void onCollision(Entity other) {
		// If its an alien, notify the game that the player is dead
		if (other instanceof AlienEntity) {
			game.notifyDeath();
		}
	}
}