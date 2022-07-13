package com.gpergrossi.spaceinvaders.game;

import com.gpergrossi.spaceinvaders.assets.*;
import com.gpergrossi.spaceinvaders.render.Starfield;
import com.gpergrossi.spaceinvaders.ui.AnimatedText;
import com.gpergrossi.spaceinvaders.animation.AnimationSystem;
import com.gpergrossi.spaceinvaders.entity.*;
import com.gpergrossi.spaceinvaders.ui.AnimatedTextEffect;
import com.gpergrossi.spaceinvaders.ui.screens.Screen;
import com.gpergrossi.spaceinvaders.ui.screens.Screens;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * The main hook of our game. This class with both act as a manager
 * for the display and central mediator for the game logic. 
 * 
 * Display management will consist of a loop that cycles round all
 * entities in the game asking them to move and then drawing them
 * in the appropriate place. With the help of an inner class it
 * will also allow the player to control the main ship.
 * 
 * As a mediator it will be informed when entities within our game
 * detect events (e.g. alient killed, played died) and will take
 * appropriate game actions.
 * 
 * @author Kevin Glass
 */
public class Game extends Canvas {

	/** The game window object allows us to ask the framerate and change display size */
	private GameWindow gameWindow;

	/** The settings object contains game settings and constants that define graphics and gameplay. */
	private Settings gameSettings;

	/** The statistics used by the scoring system */
	private Statistics scoreStatistics;

	/** The input object through which this game can receive input and register listeners */
	private Input input;

	/** The animation system that handles updating of all animations */
	private AnimationSystem animationSystem;

	/** Which screens are open */
	private ArrayList<Screen> screenStack;

	/** The state of the game */
	private GameState state;

	/** The list of all the entities that exist in our game */
	private ArrayList<Entity> entities;

	/** The list of entities that need to be removed from the game this loop */
	private ArrayList<Entity> removeList;

	/** The entity representing the player */
	private ShipEntity ship;

	/** The speed at which the player's ship should move (pixels/sec) */
	private float moveSpeed;

	/** Keeps track of all alien entities */
	private AlienSwarm alienSwarm;

	/** The background star field */
	private Starfield starfield;


	/**
	 * Construct our game and set it running.
	 */
	public Game(Input input, Settings settings) {
		this.gameWindow = null; // Will be assigned by the GameWindow via setParent() when the game begins.
		this.gameSettings = settings;
		this.scoreStatistics = new Statistics();
		this.input = input;
		this.animationSystem = new AnimationSystem();
		this.screenStack = new ArrayList<>();

		this.entities = new ArrayList<>();
		this.removeList = new ArrayList<>();

		this.alienSwarm = new AlienSwarm();
		this.starfield = new Starfield(settings.getScreenWidth(), settings.getScreenHeight(), 20, 1000);
	}

	public void setParent(GameWindow window) {
		gameWindow = window;
	}

	public Settings getSettings() {
		return gameSettings;
	}


	public void init() {
		// Load fonts
		Fonts.get().load();

		// Load screens
		Screens.get().load();

		// Load sprites
		Sprites.get().load();

		// Start the game in a paused state
		this.enterState(GameState.TITLE_SCREEN);
	}

	public void reset() {
		// Initialize game state variables
		moveSpeed = 300;

		// Clear all scoring statistics
		scoreStatistics.reset();

		// Blank out any keyboard input we might currently have
		input.reset();

		// Clear out any existing entities and initialize a new set
		entities.clear();
		removeList.clear();

		// Clear all animations
		animationSystem.clear();

		// Create entities
		initEntities();
	}

	private void enterState(GameState state) {
		this.state = state;

		switch (state) {
			case TITLE_SCREEN:
				reset();
				screenStack.clear();
				screenStack.add(Screens.get().getTitleScreen());

				// Wait for a key press from the user before beginning.
				input.waitKey(() -> enterState(GameState.INTRO_ANIMATION));
				break;

			case INTRO_ANIMATION:
				screenStack.clear();
				enterState(GameState.GAMEPLAY);
				break;

			case GAMEPLAY:
				break;

			case VICTORY:
				//message = "Well done! You Win!";
				input.waitKey(() -> enterState(GameState.TITLE_SCREEN));
				break;

			case DEATH:
				//message = "Oh no! They got you, try again?";
				input.waitKey(() -> enterState(GameState.TITLE_SCREEN));
				break;
		}
	}

	/**
	 * Initialise the starting state of the entities (ship and aliens). Each
	 * entity will be added to the overall list of entities in the game.
	 */
	private void initEntities() {
		// create the player ship and place it roughly in the center of the screen
		ship = new ShipEntity(this, Sprites.get().getShipSprite(), 370, 550);
		entities.add(ship);

		// Clear the alien swarm (in case there were some alive when the round ended)
		alienSwarm.clear();

		// Create a block of aliens (5 rows, by 12 aliens, spaced evenly)
		for (int row = 0; row < 5; row++) {
			for (int x = 0; x < 12; x++) {
				Entity alien = new AlienEntity(this, alienSwarm, Sprites.get().getAlienSprite(), 100+(x*50), (50)+row*30);
				entities.add(alien);
			}
		}
	}
	
	/**
	 * Remove an entity from the game. The entity removed will
	 * no longer move or be drawn.
	 * 
	 * @param entity The entity that should be removed
	 */
	public void removeEntity(Entity entity) {
		removeList.add(entity);
	}

	/**
	 * Notification that the player has fired a shot.
	 */
	public void notifyPlayerShoot(ShotEntity shot) {
		scoreStatistics.trackShotFired(shot);
	}

	/**
	 * Notification that a shot fired by the player has missed and gone off the top of the screen.
	 */
	public void notifyShotMissed(ShotEntity shot) {
		scoreStatistics.trackShotMissed(shot);
	}

	/**
	 * Notification that a shot fired by the player has hit a valid target.
	 */
	public void notifyShotHit(ShotEntity shot) {
		scoreStatistics.trackShotHit(shot);
	}

	/**
	 * Notification that an alien has been killed
	 */
	public void notifyAlienKilled(AlienEntity alien) {
		// Remove the alien from the swarm
		alienSwarm.removeAlien(alien);

		// If there are none left, the player has won!
		if (alienSwarm.count() == 0) {
			notifyWin();
		}

		// If there are still some aliens left then they all need to get faster, so
		// speed up all the existing aliens
		alien.getSwarm().increaseSpeed(1.02f);
	}

	/**
	 * Notification that the player has won since all the aliens
	 * are dead.
	 */
	public void notifyWin() {
		enterState(GameState.VICTORY);
	}

	/**
	 * Notification that the player has died. 
	 */
	public void notifyDeath() {
		enterState(GameState.DEATH);
	}
	
	/**
	 * Attempt to fire a shot from the player. Its called "try"
	 * since we must first check that the player can fire at this 
	 * point, i.e. has he/she waited long enough between shots
	 */
	public void tryToFire() {
		// Check that we been have waiting long enough to fire
		if (ship.canShoot()) {
			// If we waited long enough, create the shot entity, and record the time.
			ShotEntity shot = new ShotEntity(this, Sprites.get().getShotSprite(), ship.getX()+10, ship.getY()-30);
			entities.add(shot);

			ship.resetShotTimer();

			notifyPlayerShoot(shot);
		}
	}

	/**
	 * The game's update method. This is called repeatedly during
	 * gameplay and is responsible for the following activities:
	 * <p>
	 * - Working out the speed of the game loop to update moves
	 * - Moving the game entities
	 * - Updating game events
	 * - Responding to some forms of input
	 * <p>
	 */
	public void update(long deltaMs) {

		// Allows the input system's wait key callback to run synchronously with the main thread.
		input.checkCallbacks();

		// Update all entities
		if (state == GameState.GAMEPLAY) {
			for (int i = 0; i < entities.size(); i++) {
				Entity entity = (Entity) entities.get(i);
				entity.updateLogic(deltaMs);
				entity.updateAnimation(deltaMs);
			}

			// Update the alien swarm
			alienSwarm.update();

			// Do collision checks
			doCollisions();
		}

		// Remove any entity that has been marked for clean up
		entities.removeAll(removeList);
		removeList.clear();

		// Update animation system
		animationSystem.update(deltaMs);

		// Update star field twinkle
		starfield.update(deltaMs);

		// resolve the movement of the ship. First assume the ship
		// isn't moving. If either cursor key is pressed then
		// update the movement appropriately
		ship.setVelocityX(0);

		if (input.isLeftPressed() && !input.isRightPressed()) {
			ship.setVelocityX(-moveSpeed);
		} else if (input.isRightPressed() && !input.isLeftPressed()) {
			ship.setVelocityX(moveSpeed);
		}

		// if we're pressing fire, attempt to fire
		if (input.isFirePressed()) {
			tryToFire();
		}
	}

	private void doCollisions() {
		// brute force collisions, compare every entity against
		// every other entity. If any of them collide notify
		// both entities that the collision has occurred

		Rectangle2D.Float boundsA = new Rectangle2D.Float();
		Rectangle2D.Float boundsB = new Rectangle2D.Float();

		for (int i = 0; i < entities.size(); i++) {
			for (int j = i+1; j < entities.size(); j++) {
				Entity entityA = (Entity) entities.get(i);
				Entity entityB = (Entity) entities.get(j);

				if ((entityA instanceof PhysicsEntity) && (entityB instanceof PhysicsEntity)) {
					PhysicsEntity physicsA = (PhysicsEntity) entityA;
					PhysicsEntity physicsB = (PhysicsEntity) entityB;

					physicsA.getBounds(boundsA);
					physicsB.getBounds(boundsB);

					if (boundsA.intersects(boundsB)) {
						physicsA.onCollision(physicsB);
						physicsB.onCollision(physicsA);
					}
				}
			}
		}
	}

	private AnimatedText[] textLines = new AnimatedText[2];

	/**
	 * The render method, this is called each frame after update()
	 * it handles the following:
	 * <p>
	 * - Drawing the screen contents (entities, text)
	 * <p>
	 */
	public void render(Graphics2D g) {
		starfield.render(g);

		// cycle round drawing all the entities we have in the game
		for (int i = 0; i < entities.size(); i++) {
			Entity entity = (Entity) entities.get(i);
			entity.render(g);
		}

		// Render all visible screens
		if (screenStack.size() > 0) {
			int firstVisibleScreenIndex = 0;
			for (int i = screenStack.size() - 1; i >= 0; i--) {
				if (!screenStack.get(i).isOverlay()) {
					firstVisibleScreenIndex = i;
					break;
				}
			}
			for (int i = firstVisibleScreenIndex; i < screenStack.size(); i++) {
				screenStack.get(i).render(g);
			}
		}

		/*
		// if we're waiting for an "any key" press then draw the
		// current message
		if (input.isWaitingForKeyPress()) {
			g.setColor(Color.white);

			Font[] lineFont = new Font[] { Fonts.get().getLargeFont(), Fonts.get().getMediumFont() };
			String[] lines = new String[] { message, "Press any key" };
			int[] lineYs = new int[] { 250, 300 };

			for (int i = 0; i < 2; i++) {
				g.setFont(lineFont[i]);

				if (textLines[i] == null || !textLines[i].getText().equals(lines[i])) {
					if (textLines[i] != null) {
						textLines[i].unregisterAnimations(animationSystem);
					}

					String text = lines[i];
					FontMetrics fm = g.getFontMetrics();
					int strWidth = fm.stringWidth(text);
					int strHeight = fm.getHeight();
					int strAscent = fm.getAscent();
					textLines[i] = new AnimatedText(400 - strWidth / 2, lineYs[i] - strAscent, strWidth, strHeight, text, g.getFont(), g, AnimatedTextEffect.DEFAULT);
					textLines[i].registerAnimations(animationSystem);
				}
				if (textLines[i] != null) {
					textLines[i].render(g);
				}
			}
		}
		*/

		// Render statistics
		g.setFont(Fonts.get().getSmallFont());
		g.setColor(Color.white);
		g.drawString("Fired:", 650, 25);
		g.drawString("Hit:", 650, 45);
		g.drawString("Accuracy:", 650, 65);
		g.drawString("Combo:", 650, 85);
		g.drawString("" + scoreStatistics.getShotsFired(), 750, 26);
		g.drawString("" + scoreStatistics.getShotsHit(), 750, 46);
		g.drawString("" + Math.round(scoreStatistics.getAccuracy() * 100f) + "%", 750, 66);
		g.drawString("" + scoreStatistics.getHitCombo(), 750, 86);

		// Render FPS counter
		if (gameWindow != null) {
			g.setFont(Fonts.get().getSmallFont());
			g.setColor(Color.white);
			int fps = (int) (gameWindow.getAverageFrameRate());
			g.drawString("FPS: " + fps, 5, 20);
		}

		g.drawString("State: " + state, 5, 40);
	}

}
