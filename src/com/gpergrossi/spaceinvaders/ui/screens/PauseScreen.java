package com.gpergrossi.spaceinvaders.ui.screens;

import com.gpergrossi.spaceinvaders.assets.Fonts;
import com.gpergrossi.spaceinvaders.ui.Button;
import com.gpergrossi.spaceinvaders.ui.StyledText;

import java.awt.*;

public class PauseScreen extends Screen {

    public PauseScreen() {
        super(false);
    }

    protected Button resumeButton;
    protected Button optionsButton;
    protected Button exitButton;

    @Override
    public void init(Graphics2D g) {
        this.add(
                new StyledText(0, 150, 800, 100, "- PAUSED -", Fonts.get().getHugeFont())
                        .centered().outlined(Color.DARK_GRAY, 4.2f).skewed(0.2f)
        );

        resumeButton = new Button(275, 300, 250, 40, "RESUME", Fonts.get().getMediumFont());
        optionsButton = new Button(275, 350, 250, 40, "OPTIONS", Fonts.get().getMediumFont());
        exitButton = new Button(275, 400, 250, 40, "EXIT THE GAME", Fonts.get().getMediumFont());

        this.add(resumeButton);
        this.add(optionsButton);
        this.add(exitButton);
    }

    public Button getResumeButton() {
        return resumeButton;
    }

    public Button getOptionsButton() {
        return optionsButton;
    }

    public Button getExitButton() {
        return exitButton;
    }

}
