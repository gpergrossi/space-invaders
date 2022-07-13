package com.gpergrossi.spaceinvaders.animation;

import java.util.ArrayList;

public class TweenSequence<T> implements Animation {

    /**
     * Creates a TweenSequence that remains constant throughout its duration.
     */
    public static <T> TweenSequence<T> createConstant(String name, T value) {
        TweenStep<T> step = new TweenStep<>(0.01, value, value, null, null);
        return new TweenSequence<>(name, step);
    }

    private String name;

    private double totalDuration;
    private ArrayList<TweenStep<T>> steps;
    private boolean looping;
    private double defaultStartTime;

    private double currentTime;
    private int currentStepIndex;

    private ValueListener<T> listener;

    public TweenSequence(String name, TweenStep<T>... steps) {
        if (steps.length < 1) throw new IllegalArgumentException("Must provide at least one tween step!");

        this.name = name;
        this.totalDuration = 0;
        this.steps = new ArrayList<>();
        for (int i = 0; i < steps.length; i++) {
            addStep(steps[i]);
        }
        this.looping = false;
        this.defaultStartTime = 0;

        this.currentTime = 0;
        currentStepIndex = 0;
    }

    private void addStep(TweenStep<T> step) {
        this.steps.add(step);
        this.totalDuration += step.getDuration();
    }

    public String getName() {
        return name;
    }

    public void setLooping(boolean enabled) {
        this.looping = enabled;
    }

    public boolean isLooping() {
        return looping;
    }

    public void setDefaultStartTime(double time) {
        this.defaultStartTime = time;
    }

    public double getDefaultStartTime() {
        return this.defaultStartTime;
    }

    public T getValue() {
        int index = currentStepIndex;
        if (index >= steps.size()) {
            index = steps.size()-1;
        }
        return steps.get(index).getValue();
    }

    public void setListener(ValueListener listener) {
        this.listener = listener;
    }

    @Override
    public void update(double delta) {
        double remainingDelta = delta;
        while (remainingDelta > 0) {
            // Break out if we're at the end of the sequence
            if (currentStepIndex >= steps.size()) return;

            TweenStep<T> currentStep = steps.get(currentStepIndex);
            if (currentStep != null) {

                if (delta < currentStep.getTimeRemaining()) {
                    // If there is a enough time in the current step, proceed within that step
                    currentTime += delta;
                    currentStep.update(delta);
                    remainingDelta = 0;
                } else {
                    // Otherwise, finish the current step and move to the next
                    remainingDelta -= currentStep.getTimeRemaining();
                    currentTime += currentStep.getTimeRemaining();
                    currentStep.finish();
                    currentStepIndex++;
                }
            }
        }

        if (listener != null) {
            listener.onChange(getValue());
        }
    }

    @Override
    public double getDuration() {
        return totalDuration;
    }

    @Override
    public double getCurrentTime() {
        return currentTime;
    }

    @Override
    public double getTimeRemaining() {
        return totalDuration - currentTime;
    }

    @Override
    public void seek(double time) {
        currentTime = time;

        boolean stepFound = false;
        for (int i = 0; i < steps.size(); i++) {
            TweenStep<T> currentStep = steps.get(i);
            if (time < currentStep.getDuration()) {
                currentStepIndex = i;
                currentStep.seek(time);
                stepFound = true;
                break;
            } else {
                time -= currentStep.getDuration();
            }
        }

        if (!stepFound) {
            currentStepIndex = steps.size()-1;
        }
    }
}
