package com.gpergrossi.spaceinvaders.ui.screens;

import com.gpergrossi.spaceinvaders.assets.Fonts;
import com.gpergrossi.spaceinvaders.ui.StyledText;
import com.gpergrossi.spaceinvaders.ui.Text;

import java.awt.*;

public class DefeatScreen extends Screen {

    public DefeatScreen() {
        super(false);
    }

    @Override
    public void init(Graphics2D g) {
        this.add(
                new StyledText(0, 250, 800, 100, "Oh no! They got you, try again?", Fonts.get().getLargeFont())
                        .centered().outlined(Color.DARK_GRAY, 4.2f).skewed(0.2f)
        );
        this.add(
                new Text(0, 350, 800, 100, "Press space to continue", Fonts.get().getMediumFont())
                        .centered()
        );
    }
}
