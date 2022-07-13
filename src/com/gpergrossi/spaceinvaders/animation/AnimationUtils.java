package com.gpergrossi.spaceinvaders.animation;

import java.awt.*;
import java.awt.geom.Point2D;

public class AnimationUtils {

    public static final LerpFunction<Double> LERP_DOUBLE = (Double a, Double b, double t) -> a * t + b * (1.0-t);

    public static final LerpFunction<Color> LERP_HSB = (Color a, Color b, double t) -> {
        float[] hsbA = new float[3];
        float[] hsbB = new float[3];

        Color.RGBtoHSB(a.getRed(), a.getGreen(), a.getBlue(), hsbA);
        Color.RGBtoHSB(b.getRed(), b.getGreen(), b.getBlue(), hsbB);

        float tf = (float) t;
        float hue = hsbA[0] * tf + hsbB[0] * (1.0f - tf);
        float sat = hsbA[1] * tf + hsbB[1] * (1.0f - tf);
        float bri = hsbA[2] * tf + hsbB[2] * (1.0f - tf);
        int alpha = (int)(a.getAlpha() * tf + b.getAlpha() * (1.0f - tf));

        int rgb = Color.HSBtoRGB(hue, sat, bri);
        return new Color((alpha << 24) | rgb);
    };

    public static final LerpFunction<Color> LERP_RGB = (Color a, Color b, double t) -> {
        int red   = (int)(a.getRed()   * t  + b.getRed()   * (1.0f - t));
        int green = (int)(a.getGreen() * t  + b.getGreen() * (1.0f - t));
        int blue  = (int)(a.getBlue()  * t  + b.getBlue()  * (1.0f - t));
        int alpha = (int)(a.getAlpha() * t  + b.getAlpha() * (1.0f - t));
        return new Color((alpha << 24) | (red << 16) | (green << 8) | blue);
    };

    public static final LerpFunction<Point2D.Double> LERP_VEC2 = (Point2D.Double a, Point2D.Double b, double t) -> {
        double x = a.x * t + b.x * (1.0 - t);
        double y = a.y * t + b.y * (1.0 - t);
        return new Point2D.Double(x, y);
    };

    /**
     * Assumes the correct lerp function based on type or default to a Nearest-Value function.
     */
    public static <T> LerpFunction<T> getDefaultLerpFunction(T startValue, T endValue) {
        if (startValue instanceof Double) return (LerpFunction<T>) LERP_DOUBLE;
        if (startValue instanceof Color)  return (LerpFunction<T>) LERP_HSB;
        if (startValue instanceof Point2D.Double) return (LerpFunction<T>) LERP_VEC2;

        // Default to nearest value function
        return (T a, T b, double t) -> {
            return (t < 0.5) ? a : b;
        };
    }

}
