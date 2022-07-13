package com.gpergrossi.spaceinvaders.ui.screens;

import com.gpergrossi.spaceinvaders.assets.Fonts;
import com.gpergrossi.spaceinvaders.ui.AnimatedText;
import com.gpergrossi.spaceinvaders.ui.Button;
import com.gpergrossi.spaceinvaders.ui.Text;
import com.gpergrossi.spaceinvaders.ui.texteffects.VictoryTextEffect;

import java.awt.*;

public class VictoryScreen extends Screen {

    public VictoryScreen() {
        super(false);
    }

    private Button playAgainButton;

    public void init(Graphics2D g) {

        AnimatedText victoryMessage = new AnimatedText(
                0, 50, 800, 100, "Well done! You win!",
                Fonts.get().getLargeFont(), g, VictoryTextEffect.get()
        );

        this.add(victoryMessage);

        playAgainButton = new Button(275, 450, 250, 40, "Click to continue", Fonts.get().getMediumFont());
        this.add(playAgainButton);

        this.add(new Text(0, 500, 800, 100, "... or press any key", Fonts.get().getMediumFont()).centered());
    }

    public Button getPlayAgainButton() {
        return playAgainButton;
    }
}
