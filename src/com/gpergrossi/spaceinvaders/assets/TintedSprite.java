package com.gpergrossi.spaceinvaders.assets;

import java.awt.*;
import java.awt.image.BufferedImage;

public class TintedSprite extends Sprite {

    private BufferedImage mask;
    private BufferedImage colorLayer;
    private Color color;

    private int[] pixelBuffer;

    public TintedSprite(Sprite imageSprite, Sprite maskSprite, Color color) {
        super(imageSprite.image);
        this.mask = maskSprite.image;
        this.setColor(color);
    }

    private void setColor(Color color) {
        // If no change, don't do anything
        if (this.color != null && this.color.equals(color)) return;

        this.color = color;

        int width = mask.getWidth();
        int height = mask.getHeight();
        int pixelCount = width*height;

        // Create pixel buffer if needed
        if (pixelBuffer == null || pixelBuffer.length < pixelCount) {
            pixelBuffer = new int[pixelCount];
        }

        // Compute components for tint color
        float tintR = ((float) color.getRed()) / 255.0f;
        float tintG = ((float) color.getGreen()) / 255.0f;
        float tintB = ((float) color.getBlue()) / 255.0f;

        // Get pixel data from mask image
        pixelBuffer = mask.getRGB(0, 0, width, height, pixelBuffer, 0, width);

        // Apply a tint to each pixel
        int index = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Extract ARGB channels
                int pixelA = (pixelBuffer[index] >> 24) & 0xFF;
                int pixelR = (pixelBuffer[index] >> 16) & 0xFF;
                int pixelG = (pixelBuffer[index] >>  8) & 0xFF;
                int pixelB = (pixelBuffer[index] >>  0) & 0xFF;

                // Apply tint
                pixelR = (int) (pixelR * tintR);
                pixelG = (int) (pixelG * tintG);
                pixelB = (int) (pixelB * tintB);

                // Recombine channels
                pixelBuffer[index] = (pixelA << 24) | (pixelR << 16) | (pixelG << 8) | pixelB;

                index++;
            }
        }

        // Create color layer image if needed
        if (colorLayer == null || colorLayer.getWidth() != width || colorLayer.getHeight() != height) {
            colorLayer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        }

        // Copy pixel data into it
        colorLayer.setRGB(0, 0, width, height, pixelBuffer, 0, width);
    }

    /**
     * Draw the sprite onto the graphics context provided
     *
     * @param g The graphics context on which to draw the sprite
     * @param x The x location at which to draw the sprite
     * @param y The y location at which to draw the sprite
     */
    @Override
    public void draw(Graphics2D g, int x, int y) {
        g.drawImage(image, x, y, null);
        g.drawImage(colorLayer, x, y, null);
    }

}
