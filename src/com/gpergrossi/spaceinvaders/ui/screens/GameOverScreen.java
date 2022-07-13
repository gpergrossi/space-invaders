package com.gpergrossi.spaceinvaders.ui.screens;

import com.gpergrossi.spaceinvaders.assets.Fonts;
import com.gpergrossi.spaceinvaders.game.Game;
import com.gpergrossi.spaceinvaders.game.Statistics;
import com.gpergrossi.spaceinvaders.ui.AnimatedText;
import com.gpergrossi.spaceinvaders.ui.Button;
import com.gpergrossi.spaceinvaders.ui.ScoreboardText;
import com.gpergrossi.spaceinvaders.ui.Text;
import com.gpergrossi.spaceinvaders.ui.texteffects.AnimatedTextEffect;
import com.gpergrossi.spaceinvaders.ui.texteffects.VictoryTextEffect;

import java.awt.*;

public class GameOverScreen extends Screen {

    private Game game;
    private String titleMessage;
    private AnimatedTextEffect titleEffect;

    public GameOverScreen(Game game, String titleMessage, AnimatedTextEffect titleEffect) {
        super(false);
        this.game = game;
        this.titleMessage = titleMessage;
        this.titleEffect = titleEffect;
    }

    private Button playAgainButton;

    public void init(Graphics2D g) {
        // Victory message
        AnimatedText victoryMessage = new AnimatedText(
                0, 60, 800, 100, titleMessage,
                Fonts.get().getLargeFont(), g, titleEffect
        );
        this.add(victoryMessage);

        final Statistics stats = game.getScoreStatistics();

        // Score readouts
        ScoreboardText shotsFiredText = new ScoreboardText(
                200, 220, 400, 50, 250,
                "Shots Fired:", "%.0f", () -> stats.getShotsFired(),
                Fonts.get().getMediumFont(), 1
        );
        this.add(shotsFiredText);

        ScoreboardText enemiesKilledText = new ScoreboardText(
                200, 270, 400, 50, 250,
                "Enemies Killed:", "%.0f", () -> stats.getShotsHit(),
                Fonts.get().getMediumFont(), 2
        );
        this.add(enemiesKilledText);

        ScoreboardText accuracyText = new ScoreboardText(
                200, 320, 400, 50, 250,
                "Accuracy:", "%.01f%%", () -> (100f * stats.getAccuracy()),
                Fonts.get().getMediumFont(), 3
        );
        this.add(accuracyText);

        ScoreboardText highestComboText = new ScoreboardText(
                200, 370, 400, 50, 250,
                "Highest Combo:", "%.0f", () -> stats.getMaxHitCombo(),
                Fonts.get().getMediumFont(), 4
        );
        this.add(highestComboText);


        // Bottom button and "press any key" text
        playAgainButton = new Button(275, 450, 250, 40, "Click to continue", Fonts.get().getMediumFont());
        this.add(playAgainButton);

        this.add(new Text(0, 500, 800, 100, "... or press any key", Fonts.get().getSmallFont()).centered());
    }

    public Button getPlayAgainButton() {
        return playAgainButton;
    }
}
