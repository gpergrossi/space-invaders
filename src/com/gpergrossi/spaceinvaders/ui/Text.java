package com.gpergrossi.spaceinvaders.ui;

import com.gpergrossi.spaceinvaders.animation.AnimationSystem;
import com.gpergrossi.spaceinvaders.game.Input;

import java.awt.*;

public class Text extends Component {

    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected String text;
    protected Font font;
    protected boolean centered;

    public Text(int x, int y, int width, int height, String text, Font font) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.font = font;
        this.centered = false;
    }

    public Text centered() {
        this.centered = true;
        return this;
    }

    @Override
    public void registerInputListeners(Input input) {
        // No input listeners for Text
    }

    @Override
    public void unregisterInputListeners(Input input) {
        // No input listeners for Text
    }

    @Override
    public void registerAnimations(AnimationSystem animationSystem) {
        // No animations for Text
    }

    @Override
    public void unregisterAnimations(AnimationSystem animationSystem) {
        // No animations for Text
    }

    @Override
    public void render(Graphics2D g) {
        g.setFont(font);

        float posX = x;
        float posY = y;

        if (centered) {
            int strWidth = g.getFontMetrics().stringWidth(text);
            int ascent = g.getFontMetrics().getAscent();
            posX = x + (width - strWidth) * 0.5f;
            posY = y + ascent;
        }

        // Foreground color
        g.setColor(Color.WHITE);
        g.drawString(text, posX, posY);
    }

}
