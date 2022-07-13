package com.gpergrossi.spaceinvaders.render;

import com.gpergrossi.spaceinvaders.game.Settings;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Starfield {

    private int width;
    private int height;
    private int depth;
    private int starCount;

    private float cameraX;
    private float cameraY;
    private float parallax;

    private float scroll;
    private float speed;

    private ArrayList<Star> stars = new ArrayList<Star>();

    public Starfield(int width, int height, int depth, int starCount) {
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.starCount = starCount;
        this.stars = new ArrayList<>();
        this.cameraX = width/2;
        this.cameraY = height/2;
        this.parallax = 0.2f;
        this.scroll = 0f;
        this.speed = 30f;
        initStars();
    }

    private void initStars() {
        this.stars.clear();

        for (int i = 0; i < starCount; i++) {
            float z = (1.0f / parallax) + (float) (Math.random() * depth);
            float x = (0.5f * ((float)(Math.random()) * 2.0f - 1.0f) * (parallax * z) + 0.5f) * width;
            float y = (0.5f * ((float)(Math.random()) * 2.0f - 1.0f) * (parallax * z) + 0.5f) * height;
            float size = (float) (Math.random() * 3f + 1.0f);

            float colorShift = (float) Math.random() * 2.0f - 1.0f;

            Color color = (colorShift >= 0f) ?
                new Color(255, 255 - (int) (colorShift*128), 255 - (int) (colorShift*128)) :
                new Color(255 + (int) (colorShift*128), 255 + (int) (colorShift*128), 255);

            Star star = new Star(x, y, z, size, color);
            this.stars.add(star);
        }
    }

    public void update(long deltaMs) {
        float delta = deltaMs / 1000.0f;
        this.scroll += this.speed * delta;
        for (Star s : stars) {
            s.update(deltaMs);
        }
    }

    public void render(Graphics2D g) {
        for (Star s : stars) {
            float projX = s.x - cameraX;
            float projY = s.y - cameraY + scroll;

            float div = 1.0f / (parallax * s.z);
            projX *= div;
            projY *= div;

            projX += cameraX;
            projY += cameraY;

            float size = s.size * div;

            // Make sure stars stay on screen
            projY -= Math.floor(projY / height) * height;

            s.render(g, projX, projY, size);
        }
    }

    private static class Star {

        public float x, y, z;
        public float size;
        public Color color;

        public float twinkle;
        public float twinkleFrequency;
        public float twinklePhase;

        public float time;

        private Ellipse2D.Float shape;

        private Star(float x, float y, float z, float size, Color color) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.size = size;
            this.color = color;

            this.twinkleFrequency = (float) (1.0 / (Math.random() * 10.0 + 10.0));
            this.twinklePhase = (float) Math.random();
            this.time = 0;

            float halfSize = size * 0.5f;
            this.shape = new Ellipse2D.Float(x - halfSize, y - halfSize, size, size);
        }

        public void update(long deltaMs) {
            time += deltaMs / 1000.0f;

            float twinkleTime = time * twinkleFrequency + twinklePhase;
            twinkleTime -= Math.floor(twinkleTime);

            float twinkleK = 40f * (twinkleTime - 0.8f);
            float twinkleFactor = 1f + (float) Math.exp(-twinkleK*twinkleK);

            this.twinkle = twinkleFactor;
        }

        public void render(Graphics2D g, float x, float y, float size) {
            size *= this.twinkle;
            float halfSize = size * 0.5f;
            shape.setFrame(x - halfSize, y - halfSize, size, size);
            g.setColor(color);
            g.fill(shape);
        }
    }

}
