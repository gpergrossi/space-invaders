package com.gpergrossi.spaceinvaders.assets;

import java.awt.*;

public class Fonts {

    private static Fonts single = new Fonts();
    public static Fonts get() { return single; }

    /** The font used for large text */
    private Font hugeFont;

    /** The font used for large text */
    private Font largeFont;

    /** The font used for large text */
    private Font mediumFont;

    /** The font used for small text */
    private Font smallFont;

    public Fonts() {
        hugeFont = null;
        largeFont = null;
        mediumFont = null;
        smallFont = null;
    }

    public void load() {
        AssetStore assets = AssetStore.get();
        hugeFont = assets.getFont("font/SquadaOne-Regular.ttf", Font.PLAIN, 120.0f);
        largeFont = assets.getFont("font/SquadaOne-Regular.ttf", Font.PLAIN, 64.0f);
        mediumFont = assets.getFont("font/SquadaOne-Regular.ttf", Font.PLAIN, 30.0f);
        smallFont = assets.getFont("font/SquadaOne-Regular.ttf", Font.PLAIN, 20.0f);
    }

    public Font getHugeFont() {
        return hugeFont;
    }

    public Font getLargeFont() {
        return largeFont;
    }

    public Font getMediumFont() {
        return mediumFont;
    }

    public Font getSmallFont() {
        return smallFont;
    }
}
