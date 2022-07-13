package com.gpergrossi.spaceinvaders.animation;

import java.awt.*;
import java.awt.geom.Point2D;

public class TweenStep<T> implements Animation {

    private double currentTime;
    private T currentValue;
    private double duration;
    private T startValue;
    private T endValue;
    private LerpFunction<T> lerpFunction;
    private TweenFunction tweenFunction;

    public TweenStep(double duration, T startValue, T endValue, LerpFunction<T> lerpFunction, TweenFunction tweenFunction) {
        this.currentTime = 0;
        this.duration = duration;
        this.startValue = startValue;
        this.endValue = endValue;
        this.lerpFunction = (lerpFunction != null) ? lerpFunction : AnimationUtils.getDefaultLerpFunction(startValue, endValue);
        this.tweenFunction = (tweenFunction != null) ? tweenFunction : ((t) -> t);
    }

    public T getValue() {
        return currentValue;
    }

    private void recompute() {
        double t = currentTime / duration;
        t = Math.max(Math.min(t, 1.0), 0.0);
        t = tweenFunction.tween(t);
        currentValue = lerpFunction.lerp(startValue, endValue, t);
    }

    @Override
    public void update(double delta) {
        currentTime += delta;
        recompute();
    }

    @Override
    public double getDuration() {
        return duration;
    }

    @Override
    public double getCurrentTime() {
        return currentTime;
    }

    @Override
    public double getTimeRemaining() {
        return duration - currentTime;
    }

    @Override
    public void seek(double time) {
        currentTime = time;
        recompute();
    }

}
