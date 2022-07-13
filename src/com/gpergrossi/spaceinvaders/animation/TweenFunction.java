package com.gpergrossi.spaceinvaders.animation;

@FunctionalInterface
public interface TweenFunction {

    /**
     * A tweening function is responsible for mapping a value in the range 0.0 to 1.0 to
     * a differently shaped curve within that range (or slightly outside it).
     *
     * Good tweening functions are typically a continuous curve starting at y = 0.0 and ending at y = 1.0.
     */
    double tween(double input);



    /** A prefab Tweening function that acts as a linear interpolation */
    TweenFunction LINEAR = (t) -> t;

    /** A nice Tweening function that accelerates at the start and decelerates at the end */
    TweenFunction COSINE = (t) -> 1.0 - (Math.cos(Math.PI * 2.0 * t) * 0.5 + 0.5);

    /** A Tweening function that mimics a few bounces */
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

    /** A Tweening function that decelerates toward the end */
    TweenFunction EASE_OUT_QUAD = (t) -> 1.0 - (1.0 - t) * (1.0 - t);
}
