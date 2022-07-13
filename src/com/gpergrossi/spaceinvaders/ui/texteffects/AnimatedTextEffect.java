package com.gpergrossi.spaceinvaders.ui.texteffects;

import com.gpergrossi.spaceinvaders.animation.TweenSequence;

import java.awt.geom.Point2D;

public abstract class AnimatedTextEffect {

    public static final AnimatedTextEffect DEFAULT = new Default();

    public static class TextAnimationsBundle {

        public TweenSequence<Double> characterRotation;
        public TweenSequence<Point2D.Double> characterPosition;

        public TweenSequence<Double> textRotation;
        public TweenSequence<Point2D.Double> textShear;
        public TweenSequence<Point2D.Double> textScale;
        public TweenSequence<Point2D.Double> textPosition;

        public TweenSequence<Double> hue;
        public TweenSequence<Double> saturation;
        public TweenSequence<Double> brightness;
        public TweenSequence<Double> alpha;

        protected TextAnimationsBundle() {
            characterRotation = null;
            characterPosition = null;
            textRotation = null;
            textShear = null;
            textScale = null;
            textPosition = null;
            hue = null;
            saturation = null;
            brightness = null;
            alpha = null;
        }

        public TweenSequence[] getAll() {
            return new TweenSequence[] {
                    characterRotation,
                    characterPosition,
                    textRotation,
                    textShear,
                    textScale,
                    textPosition,
                    hue,
                    saturation,
                    brightness,
                    alpha
            };
        }

    }

    public abstract TextAnimationsBundle getAnimations(String fullText, int index);



    private static final class Default extends AnimatedTextEffect {

        @Override
        public TextAnimationsBundle getAnimations(String fullText, int index) {
            TextAnimationsBundle bundle = new TextAnimationsBundle();

            bundle.characterRotation = null; //TweenSequence.createConstant(0, 0.0);
            bundle.characterPosition = null; //TweenSequence.createConstant(0, new Point2D.Double(0, 0));

            bundle.textRotation = null; //TweenSequence.createConstant(0, 0.0);
            bundle.textShear = null; //TweenSequence.createConstant(0, new Point2D.Double(0, 0));
            bundle.textScale = null; //TweenSequence.createConstant(0, new Point2D.Double(0, 0));
            bundle.textPosition = null; //TweenSequence.createConstant(0, new Point2D.Double(0, 0));

            return bundle;
        }

    }

}
