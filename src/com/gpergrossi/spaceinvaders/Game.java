package com.gpergrossi.spaceinvaders;

import com.gpergrossi.spaceinvaders.entity.AlienEntity;
import com.gpergrossi.spaceinvaders.entity.Entity;
import com.gpergrossi.spaceinvaders.entity.ShipEntity;
import com.gpergrossi.spaceinvaders.entity.ShotEntity;

import java.awt.*;
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

	/** The input object through which this game can receive input and register listeners */
	private Input input;

	/** True if the game is currently "running", i.e. the game loop is looping */
	private boolean gameRunning;

	/** The list of all the entities that exist in our game */
	private ArrayList entities;

	/** The list of entities that need to be removed from the game this loop */
	private ArrayList removeList;

	/** The entity representing the player */
	private Entity ship;

	/** The speed at which the player's ship should move (pixels/sec) */
	private double moveSpeed;

	/** The time at which last fired a shot */
	private long lastFire;

	/** The interval between our players shot (ms) */
	private long firingInterval;

	/** The number of aliens left on the screen */
	private int alienCount;
	
	/** The message to display which waiting for a key press */
	private String message;

	/** True if game logic needs to be applied this loop, normally as a result of a game event */
	private boolean logicRequiredThisLoop;

	/** The font used for large text */
	private Font largeFont;

	 /** The font used for small text */
	private Font smallFont;

	/**
	 * Construct our game and set it running.
	 */
	public Game(Input input) {
		this.gameWindow = null; // Will be assigned by the GameWindow via setParent() when the game begins.
		this.input = input;

		this.entities = new ArrayList<>();
		this.removeList = new ArrayList<>();
		this.reset();

		// These will be initialized in the init() method
		this.largeFont = null;
		this.smallFont = null;

		// initialise the entities in our game so there's something to see at startup.
		initEntities();
	}

	public void setParent(GameWindow window) {
		gameWindow = window;
	}

	public void reset() {
		// initialize game state variables
		gameRunning = true;
		moveSpeed = 300;
		lastFire = 0;
		firingInterval = 500;
		alienCount = 0;
		message = "";
		logicRequiredThisLoop = false;

		// clear out any existing entities and initialize a new set
		entities.clear();
		removeList.clear();
		initEntities();

		// blank out any keyboard settings we might currently have
		input.reset();
	}
	
	/**
	 * Initialise the starting state of the entities (ship and aliens). Each
	 * entity will be added to the overall list of entities in the game.
	 */
	private void initEntities() {
		// create the player ship and place it roughly in the center of the screen
		ship = new ShipEntity(this,"sprites/ship.gif",370,550);
		entities.add(ship);
		
		// create a block of aliens (5 rows, by 12 aliens, spaced evenly)
		alienCount = 0;
		for (int row = 0; row < 5; row++) {
			for (int x = 0; x < 12; x++) {
				Entity alien = new AlienEntity(this,"sprites/alien.gif",100+(x*50),(50)+row*30);
				entities.add(alien);
				alienCount++;
			}
		}
	}

	public void init() {
		largeFont = AssetStore.get().getFont("font/SquadaOne-Regular.ttf", Font.PLAIN, 24.0f);
		smallFont = AssetStore.get().getFont("font/SquadaOne-Regular.ttf", Font.PLAIN, 18.0f);

		// The game is now ready, wait for a key press from the user before beginning.
		Runnable callback = () -> { this.reset(); };
		input.waitKey(callback);
	}

	/**
	 * Notification from a game entity that the logic of the game
	 * should be run at the next opportunity (normally as a result of some
	 * game event)
	 */
	public void updateLogic() {
		logicRequiredThisLoop = true;
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
	public void notifyAlienKilled() {
		// reduce the alient count, if there are none left, the player has won!
		alienCount--;
		
		if (alienCount == 0) {
			notifyWin();
		}
		
		// if there are still some aliens left then they all need to get faster, so
		// speed up all the existing aliens
		for (int i = 0; i < entities.size(); i++) {
			Entity entity = (Entity) entities.get(i);
			
			if (entity instanceof AlienEntity) {
				// speed up by 2%
				entity.setHorizontalMovement(entity.getHorizontalMovement() * 1.02);
			}
		}
	}
	
	/**
	 * Attempt to fire a shot from the player. Its called "try"
	 * since we must first check that the player can fire at this 
	 * point, i.e. has he/she waited long enough between shots
	 */
	public void tryToFire() {
		// check that we have waiting long enough to fire
		if (System.currentTimeMillis() - lastFire < firingInterval) {
			return;
		}
		
		// if we waited long enough, create the shot entity, and record the time.
		lastFire = System.currentTimeMillis();
		ShotEntity shot = new ShotEntity(this,"sprites/shot.gif",ship.getX()+10,ship.getY()-30);
		entities.add(shot);
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

		// cycle round asking each entity to move itself
		if (!input.isWaitingForKeyPress()) {
			for (int i = 0; i < entities.size(); i++) {
				Entity entity = (Entity) entities.get(i);
				entity.move(deltaMs);
			}
		}

		// brute force collisions, compare every entity against
		// every other entity. If any of them collide notify
		// both entities that the collision has occured
		for (int p = 0; p < entities.size(); p++) {
			for (int s = p+1; s < entities.size(); s++) {
				Entity me = (Entity) entities.get(p);
				Entity him = (Entity) entities.get(s);

				if (me.collidesWith(him)) {
					me.collidedWith(him);
					him.collidedWith(me);
				}
			}
		}

		// remove any entity that has been marked for clear up
		entities.removeAll(removeList);
		removeList.clear();

		// if a game event has indicated that game logic should
		// be resolved, cycle round every entity requesting that
		// their personal logic should be considered.
		if (logicRequiredThisLoop) {
			for (int i = 0; i < entities.size(); i++) {
				Entity entity = (Entity) entities.get(i);
				entity.doLogic();
			}

			logicRequiredThisLoop = false;
		}

		// resolve the movement of the ship. First assume the ship
		// isn't moving. If either cursor key is pressed then
		// update the movement appropriately
		ship.setHorizontalMovement(0);

		if (input.isLeftPressed() && !input.isRightPressed()) {
			ship.setHorizontalMovement(-moveSpeed);
		} else if (input.isRightPressed() && !input.isLeftPressed()) {
			ship.setHorizontalMovement(moveSpeed);
		}

		// if we're pressing fire, attempt to fire
		if (input.isFirePressed()) {
			tryToFire();
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
			entity.draw(g);
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
