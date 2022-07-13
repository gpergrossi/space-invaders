package com.gpergrossi.spaceinvaders.ui.screens;

import com.gpergrossi.spaceinvaders.assets.Fonts;
import com.gpergrossi.spaceinvaders.ui.Button;
import com.gpergrossi.spaceinvaders.ui.StyledText;

import java.awt.*;

public class OptionsScreen extends Screen {

    public OptionsScreen() {
        super(false);
    }

    protected Button returnButton;

    @Override
    public void init(Graphics2D g) {
        this.add(
                new StyledText(0, 100, 800, 100, "OPTIONS", Fonts.get().getHugeFont())
                        .centered().outlined(Color.DARK_GRAY, 4.2f).skewed(0.2f)
        );

        returnButton = new Button(275, 450, 250, 40, "RETURN", Fonts.get().getMediumFont());

        this.add(returnButton);
    }

    public Button getReturnButton() {
        return returnButton;
    }
}
