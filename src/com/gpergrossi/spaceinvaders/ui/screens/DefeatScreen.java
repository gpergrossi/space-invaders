package com.gpergrossi.spaceinvaders.ui.screens;

import com.gpergrossi.spaceinvaders.game.Game;
import com.gpergrossi.spaceinvaders.ui.texteffects.DefeatTextEffect;

public class DefeatScreen extends GameOverScreen {

    public DefeatScreen(Game game) {
        super(game, "Oh no! They got you, try again?", DefeatTextEffect.get());
    }

}
