package com.gpergrossi.spaceinvaders.ui.screens;

import com.gpergrossi.spaceinvaders.assets.Fonts;
import com.gpergrossi.spaceinvaders.ui.StyledText;
import com.gpergrossi.spaceinvaders.ui.Text;

import java.awt.*;

public class TitleScreen extends Screen {

    public TitleScreen() {
        super(false);
        createComponents();
    }

    private void createComponents() {

        this.add(
                new StyledText(0, 100, 800, 100, "Space", Fonts.get().getHugeFont())
                        .centered().outlined(Color.DARK_GRAY, 4.2f).skewed(0.2f)
        );
        this.add(
                new StyledText(0, 200, 800, 100, "Invaders", Fonts.get().getHugeFont())
                        .centered().outlined(Color.DARK_GRAY, 4.2f).skewed(0.2f)
        );
        this.add(
                new Text(0, 400, 800, 100, "Press space to begin", Fonts.get().getMediumFont())
                        .centered()
        );

    }

}
