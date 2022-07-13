package com.gpergrossi.spaceinvaders.animation;

@FunctionalInterface
public interface LerpFunction<T> {
    T lerp(T a, T b, double t);
}
