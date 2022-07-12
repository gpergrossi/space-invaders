package com.gpergrossi.spaceinvaders.game;

import com.gpergrossi.spaceinvaders.assets.AssetStore;
import com.gpergrossi.spaceinvaders.assets.Sprite;
import com.gpergrossi.spaceinvaders.assets.TintedSprite;
import com.gpergrossi.spaceinvaders.entity.*;

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

	/** The input object through which this game can receive input and register listeners */
	private Input input;

	/** True if the game is currently "running", i.e. the game loop is looping */
	private boolean gameRunning;

	/** The list of all the entities that exist in our game */
	private ArrayList<Entity> entities;

	/** The list of entities that need to be removed from the game this loop */
	private ArrayList<Entity> removeList;

	/** The sprite used for rendering the player's ship */
	private TintedSprite shipSprite;

	/** The sprite used for rendering the enemies */
	private Sprite alienSprite;

	/** The entity representing the player */
	private ShipEntity ship;

	/** The speed at which the player's ship should move (pixels/sec) */
	private float moveSpeed;

	/** The number of aliens left on the screen */
	private AlienSwarm alienSwarm;
	
	/** The message to display which waiting for a key press */
	private String message;

	/** The font used for large text */
	private Font largeFont;

	 /** The font used for small text */
	private Font smallFont;

	/**
	 * Construct our game and set it running.
	 */
	public Game(Input input, Settings settings) {
		this.gameWindow = null; // Will be assigned by the GameWindow via setParent() when the game begins.
		this.gameSettings = settings;
		this.input = input;
		this.gameRunning = true;

		this.entities = new ArrayList<>();
		this.removeList = new ArrayList<>();

		alienSwarm = new AlienSwarm();

		// These will be initialized in the init() method
		this.largeFont = null;
		this.smallFont = null;
	}

	public void setParent(GameWindow window) {
		gameWindow = window;
	}

	public Settings getSettings() {
		return gameSettings;
	}


	public void init() {
		// Load fonts
		largeFont = AssetStore.get().getFont("font/SquadaOne-Regular.ttf", Font.PLAIN, 24.0f);
		smallFont = AssetStore.get().getFont("font/SquadaOne-Regular.ttf", Font.PLAIN, 18.0f);

		// Load ship sprite
		Sprite shipBaseSprite = AssetStore.get().getSprite("sprites/ship.png");
		Sprite shipMaskSprite = AssetStore.get().getSprite("sprites/ship-mask.png");
		shipSprite = new TintedSprite(shipBaseSprite, shipMaskSprite, Color.RED);

		// Load alien sprite
		alienSprite = AssetStore.get().getSprite("sprites/alien.gif");

		// Start the game in a paused state
		this.reset();

		// The game is now ready, wait for a key press from the user before beginning.
		input.waitKey();
	}

	public void reset() {
		// Initialize game state variables
		moveSpeed = 300;
		alienSwarm.clear();
		message = "";

		// Blank out any keyboard input we might currently have
		input.reset();

		// Clear out any existing entities and initialize a new set
		entities.clear();
		removeList.clear();

		// Create entities
		initEntities();
	}
	
	/**
	 * Initialise the starting state of the entities (ship and aliens). Each
	 * entity will be added to the overall list of entities in the game.
	 */
	private void initEntities() {
		// create the player ship and place it roughly in the center of the screen
		ship = new ShipEntity(this, shipSprite, 370, 550);
		entities.add(ship);

		// Clear the alien swarm (in case there were some alive when the round ended)
		alienSwarm.clear();

		// Create a block of aliens (5 rows, by 12 aliens, spaced evenly)
		for (int row = 0; row < 5; row++) {
			for (int x = 0; x < 12; x++) {
				Entity alien = new AlienEntity(this, alienSwarm, alienSprite, 100+(x*50), (50)+row*30);
				entities.add(alien);
			}
		}
		System.out.println("Aliens to start: " + alienSwarm.count());
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
	 * Notification that the player has died. 
	 */
	public void notifyDeath() {
		message = "Oh no! They got you, try again?";

		Runnable callback = () -> { this.reset(); };
		input.waitKey(callback);
	}
	
	/**
	 * Notification that the player has won since all the aliens
	 * are dead.
	 */
	public void notifyWin() {
		message = "Well done! You Win!";

		Runnable callback = () -> { this.reset(); };
		input.waitKey(callback);
	}
	
	/**
	 * Notification that an alien has been killed
	 */
	public void notifyAlienKilled(AlienEntity alien) {
		// Remove the alien from the swarm
		alienSwarm.removeAlien(alien);

		System.out.println("Alien killed: " + alienSwarm.count());

		// If there are none left, the player has won!
		if (alienSwarm.count() == 0) {
			notifyWin();
		}

		// If there are still some aliens left then they all need to get faster, so
		// speed up all the existing aliens
		alien.getSwarm().increaseSpeed(1.02f);
	}
	
	/**
	 * Attempt to fire a shot from the player. Its called "try"
	 * since we must first check that the player can fire at this 
	 * point, i.e. has he/she waited long enough between shots
	 */
	public void tryToFire() {
		// Check that we been have waiting long enough to fire
		if (ship.canShoot()) {
			Sprite shotSprite = AssetStore.get().getSprite("sprites/shot.gif");

			// if we waited long enough, create the shot entity, and record the time.
			ShotEntity shot = new ShotEntity(this, shotSprite, ship.getX()+10, ship.getY()-30);
			entities.add(shot);

			ship.resetShotTimer();
		}
	}

	/**
	 * @return true if the game is still running, false if it is not and the window should be closed.
	 */
	public boolean isRunning() {
		return gameRunning;
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

		// Update all entities
		if (!input.isWaitingForKeyPress()) {
			for (int i = 0; i < entities.size(); i++) {
				Entity entity = (Entity) entities.get(i);
				entity.updateLogic(deltaMs);
				entity.updateAnimation(deltaMs);
			}
		}

		// Update the alien swarm
		alienSwarm.update();

		// Do collision checks
		doCollisions();

		// Remove any entity that has been marked for clean up
		entities.removeAll(removeList);
		removeList.clear();

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

	/**
	 * The render method, this is called each frame after update()
	 * it handles the following:
	 * <p>
	 * - Drawing the screen contents (entities, text)
	 * <p>
	 */
	public void render(Graphics2D g) {
		// cycle round drawing all the entities we have in the game
		for (int i = 0; i < entities.size(); i++) {
			Entity entity = (Entity) entities.get(i);
			entity.render(g);
		}

		// if we're waiting for an "any key" press then draw the
		// current message
		if (input.isWaitingForKeyPress()) {
			g.setFont(largeFont);
			g.setColor(Color.white);
			g.drawString(message,(800-g.getFontMetrics().stringWidth(message))/2,250);
			g.drawString("Press any key",(800-g.getFontMetrics().stringWidth("Press any key"))/2,300);
		}

		// Render FPS counter
		if (gameWindow != null) {
			int fps = (int) (gameWindow.getAverageFrameRate());
			g.setFont(smallFont);
			g.setColor(Color.white);
			g.drawString("FPS: " + fps, 730, 20);
		}
	}

}
