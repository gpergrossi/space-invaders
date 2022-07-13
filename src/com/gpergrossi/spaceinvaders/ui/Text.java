package com.gpergrossi.spaceinvaders.ui;

import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Text {

    private int x;
    private int y;
    private int width;
    private int height;
    private String text;
    private Font font;

    public Text(int x, int y, int width, int height, String text, Font font) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.font = font;
    }

    public void render(Graphics2D g) {
        // Foreground color
        g.setColor(Color.WHITE);

        // Draw text
        g.setFont(font);
        int strWidth = g.getFontMetrics().stringWidth(text);
        int ascent = g.getFontMetrics().getAscent();
        g.drawString(text, x + (width - strWidth) * 0.5f, y + ascent);
    }

}
