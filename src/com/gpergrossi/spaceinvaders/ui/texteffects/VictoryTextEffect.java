package com.gpergrossi.spaceinvaders.ui.texteffects;

import com.gpergrossi.spaceinvaders.animation.*;

import java.awt.geom.Point2D;

public class VictoryTextEffect extends AnimatedTextEffect {

    private static final VictoryTextEffect single = new VictoryTextEffect();

    public static VictoryTextEffect get() { return single; }

    private VictoryTextEffect() {}

    @Override
    public TextAnimationsBundle getAnimations(String fullText, int index) {

        TextAnimationsBundle animations = new TextAnimationsBundle();

        animations.characterPosition = new TweenSequence<>("Victory Animation Position",
            new TweenStep<Point2D.Double>(0.2, new Point2D.Double(0,  75), new Point2D.Double(0, -50), LerpFunction.LERP_VEC2, TweenFunction.EASE_OUT_QUAD),
            new TweenStep<Point2D.Double>(0.8, new Point2D.Double(0,  -50), new Point2D.Double(0, 0), LerpFunction.LERP_VEC2, TweenFunction.DROP_BOUNCE)
        );
        animations.characterPosition.setDefaultStartTime(index * -0.1);

        animations.alpha = new TweenSequence<>("Victory Animation Alpha",
            new TweenStep<Double>(0.1, 0.0, 1.0, LerpFunction.LERP_DOUBLE, TweenFunction.LINEAR)
        );
        animations.alpha.setDefaultStartTime(index * -0.1);

        animations.saturation = TweenSequence.createConstant("Victory Animation Saturation", 0.8);

        animations.hue = new TweenSequence<>("Victory Animation Hue",
            new TweenStep<Double>(4.0, 0.0, 1.0, LerpFunction.LERP_DOUBLE, TweenFunction.LINEAR)
        );
        animations.hue.setLooping(true);
        animations.hue.setDefaultStartTime(index * -0.1);

        return animations;

    };

}
