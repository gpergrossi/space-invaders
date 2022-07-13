package com.gpergrossi.spaceinvaders.ui;

import com.gpergrossi.spaceinvaders.animation.AnimationSystem;
import com.gpergrossi.spaceinvaders.animation.TweenSequence;
import com.gpergrossi.spaceinvaders.animation.TweenStep;
import com.gpergrossi.spaceinvaders.game.Input;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class AnimatedText extends Text {

    private static ArrayList<AnimatedTextCharacter> createCharacters(Graphics2D g, String text, double centerX, double centerY, AnimatedTextEffect effect) {
        // Get bounds of text string
        FontMetrics fm = g.getFontMetrics();
        Rectangle2D bounds = fm.getStringBounds(text, 0, text.length(), g);
        double width = bounds.getWidth();
        double height = bounds.getHeight();
        double left = -width/2.0;
        double bottom = fm.getDescent();

        double previousWidth = 0;
        ArrayList<AnimatedTextCharacter> chars = new ArrayList<>();
        for (int i = 0; i < text.length(); i++) {
            Rectangle2D cBounds = fm.getStringBounds(text, 0, i+1, g);

            double cx = left + previousWidth;
            double cy = bottom;
            double cWidth = cBounds.getWidth() - previousWidth;

            chars.add(new AnimatedTextCharacter(text, i, cx, cy, cWidth, height, centerX, centerY, effect));

            previousWidth = cBounds.getWidth();
        }

        return chars;
    }



    private ArrayList<AnimatedTextCharacter> chars;

    public AnimatedText(int x, int y, int width, int height, String text, Font font, Graphics2D graphics, AnimatedTextEffect effect) {
        super(x, y, width, height, text, font);

        this.chars = createCharacters(graphics, text, x + width/2, y + height/2, effect);
    }

    @Override
    public void registerInputListeners(Input input) {
        super.registerInputListeners(input);
        // No input listeners for AnimatedText
    }

    @Override
    public void unregisterInputListeners(Input input) {
        super.unregisterInputListeners(input);
        // No input listeners for Text
    }

    @Override
    public void registerAnimations(AnimationSystem animationSystem) {
        super.registerAnimations(animationSystem);
        for (AnimatedTextCharacter c : chars) {
            c.registerAnimations(animationSystem);
        }
    }

    @Override
    public void unregisterAnimations(AnimationSystem animationSystem) {
        super.unregisterAnimations(animationSystem);
        for (AnimatedTextCharacter c : chars) {
            c.unregisterAnimations(animationSystem);
        }
    }

    @Override
    public void render(Graphics2D g) {
        for (int i = chars.size()-1; i >= 0; i--) {
            AnimatedTextCharacter c = chars.get(i);
            c.render(g);
        }

        // Reset transform after last character
        g.setTransform(new AffineTransform());
    }

    public String getText() {
        return text;
    }






    private static final class AnimatedTextCharacter {
        public String fullText;
        public int index;
        public char c;
        public Rectangle2D bounds;
        public Point2D stringCenter;
        public AnimatedTextEffect.TextAnimationsBundle animations;

        private AnimatedTextCharacter(String fullText, int index, double x, double y, double width, double height, double stringCenterX, double stringCenterY, AnimatedTextEffect effect) {
            this.fullText = fullText;
            this.index = index;
            this.c = fullText.charAt(index);
            this.bounds = new Rectangle2D.Double(x, y, width, height);
            this.stringCenter = new Point2D.Double(stringCenterX, stringCenterY);
            this.animations = effect.getAnimations(fullText, index);
        }

        private void render(Graphics2D g) {

            // Lookup animation values
            Point2D.Double vectorZero = new Point2D.Double(0, 0);
            Point2D.Double vectorOne = new Point2D.Double(1, 1);

            double characterRotation = getWithDefault(animations.characterRotation, 0.0);
            Point2D.Double characterPosition = getWithDefault(animations.characterPosition, vectorZero);

            double textRotation = getWithDefault(animations.textRotation, 0.0);
            Point2D.Double textShear = getWithDefault(animations.textShear, vectorZero);
            Point2D.Double textScale = getWithDefault(animations.textScale, vectorOne);
            Point2D.Double textPosition = getWithDefault(animations.textPosition, vectorZero);

            float hue   = (float) (double) getWithDefault(animations.hue, 0.0);
            float sat   = (float) (double) getWithDefault(animations.saturation, 0.0);
            float bri   = (float) (double) getWithDefault(animations.brightness, 1.0);
            float alpha = (float) (double) getWithDefault(animations.alpha, 1.0);

            // Create transform
            AffineTransform localRotate = AffineTransform.getRotateInstance(characterRotation);
            AffineTransform localTranslate = AffineTransform.getTranslateInstance(bounds.getX() + characterPosition.getX(), bounds.getY() + characterPosition.getY());

            AffineTransform rotate = AffineTransform.getRotateInstance(textRotation);
            AffineTransform shear = AffineTransform.getShearInstance(textShear.getX(), textShear.getY());
            AffineTransform scale = AffineTransform.getScaleInstance(textScale.getX(), textScale.getY());
            AffineTransform translate = AffineTransform.getTranslateInstance(stringCenter.getX() + textPosition.getX(), stringCenter.getY() + textPosition.getY());

            AffineTransform transform = new AffineTransform();
            transform.concatenate(translate);
            transform.concatenate(rotate);
            transform.concatenate(scale);
            transform.concatenate(shear);
            transform.concatenate(localTranslate);
            transform.concatenate(localRotate);
            g.setTransform(transform);

            // Create color
            int rgb = Color.HSBtoRGB(hue, sat, bri);
            int a = (int) (255 * Math.min(Math.max(alpha, 0f), 1f));
            Color color = new Color(a << 24 | rgb);
            g.setColor(color);

            // Draw the character
            g.drawString(""+c, 0f, 0f);
        }

        private static <T> T getWithDefault(TweenSequence<T> seq, T defaultValue) {
            return (seq == null) ? defaultValue : seq.getValue();
        }

        private void registerAnimations(AnimationSystem animationSystem) {
            for (TweenSequence seq : animations.getAll()) {
                if (seq != null) {
                    animationSystem.start(seq, seq.isLooping(), seq.getDefaultStartTime(), null);
                }
            }
        }

        private void unregisterAnimations(AnimationSystem animationSystem) {
            for (TweenSequence seq : animations.getAll()) {
                if (seq != null) {
                    animationSystem.remove(seq);
                }
            }
        }
    }
}
