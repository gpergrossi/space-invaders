package com.gpergrossi.spaceinvaders.ui;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class StyledText extends Text {

    protected Color color;
    protected boolean outline;
    protected Color outlineColor;
    protected float outlineThickness;
    protected float skew;

    public StyledText(int x, int y, int width, int height, String text, Font font) {
        super(x, y, width, height, text, font);
        this.color = Color.WHITE;
        this.outline = false;
        this.outlineColor = Color.BLACK;
        this.outlineThickness = 5;
        this.skew = 0;
    }

    public Text color(Color color) {
        this.color = color;
        return this;
    }

    @Override
    public StyledText centered() {
        return (StyledText) super.centered();
    }

    public StyledText outlined(Color color, float thickness) {
        this.outline = true;
        this.outlineColor = color;
        this.outlineThickness = thickness;
        return this;
    }

    public StyledText skewed(float skew) {
        this.skew = skew;
        return this;
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

        AffineTransform skewTransform = AffineTransform.getShearInstance(-skew, 0);
        AffineTransform translation = AffineTransform.getTranslateInstance(posX, posY);
        AffineTransform transform = new AffineTransform();
        transform.concatenate(translation);
        transform.concatenate(skewTransform);
        g.setTransform(transform);

        if (outline) {
            g.setColor(outlineColor);
            g.drawString(text, -outlineThickness, -outlineThickness);
            g.drawString(text, -outlineThickness, 0);
            g.drawString(text, -outlineThickness, outlineThickness);
            g.drawString(text, 0, -outlineThickness);
            g.drawString(text, 0, outlineThickness);
            g.drawString(text, outlineThickness, -outlineThickness);
            g.drawString(text, outlineThickness, 0);
            g.drawString(text, outlineThickness, outlineThickness);
        }

        // Foreground color
        g.setColor(color);
        g.drawString(text, 0, 0);

        // Reset transform
        g.setTransform(new AffineTransform());
    }

}
