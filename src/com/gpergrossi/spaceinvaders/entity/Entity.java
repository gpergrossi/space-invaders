package com.gpergrossi.spaceinvaders.entity;

import com.gpergrossi.spaceinvaders.Game;
import com.gpergrossi.spaceinvaders.render.Renderer;

import java.awt.*;

/**
 * An entity represents any element that appears in the game.
 * The entity class serves as a base class for all entities
 * and provides the following interface:
 *   1. A protected Game object member that the entity can use to communicate with the game and other entities.
 *   2. An updateLogic() method that is called each frame to allow the entity to process logic updates.
 *   3. An updateAnimation() method that is called each frame to allow the entity to process visual updates.
 *   4. A getRenderer() method that returns the renderer for this entity.
 */
public abstract class Entity {

	/** The game in which this entity exists. Used mostly for sending events. */
	protected Game game;

	/**
	 * Construct a entity belonging to a specified Game object.
	 */
	public Entity(Game game) {
		this.game = game;
	}

	/**
	 * Process any logical updates associated with this entity.
	 * This method will not be called while the game is paused.
	 *
	 * @param delta The amount of time that has passed in milliseconds
	 */
	public abstract void updateLogic(long delta);

	/**
	 * Process any updates associated with this entity's visual animation only.
	 * This method is called even when the game is paused.
	 *
	 * @param delta The amount of time that has passed in milliseconds
	 */
	public abstract void updateAnimation(long delta);

	/**
	 * Get the renderer that can render this entity.
	 * @return The renderer associated with this entity.
	 */
	public abstract Renderer getRenderer();

	/**
	 * A render method that handles all rendering of all entities via the
	 * Renderer object returned by their getRenderer() method.
	 *
	 * @param g A Graphics2D object.
	 */
	public final void render(Graphics2D g) {
		this.getRenderer().render(g, this);
	}

}