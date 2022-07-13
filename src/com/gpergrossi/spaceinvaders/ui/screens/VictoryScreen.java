package com.gpergrossi.spaceinvaders.ui.screens;

import com.gpergrossi.spaceinvaders.game.Game;
import com.gpergrossi.spaceinvaders.ui.texteffects.VictoryTextEffect;

public class VictoryScreen extends GameOverScreen {

    public VictoryScreen(Game game) {
        super(game, "Well done! You win!", VictoryTextEffect.get());
    }

}
