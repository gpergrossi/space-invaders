package com.gpergrossi.spaceinvaders.render;

import com.gpergrossi.spaceinvaders.animation.LerpFunction;
import com.gpergrossi.spaceinvaders.assets.Sprite;
import com.gpergrossi.spaceinvaders.assets.Sprites;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

public class ParticleSystem {

    private int maxParticles;
    private ArrayList<Particle> particles;

    public ParticleSystem(int maxParticles, int maxSpriteParticles) {
        this.maxParticles = maxParticles;
        this.particles = new ArrayList<>();
    }

    public void update(long deltaMs) {
        float timeStep = deltaMs / 1000.0f;

        for (int i = 0; i < particles.size(); i++) {
            Particle p = particles.get(i);
            p.update(timeStep);

            if (p.life <= 0f) {
                particles.remove(i);

                // Next loop we should re-use our current index because we just removed a particle.
                i--;
            }
        }
    }

    public void render(Graphics2D g) {
        for (Particle p : particles) {
            p.render(g);
        }
    }

    private void addParticle(Particle p) {
        if (particles.size() < maxParticles) {
            particles.add(p);
        } else if (Math.random() < 0.5) {
            int i = (int) (Math.random() * maxParticles);
            particles.set(i, p);
        }
    }

    public void spawnBulletTrail(float x, float y) {
        for (int i = 0; i < 3; i++) {
            ColorParticle p = new ColorParticle(x, y, 0.15f);  // Somewhere between orange and yellow

            p.velX = 20f * ((float) Math.random() * 2f - 1f);
            p.velY = 20f * ((float) Math.random());

            p.maxLife = 0.5f;
            p.initialSize = 0.5f;
            p.finalSize = 5f;
            addParticle(p);
        }
    }

    public void spawnBulletHit(float x, float y) {
        for (int i = 0; i < 50; i++) {
            ColorParticle p = new ColorParticle(x, y, 0.05f);  // Somewhere between red and orange

            burstVelocity(p, 20f, 60f);

            p.gravity = 0;
            p.drag = -0.05f;
            p.maxLife = 1f;
            p.initialSize = 0.5f;
            p.finalSize = 5f;
            addParticle(p);
        }
    }

    public void spawnDeadAlien(float x, float y, float velocityX) {
        Sprite alien = Sprites.get().getAlienSprite();
        if (alien == null) return;

        SpriteParticle p = new SpriteParticle(alien, x, y);

        burstVelocity(p, 20f, 60f);
        p.velY -= 20;
        p.velY += velocityX;

        addParticle(p);
    }

    private static void burstVelocity(Particle particle, float minVelocity, float maxVelocity) {
        double angle = Math.random() * Math.PI * 2.0;
        double speed = Math.random() * (maxVelocity - minVelocity) + minVelocity;
        particle.velX = (float) (Math.cos(angle) * speed);
        particle.velY = (float) (Math.sin(angle) * speed);
    }

    public void clear() {
        this.particles.clear();
    }


    private static abstract class Particle {

        public float x, y;
        public float velX, velY;
        public float drag, gravity;
        public float maxLife;

        protected float life;

        boolean initialized;

        private Particle(float x, float y) {
            this.x = x;
            this.y = y;
            this.velX = 0;
            this.velY = 0;
            this.drag = 0.1f;
            this.gravity = 100.0f;
            this.maxLife = 1.0f;

            this.initialized = false;
        }

        public void init() {
            life = maxLife;
        }

        public void update(float timeStep) {
            // Initialize
            if (!initialized) {
                init();
                initialized = true;
            }

            // Motion
            this.x += this.velX * timeStep;
            this.y += this.velY * timeStep;

            // Drag
            this.velX -= this.velX * this.drag * timeStep;
            this.velY -= this.velY * this.drag * timeStep;

            // Gravity
            this.velY += this.gravity * timeStep;

            // Lifetime
            this.life -= timeStep;
            if (life < 0f) life = 0f;
        }

        public abstract void render(Graphics2D g);

    }

    private static class ColorParticle extends Particle {

        public Color initialColor, finalColor;
        public float initialSize, finalSize;

        private Color color;
        private float size;

        private Ellipse2D.Float shape;

        private ColorParticle(float x, float y, float hue) {
            super(x, y);

            float h = (float) Math.random() * 0.1f + hue - 0.05f;
            float s = (float) (float) Math.random() * 0.5f + 0.5f;
            float b = 1.0f;
            this.initialColor = new Color(Color.HSBtoRGB(h, s, b));
            this.finalColor = new Color(initialColor.getRed(), initialColor.getGreen(), initialColor.getBlue(), 0);

            this.initialSize = 0.5f;
            this.finalSize = 1.0f;

            this.shape = new Ellipse2D.Float(0, 0, 1, 1);
        }

        @Override
        public void init() {
            super.init();
            size = initialSize;
            color = initialColor;
        }

        @Override
        public void update(float timeStep) {
            super.update(timeStep);
            float t = 1.0f - (this.life / this.maxLife);
            this.size = initialSize * (1f - t) + finalSize * t;
            this.color = LerpFunction.LERP_RGB.lerp(initialColor, finalColor, t);
        }

        @Override
        public void render(Graphics2D g) {
            float x = this.x - this.size * 0.5f;
            float y = this.y - this.size * 0.5f;
            this.shape.setFrame(x, y, this.size, this.size);

            g.setColor(color);
            g.fill(shape);
        }

    }

    private static class SpriteParticle extends Particle {

        public Sprite sprite;

        public float rotation;

        private float angle;

        private SpriteParticle(Sprite sprite, float x, float y) {
            super(x, y);
            this.sprite = sprite;
            this.rotation = (float) ((Math.PI * (Math.random() * 0.5 + 0.5)) * Math.signum(Math.random() - 0.5f));
        }

        @Override
        public void init() {
            super.init();
            this.angle = 0;
        }

        @Override
        public void update(float timeStep) {
            super.update(timeStep);
            this.angle += this.rotation * timeStep;
        }

        private static final AffineTransform identity = new AffineTransform();

        public void render(Graphics2D g) {
            // Compute transform
            AffineTransform localOffset = AffineTransform.getTranslateInstance(-sprite.getWidth() * 0.5, -sprite.getHeight() * 0.5);
            AffineTransform localRotate = AffineTransform.getRotateInstance(this.angle);
            AffineTransform worldOffset = AffineTransform.getTranslateInstance(x + sprite.getWidth() * 0.5, y + sprite.getHeight() * 0.5);

            AffineTransform transform = new AffineTransform();
            transform.concatenate(worldOffset);
            transform.concatenate(localRotate);
            transform.concatenate(localOffset);
            g.setTransform(transform);

            // Fade out over time
            float alpha = (life / maxLife);
            g.setComposite(AlphaComposite.SrcOver.derive(alpha));

            sprite.draw(g, 0, 0);

            // Reset transform
            g.setTransform(identity);

            // Reset alpha composite
            g.setComposite(AlphaComposite.SrcOver);
        }
    }

}
