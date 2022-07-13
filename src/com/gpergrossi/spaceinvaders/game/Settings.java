package com.gpergrossi.spaceinvaders.game;

public class Settings {

    private int screenWidth = 800;
    private int screenHeight = 600;

    private int alienBehaviorEdgeWidth = 10;
    private int deathZoneHeight = 10;
    private int alienDropIncrement = 10;

    private int playerReloadTime = 500; // Half a second

    private boolean showDebugInfo = false;


    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public int getAlienBehaviorEdgeWidth() {
        return alienBehaviorEdgeWidth;
    }

    public int getDeathZoneHeight() {
        return deathZoneHeight;
    }

    public int getAlienDropIncrement() {
        return alienDropIncrement;
    }

    public int getPlayerReloadTime() { return playerReloadTime; }

    public boolean getShowDebugInfo() { return showDebugInfo; }
}
