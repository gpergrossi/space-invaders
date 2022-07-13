package com.gpergrossi.spaceinvaders.animation;

@FunctionalInterface
public interface TweenFunction {
    double tween(double input);

    TweenFunction LINEAR = (t) -> t;

    TweenFunction COSINE = (t) -> 1.0 - (Math.cos(Math.PI * 2.0 * t) * 0.5 + 0.5);

    TweenFunction DROP_BOUNCE = (t) -> {
        double n1 = 7.5625;
        double d1 = 2.75;

        if (t < 1.0 / d1) {
            return n1 * t * t;
        } else if (t < 2.0 / d1) {
            t -= 1.5 / d1;
            return n1 * t * t + 0.75;
        } else if (t < 2.5 / d1) {
            t -= 2.25 / d1;
            return n1 * t * t + 0.9375;
        } else {
            t -= 2.625 / d1;
            return n1 * t * t + 0.984375;
        }
    };

    TweenFunction EASE_OUT_QUAD = (t) -> 1.0 - (1.0 - t) * (1.0 - t);
}
