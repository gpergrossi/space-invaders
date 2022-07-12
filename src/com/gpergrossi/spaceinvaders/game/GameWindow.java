package com.gpergrossi.spaceinvaders.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;

/**
 * This class will handle setting up the Window and Canvas within,
 * the input handler, and graphics low level configuration.
 *
 * With these details organized into their own class, the Game class
 * can concern itself only with game details instead of a mixture
 * of game details and I/O details.
 */
public class GameWindow extends Canvas {

    /** Width of the display area */
    private int width;

    /** Height of the display area */
    private int height;

    /** The game object that handles the core game loop and behaviors */
    private Game game;

    /** The input object that handles mouse and keyboard input */
    private Input input;

    /** The strategy that allows us to use accelerate page flipping */
    private BufferStrategy strategy;

    private final double targetFPS = 60.0;
    private final int targetFrameTimeMs = (int) (1000 / targetFPS);
    private final long targetFrameTimeNs = (long) (1000000000L / targetFPS);
    private final int MINIMUM_YIELD_TIME_NS = 1000000; // 1 ms minimum wait time for call to Thread.yield

    private long lastLoopTimeMs;
    private long lastLoopTimeNs;

    /** Tracks the FPS of the game */
    private double averageFrameRate = targetFPS; // Initial estimate so the rolling average starts somewhere other than 0.

    /**
     * Construct our game and set it running.
     */
    public GameWindow(String title, int width, int height, Game game, Input input) {

        this.width = width;
        this.height = height;
        this.game = game;
        this.input = input;

        // create a frame to contain our game
        JFrame container = new JFrame(title);

        // get hold the content of the frame and set up the resolution of the game
        JPanel panel = (JPanel) container.getContentPane();
        panel.setPreferredSize(new Dimension(width,height));
        panel.setLayout(null);

        // setup our canvas size and put it into the content of the frame
        setBounds(0,0,width,height);
        panel.add(this);

        // Tell AWT not to bother repainting our canvas since we're
        // going to do that our self in accelerated mode
        setIgnoreRepaint(true);

        // finally make the window visible
        container.pack();
        container.setResizable(false);
        container.setVisible(true);

        // add a listener to respond to the user closing the window. If they
        // do we'd like to exit the game
        container.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        // add a key input system to our canvas so we can respond to key pressed
        addKeyListener(input.getKeyListener());

        // add a mouse input system to our canvas so we can respond to mouse events
        addMouseListener(input.getMouseListener());
        addMouseMotionListener(input.getMouseMotionListener());
        addMouseWheelListener(input.getMouseWheelListener());

        // request the focus so key events come to us
        requestFocus();

        // create the buffering strategy which will allow AWT
        // to manage our accelerated graphics
        createBufferStrategy(2);
        strategy = getBufferStrategy();

        this.game.setParent(this);
    }

    public void start() {
        lastLoopTimeMs = System.currentTimeMillis();
        lastLoopTimeNs = System.nanoTime();

        // Main game loop
        game.init();

        while (game.isRunning()) {

            // work out how long its been since the last update, this
            // will be used to calculate how far the entities should
            // move this loop
            long deltaMs = targetFrameTimeMs; //System.currentTimeMillis() - lastLoopTimeMs;

            // Record the starting time for this loop
            lastLoopTimeMs = System.currentTimeMillis();
            lastLoopTimeNs = System.nanoTime();

            // Track framerate if it isn't going to be infinity
            if (deltaMs > 0) {
                // FPS = 1000 ms / frame time (ms)
                double frameRate = 1000.0 / deltaMs;

                // Decent running average over the last roughly 20 frames.
                averageFrameRate = averageFrameRate * 0.95 + frameRate * 0.05;
            }

            // Update the game state
            game.update(deltaMs);

            // Get hold of a graphics context for the accelerated surface
            Graphics2D g = (Graphics2D) strategy.getDrawGraphics();

            // Tell the graphics system to draw with anti-aliasing (for smooth font rendering)
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Clear the screen
            g.setColor(Color.black);
            g.fillRect(0, 0, width, height);

            // Render the game
            game.render(g);

            // Finally, we've completed drawing so clear up the graphics and flip the buffer over
            g.dispose();
            strategy.show();

            // Pause now to give the CPU a rest and hold steady the target FPS.
            while (true) {
                // Precise timing for more consistent frame rate
                long frameTimeNs = (System.nanoTime() - lastLoopTimeNs);

                if (frameTimeNs >= targetFrameTimeNs) {
                    // Time is up, break out of the waiting loop
                    break;
                } else if ((frameTimeNs - targetFrameTimeNs) > MINIMUM_YIELD_TIME_NS) {
                    // We have a enough time to yield the thread
                    Thread.yield();
                } else {
                    // Busy waiting
                }
            }
        }
    }

    public double getAverageFrameRate() {
        return averageFrameRate;
    }

}
