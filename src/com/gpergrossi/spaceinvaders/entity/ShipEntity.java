package com.gpergrossi.spaceinvaders.entity;

import com.gpergrossi.spaceinvaders.assets.TintedSprite;
import com.gpergrossi.spaceinvaders.game.Game;
import com.gpergrossi.spaceinvaders.assets.Sprite;
import com.gpergrossi.spaceinvaders.game.Settings;

import java.awt.*;

/**
 * The entity that represents the players ship
 * 
 * @author Kevin Glass
 */
public class ShipEntity extends SpriteEntity {
	/** The game in which the ship exists */
	private Game game;

	private TintedSprite shipSprite;

	private int reloadTime;
	private int reloadProgress;

	/**
	 * Create a new entity to represent the players ship
	 *
	 * @param game   The game to which this entity belongs.
	 * @param sprite The sprite used to render this entity (and determine its size).
	 * @param x      The initial x location of this entity.
	 * @param y      The initial y location of this entity.
	 */
	public ShipEntity(Game game, TintedSprite sprite, float x, float y) {
		super(game, sprite, x, y);

		this.shipSprite = sprite;

		Settings settings = game.getSettings();
		reloadTime = settings.getPlayerReloadTime();
		reloadProgress = reloadTime;
	}

	/**
	 * Process any logical updates associated with this entity.
	 * This method will not be called while the game is paused.
	 *
	 * @param delta The amount of time that has passed in milliseconds
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

		reloadProgress += delta;
		if (reloadProgress > reloadTime) {
			reloadProgress = reloadTime;
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
		float reloadPercent = (float) reloadProgress / reloadTime;

		int charge = (int) (255 * reloadPercent);
		shipSprite.setColor(new Color(255, charge, charge));
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

	public boolean canShoot() {
		return (reloadProgress == reloadTime);
	}

	public void resetShotTimer() {
		reloadProgress = 0;
	}
}