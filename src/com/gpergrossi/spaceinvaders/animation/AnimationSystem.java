package com.gpergrossi.spaceinvaders.animation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnimationSystem {

    private static double EPSILON = 0.001;

    private HashMap<Animation, AnimationStatus> animations;
    private List<Animation> removeList;

    public AnimationSystem() {
        animations = new HashMap<>();
        removeList = new ArrayList<>();
    }

    public void update(long deltaMs) {
        for (Map.Entry<Animation, AnimationStatus> entry : animations.entrySet()) {
            Animation animation = entry.getKey();
            AnimationStatus status = entry.getValue();

            if (!status.isPaused()) {
                double timeStep = (deltaMs / 1000.0) * status.getPlaybackSpeed();
                double remainingStep = 0.0;

                // Update the animation
                if (timeStep < animation.getTimeRemaining()) {
                    animation.update(timeStep);
                } else {
                    double remaining = animation.getTimeRemaining();
                    animation.update(remaining);
                    remainingStep = timeStep - remaining;
                }

                // Check if the animation is complete
                if (animation.getCurrentTime() >= (animation.getDuration() - EPSILON)) {

                    if (status.isLooping()) {
                        // Process looping
                        double position = animation.getCurrentTime() - animation.getDuration();

                        // Set time to 0 first so onLoop always sees a consistent position.
                        animation.reset();

                        // Call onLoop handlers
                        status.onLoop();

                        // Move forward to correct position to maintain smooth looping
                        animation.update(position + remainingStep);

                    } else {
                        // Pause playback
                        status.pause();

                        // Seek to end of animation so onComplete sees consistent position
                        animation.finish();

                        // Call onComplete handlers
                        status.onComplete();

                        removeList.add(animation);
                    }
                }
            }
        }

        // Remove completed animations
        for (Animation animation : removeList) {
            animations.remove(animation);
        }
        removeList.clear();
    }

    private AnimationStatus getStatus(Animation animation, boolean create) {
        if (animations.containsKey(animation)) {
            // If the animation is registered, get its status.
            return animations.get(animation);
        } else if (create) {
            AnimationStatus status = new AnimationStatus();
            animations.put(animation, status);
            return status;
        } else {
            return null;
        }
    }

    public void start(Animation animation) {
        AnimationStatus status = getStatus(animation, true);
        animation.reset();
        status.play();
    }

    public void remove(Animation animation) {
        removeList.add(animation);
    }

    public void pause(Animation animation) {
        AnimationStatus status = getStatus(animation, false);
        if (status != null) {
            status.pause();
        }
    }

    public boolean isPlaying(Animation animation) {
        AnimationStatus status = getStatus(animation, false);
        return (status != null) && !status.isPaused();
    }

    public void setLooping(Animation animation, boolean looping) {
        AnimationStatus status = getStatus(animation, false);
        if (status != null) {
            status.setLooping(looping);
        }
    }

    public boolean isLooping(Animation animation) {
        AnimationStatus status = getStatus(animation, false);
        return (status != null) && status.isLooping();
    }

    public void addListener(Animation animation, AnimationListener listener) {
        AnimationStatus status = getStatus(animation, false);
        if (status != null) {
            status.addListener(listener);
        }
    }

    public void finish(Animation animation) {
        animation.finish();
        pause(animation);
    }

    public void finishAll() {
        for (Map.Entry<Animation, AnimationStatus> entry : animations.entrySet()) {
            Animation animation = entry.getKey();
            AnimationStatus status = entry.getValue();

            animation.finish();
            status.pause();
        }
    }

    public void clear() {
        this.animations.clear();
        this.removeList.clear();
    }

    /**
     * Gets the time remaining on the animation with the longest remaining time.
     * Does not include animations that are set to looping.
     *
     * @return Number of seconds until all non-looping animations are complete.
     */
    double getTimeRemaining() {
        double maxRemaining = 0;

        for (Map.Entry<Animation, AnimationStatus> entry : animations.entrySet()) {
            Animation animation = entry.getKey();
            AnimationStatus status = entry.getValue();

            if (!status.isLooping()) {
                double remaining = animation.getTimeRemaining() / status.getPlaybackSpeed();
                if (remaining > maxRemaining) {
                    maxRemaining = remaining;
                }
            }
        }

        return maxRemaining;
    }


    private class AnimationStatus {
        private boolean paused;
        private boolean looping;
        private double speed;

        private ArrayList<AnimationListener> listeners;

        public AnimationStatus() {
            this.paused = true;
            this.looping = false;
            this.speed = 1.0f;
            this.listeners = new ArrayList<>();
        }

        public boolean isPaused() {
            return paused;
        }

        public void pause() {
            this.paused = true;
        }

        public void play() {
            this.paused = false;
        }

        public boolean isLooping() {
            return looping;
        }

        public void setLooping(boolean enabled) {
            this.looping = enabled;
        }

        public double getPlaybackSpeed() {
            return speed;
        }

        public void setPlaybackSpeed(double speed) {
            this.speed = speed;
        }

        public void addListener(AnimationListener listener) {
            this.listeners.add(listener);
        }

        /** Tell all listeners that the animation is complete */
        private void onComplete() {
            for (AnimationListener listener : listeners) {
                listener.onAnimationComplete();
            }
        }

        /** Tell all listeners that the animation has completed a loop */
        private void onLoop() {
            for (AnimationListener listener : listeners) {
                listener.onLoop();
            }
        }
    }

}
