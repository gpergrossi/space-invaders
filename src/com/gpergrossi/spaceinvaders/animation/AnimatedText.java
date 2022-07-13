package com.gpergrossi.spaceinvaders.animation;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class AnimatedText {

    public static AnimatedText create(Graphics2D g, String text, double x, double y) {
        // Get bounds of text string
        FontMetrics fm = g.getFontMetrics();
        Rectangle2D bounds = fm.getStringBounds(text, 0, text.length(), g);
        double width = bounds.getWidth();
        double height = bounds.getHeight();
        double left = -width/2.0;
        double bottom = fm.getDescent();

        double previousWidth = 0;
        ArrayList<AnimatedCharacter> chars = new ArrayList<>();
        for (int i = 0; i < text.length(); i++) {
            Rectangle2D cBounds = fm.getStringBounds(text, 0, i+1, g);

            double cx = left + previousWidth;
            double cy = bottom;
            double cWidth = cBounds.getWidth() - previousWidth;

            chars.add(new AnimatedCharacter(i, text.charAt(i), cx, cy, cWidth, height, x, y));

            previousWidth = cBounds.getWidth();
        }

        AnimatedCharacter[] charsArray = new AnimatedCharacter[chars.size()];
        charsArray = chars.toArray(charsArray);

        return new AnimatedText(charsArray);
    }

    private static class AnimatedCharacter {
        private int index;
        private char c;
        private Rectangle2D bounds;
        private Point2D stringCenter;
        private TweenStep<Double> animation;
        private double animationTimeOffset;

        public AnimatedCharacter(int index, char c, double x, double y, double width, double height, double stringCenterX, double stringCenterY) {
            this.index = index;
            this.c = c;
            this.bounds = new Rectangle2D.Double(x, y, width, height);
            this.stringCenter = new Point2D.Double(stringCenterX, stringCenterY);
            this.animation = new TweenStep<Double>(5.0, 0.0, 1.0, null, null);

            double time = (index * -0.1) / 5.0;
            time -= Math.floor(time);
            time *= 5.0;

            this.animationTimeOffset = time;
        }

        private void render(Graphics2D g) {
            double t = animation.getValue();
            double tl = Math.sin(t * Math.PI * 2.0);
            double tl2 = Math.sin(2.0 * t * Math.PI * 2.0);

            AffineTransform localRotate = AffineTransform.getRotateInstance(tl2 * -30.0 * Math.PI / 180.0);
            AffineTransform localTranslate = AffineTransform.getTranslateInstance(bounds.getX(), bounds.getY());

            AffineTransform rotate = AffineTransform.getRotateInstance(0.0 * tl * -15.0 * Math.PI / 180.0);
            AffineTransform shear = AffineTransform.getShearInstance(0.0 * tl * 0.2, 0);
            AffineTransform scale = AffineTransform.getScaleInstance(2.0 - tl, 2.0 - tl);
            AffineTransform translate = AffineTransform.getTranslateInstance(stringCenter.getX(), stringCenter.getY());
            float hue = (float) t*2f;

            AffineTransform transform = new AffineTransform();
            transform.concatenate(translate);
            transform.concatenate(rotate);
            transform.concatenate(scale);
            transform.concatenate(shear);
            transform.concatenate(localTranslate);
            transform.concatenate(localRotate);

            g.setColor(new Color(Color.HSBtoRGB(hue, 1f, 1f)));
            g.setTransform(transform);
            g.drawString(""+c, 0f, 0f);
        }

        private void registerAnimations(AnimationSystem animationSystem) {
            animationSystem.start(animation);
            animationSystem.setLooping(animation, true);
            animation.seek(animationTimeOffset);
        }

        private void unregisterAnimations(AnimationSystem animationSystem) {
            animationSystem.remove(animation);
        }
    }

    private String text;
    private ArrayList<AnimatedCharacter> chars;

    protected AnimatedText(AnimatedCharacter... chars) {
        this.text = "";
        this.chars = new ArrayList<>();
        for (int i = 0; i < chars.length; i++) {
            // Render non-space characters
            if (chars[i].c != ' ') {
                this.chars.add(chars[i]);
            }
            text += chars[i].c;
        }
    }

    public void render(Graphics2D g) {
        for (int i = chars.size()-1; i >= 0; i--) {
            AnimatedCharacter c = chars.get(i);
            c.render(g);
        }

        // Reset transform after last character
        g.setTransform(new AffineTransform());
    }

    public void registerAnimations(AnimationSystem animationSystem) {
        for (AnimatedCharacter c : chars) {
            c.registerAnimations(animationSystem);
        }
    }

    public void unregisterAnimations(AnimationSystem animationSystem) {
        for (AnimatedCharacter c : chars) {
            c.unregisterAnimations(animationSystem);
        }
    }

    public String getText() {
        return text;
    }
}
