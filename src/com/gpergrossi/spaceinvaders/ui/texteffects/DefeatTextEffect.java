package com.gpergrossi.spaceinvaders.ui.texteffects;

import com.gpergrossi.spaceinvaders.animation.LerpFunction;
import com.gpergrossi.spaceinvaders.animation.TweenFunction;
import com.gpergrossi.spaceinvaders.animation.TweenSequence;
import com.gpergrossi.spaceinvaders.animation.TweenStep;

import java.awt.geom.Point2D;

public class DefeatTextEffect extends AnimatedTextEffect {

    private static final DefeatTextEffect single = new DefeatTextEffect();

    public static DefeatTextEffect get() { return single; }

    private DefeatTextEffect() {}

    @Override
    public TextAnimationsBundle getAnimations(String fullText, int index) {

        TextAnimationsBundle animations = new TextAnimationsBundle();

        animations.characterPosition = new TweenSequence<>("Defeat Animation Position",
            new TweenStep<Point2D.Double>(1.0, new Point2D.Double(0,  -50), new Point2D.Double(0, 0), LerpFunction.LERP_VEC2, TweenFunction.DROP_BOUNCE)
        );
        animations.characterPosition.setDefaultStartTime(index * -0.05);

        animations.alpha = new TweenSequence<>("Defeat Animation Alpha",
            new TweenStep<Double>(0.1, 0.0, 1.0, LerpFunction.LERP_DOUBLE, TweenFunction.LINEAR)
        );
        animations.alpha.setDefaultStartTime(index * -0.05);

        animations.brightness = new TweenSequence<>("Defeat Animation Brightness",
                new TweenStep<Double>(1.0, 1.0, 0.7, LerpFunction.LERP_DOUBLE, TweenFunction.LINEAR)
        );
        animations.brightness.setDefaultStartTime(index * -0.05);

        animations.saturation = new TweenSequence<>("Defeat Animation Saturation",
                new TweenStep<Double>(1.0, 0.0, 0.75, LerpFunction.LERP_DOUBLE, TweenFunction.LINEAR)
        );
        animations.saturation.setDefaultStartTime(index * -0.05);

        animations.hue = TweenSequence.createConstant("Defeat Animation Hue", 0.0);

        return animations;

    };

}
