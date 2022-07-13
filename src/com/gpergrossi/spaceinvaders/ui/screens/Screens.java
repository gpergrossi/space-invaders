package com.gpergrossi.spaceinvaders.ui.screens;

import com.gpergrossi.spaceinvaders.game.Game;

public class Screens {

    private static final Screens single = new Screens();

    public static Screens get() { return single; }

    private TitleScreen titleScreen;
    private VictoryScreen victoryScreen;
    private DefeatScreen defeatScreen;
    private PauseScreen pauseScreen;
    private OptionsScreen optionsScreen;

    private Screens() {
        titleScreen = null;
        victoryScreen = null;
        defeatScreen = null;
        pauseScreen = null;
        optionsScreen = null;
    }

    public void load(Game game) {
        titleScreen = new TitleScreen();
        victoryScreen = new VictoryScreen(game);
        defeatScreen = new DefeatScreen(game);
        pauseScreen = new PauseScreen();
        optionsScreen = new OptionsScreen();
    }

    public TitleScreen getTitleScreen() {
        return titleScreen;
    }

    public VictoryScreen getVictoryScreen() {
        return victoryScreen;
    }

    public DefeatScreen getDefeatScreen() {
        return defeatScreen;
    }

    public PauseScreen getPauseScreen() {
        return pauseScreen;
    }

    public OptionsScreen getOptionsScreen() {
        return optionsScreen;
    }
}
