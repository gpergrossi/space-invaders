package com.gpergrossi.spaceinvaders.ui.screens;

import com.gpergrossi.spaceinvaders.assets.Fonts;
import com.gpergrossi.spaceinvaders.ui.Button;
import com.gpergrossi.spaceinvaders.ui.StyledText;
import com.gpergrossi.spaceinvaders.ui.Text;

import java.awt.*;

public class TitleScreen extends Screen {

    private Button startButton;

    public TitleScreen() {
        super(false);
    }

    @Override
    public void init(Graphics2D g) {
        this.add(
                new StyledText(0, 100, 800, 100, "Space", Fonts.get().getHugeFont())
                        .centered().outlined(Color.DARK_GRAY, 4.2f).skewed(0.2f)
        );
        this.add(
                new StyledText(0, 200, 800, 100, "Invaders", Fonts.get().getHugeFont())
                        .centered().outlined(Color.DARK_GRAY, 4.2f).skewed(0.2f)
        );

        startButton = new Button(275, 400, 250, 40, "Click to begin", Fonts.get().getMediumFont());
        this.add(startButton);

        this.add(
                new Text(0, 450, 800, 100, "... or press any key", Fonts.get().getSmallFont())
                        .centered()
        );
    }

    public Button getStartButton() {
        return startButton;
    }

}
