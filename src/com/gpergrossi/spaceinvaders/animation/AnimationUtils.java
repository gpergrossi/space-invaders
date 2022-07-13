package com.gpergrossi.spaceinvaders.animation;

import java.awt.*;
import java.awt.geom.Point2D;

public class AnimationUtils {

    /**
     * Assumes the correct lerp function based on type or default to a Nearest-Value function.
     */
    public static <T> LerpFunction<T> getDefaultLerpFunction(T startValue, T endValue) {
        if (startValue instanceof Double) return (LerpFunction<T>) LerpFunction.LERP_DOUBLE;
        if (startValue instanceof Color)  return (LerpFunction<T>) LerpFunction.LERP_HSB;
        if (startValue instanceof Point2D.Double) return (LerpFunction<T>) LerpFunction.LERP_VEC2;

        // Default to nearest value function
        return (T a, T b, double t) -> {
            return (t < 0.5) ? a : b;
        };
    }

}
