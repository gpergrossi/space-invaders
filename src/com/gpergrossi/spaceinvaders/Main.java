package com.gpergrossi.spaceinvaders;

public class Main {

    /**
     * The entry point into the game. We'll simply create an
     * instance of class which will start the display and game
     * loop.
     *
     * @param argv The arguments that are passed into our game
     */
    public static void main(String argv[]) {
        Input i = new Input();
        Settings s = new Settings();
        Game g = new Game(i, s);

        GameWindow w = new GameWindow("Space Invaders 101", 800, 600, g, i);

        // Start the main game loop, note: this method will not
        // return until the game has finished running. Hence we are
        // using the actual main thread to run the game.
        w.start();
    }

}
