package com.gpergrossi.spaceinvaders.animation;

import java.awt.*;
import java.awt.geom.Point2D;

@FunctionalInterface
public interface LerpFunction<T> {

    /**
     * A lerp function (stands for "linear interpolation") is responsible for blending between
     * two values. The value @p t ranges from 0 (equal to @p a) to 1 (equal to @p b).
     */
    T lerp(T a, T b, double t);



    /** A prefab function for lerping between Doubles */
    LerpFunction<Double> LERP_DOUBLE = (Double a, Double b, double t) -> a * (1.0 - t) + b * t;

    /** A prefab function for lerping between Point2D.Doubles */
    LerpFunction<Point2D.Double> LERP_VEC2 = (Point2D.Double a, Point2D.Double b, double t) -> {
        double x = a.getX() * (1.0 - t) + b.getX() * t;
        double y = a.getY() * (1.0 - t) + b.getY() * t;
        return new Point2D.Double(x, y);
    };

    /** A prefab function for lerping between Colors by Hue, Saturation, Brightness, and Alpha. */
    LerpFunction<Color> LERP_HSB = (Color a, Color b, double t) -> {
        float[] hsbA = new float[3];
        float[] hsbB = new float[3];

        Color.RGBtoHSB(a.getRed(), a.getGreen(), a.getBlue(), hsbA);
        Color.RGBtoHSB(b.getRed(), b.getGreen(), b.getBlue(), hsbB);

        float tf = (float) t;
        float hue = hsbA[0] * (1.0f - tf) + hsbB[0] * (tf);
        float sat = hsbA[1] * (1.0f - tf) + hsbB[1] * (tf);
        float bri = hsbA[2] * (1.0f - tf) + hsbB[2] * (tf);
        Color rgb = new Color(Color.HSBtoRGB(hue, sat, bri));

        int red = rgb.getRed();
        int green = rgb.getGreen();
        int blue = rgb.getBlue();
        int alpha = (int)(a.getAlpha() * (1.0f - tf) + b.getAlpha() * (tf));

        return new Color(red, green, blue, alpha);
    };

    /** A prefab function for lerping between Colors by Red, Green, Blue, and Alpha. */
    LerpFunction<Color> LERP_RGB = (Color a, Color b, double t) -> {
        int red   = (int)(a.getRed()   * (1.0f - t) + b.getRed()   * (t));
        int green = (int)(a.getGreen() * (1.0f - t) + b.getGreen() * (t));
        int blue  = (int)(a.getBlue()  * (1.0f - t) + b.getBlue()  * (t));
        int alpha = (int)(a.getAlpha() * (1.0f - t) + b.getAlpha() * (t));
        return new Color(red, green, blue, alpha);
    };
}
