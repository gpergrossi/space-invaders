package com.gpergrossi.spaceinvaders.entity;

import com.gpergrossi.spaceinvaders.animation.*;
import com.gpergrossi.spaceinvaders.game.Game;
import com.gpergrossi.spaceinvaders.game.Settings;
import com.gpergrossi.spaceinvaders.assets.Sprite;
import com.gpergrossi.spaceinvaders.render.AlienEntityRenderer;
import com.gpergrossi.spaceinvaders.render.Renderer;
import com.gpergrossi.spaceinvaders.render.SpriteEntityRenderer;

/**
 * An entity which represents one of our space invader aliens.
 * 
 * @author Kevin Glass
 */
public class AlienEntity extends SpriteEntity {

	/** The speed at which the alien moves horizontally */
	private float moveSpeed = 75;

	/** The swarm to which the entity belongs */
	private AlienSwarm swarm;

	/** The spawning animation's only animation variable */
	private TweenSequence<Double> spawnAnimation;

	/**
	 * Construct an entity based on a sprite image and a location.
	 *
	 * @param game   The game to which this entity belongs.
	 * @param swarm  The alien swarm to which this alien belongs.
	 * @param sprite The sprite used to render this entity (and determine its size).
	 * @param x      The initial x location of this entity.
	 * @param y      The initial y location of this entity.
	 */
	public AlienEntity(Game game, AlienSwarm swarm, Sprite sprite, float x, float y, double spawnDelay) {
		super(game, sprite, x, y);

		this.dx = -moveSpeed;

		this.swarm = swarm;
		swarm.addAlien(this);

		this.spawnAnimation = new TweenSequence<>("Enemy Spawn Animation",
			new TweenStep<>(0.5, 0.0, 1.0, LerpFunction.LERP_DOUBLE, TweenFunction.EASE_OUT_QUAD)
		);
		this.spawnAnimation.setDefaultStartTime(-spawnDelay);
	}

	public TweenSequence<Double> getSpawnAnimation() {
		return spawnAnimation;
	}

	public boolean isSpawning() {
		return spawnAnimation.getCurrentTime() < spawnAnimation.getDuration();
	}

	/**
	 * @return The AlienSwarm object to which this AlienEntity belongs.
	 */
	public AlienSwarm getSwarm() {
		return swarm;
	}
	
	/**
	 * Flips this alien's movement direction and drops it down by 10.
	 */
	public void changeDirection() {
		Settings settings = game.getSettings();
		int deathZoneMaxY = settings.getScreenHeight() - settings.getDeathZoneHeight() - (int) this.getHeight();

		// swap over horizontal movement and move down the screen a bit
		dx = -dx;
		y += settings.getAlienDropIncrement();

		// if we've reached the bottom of the screen then the player dies
		if (y > deathZoneMaxY) {
			game.notifyDeath();
		}
	}

	/**
	 * Request that this alien moved based on time elapsed
	 *
	 * @param delta The time that has elapsed since last move
	 */
	@Override
	public void updateLogic(long delta) {
		// Don't move while spawning
		if (swarm.isSpawning()) return;

		// Move the ship using the super class implementation
		super.updateLogic(delta);

		Settings settings = game.getSettings();
		int edgeLeft = settings.getAlienBehaviorEdgeWidth();
		int edgeRight = settings.getScreenWidth() - settings.getAlienBehaviorEdgeWidth() - (int) this.getWidth();

		// if we have reached the left hand side of the screen and
		// are moving left then request a logic update
		if ((dx < 0) && (x < edgeLeft)) {
			swarm.requestChangeDirection();
		}

		// and vice vesa, if we have reached the right hand side of
		// the screen and are moving right, request a logic update
		if ((dx > 0) && (x > edgeRight)) {
			swarm.requestChangeDirection();
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
	 * Notification that this alien has collided with another entity
	 *
	 * @param other The other entity
	 */
	@Override
	public void onCollision(Entity other) {
		// collisions with aliens are handled elsewhere
	}

	@Override
	public Renderer getRenderer() {
		return AlienEntityRenderer.get();
	}
}