package com.gpergrossi.spaceinvaders.animation;

@FunctionalInterface
public interface TweenFunction {
    double tween(double input);

    TweenFunction LINEAR = (t) -> t;
    TweenFunction COSINE = (t) -> 1.0 - (Math.cos(Math.PI * 2.0 * t) * 0.5 + 0.5);
}
