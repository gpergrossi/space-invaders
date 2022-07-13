package com.gpergrossi.spaceinvaders.ui.screens;

public class Screens {

    private static final Screens single = new Screens();

    public static Screens get() { return single; }

    private TitleScreen titleScreen;

    private Screens() {
        titleScreen = null;
    }

    public void load() {
        titleScreen = new TitleScreen();
    }

    public TitleScreen getTitleScreen() {
        return titleScreen;
    }
}
