package com.gpergrossi.spaceinvaders.assets;

import java.awt.*;

public class Sprites {

    private static final Sprites single = new Sprites();

    public static Sprites get() { return single; }


    private Sprite shipBaseSprite;
    private Sprite shipMaskSprite;
    private TintedSprite shipSprite;
    private Sprite alienSprite;
    private Sprite shotSprite;

    public Sprites() {
        shipBaseSprite = null;
        shipMaskSprite = null;
        shipSprite = null;
        alienSprite = null;
        shotSprite = null;
    }

    public void load() {
        shipBaseSprite = AssetStore.get().getSprite("sprites/ship.png");
        shipMaskSprite = AssetStore.get().getSprite("sprites/ship-mask.png");
        shipSprite = new TintedSprite(shipBaseSprite, shipMaskSprite, Color.WHITE);
        alienSprite = AssetStore.get().getSprite("sprites/alien.gif");
        shotSprite = AssetStore.get().getSprite("sprites/shot.gif");
    }

    public TintedSprite getShipSprite() {
        return shipSprite;
    }

    public Sprite getAlienSprite() {
        return alienSprite;
    }

    public Sprite getShotSprite() {
        return shotSprite;
    }
}
