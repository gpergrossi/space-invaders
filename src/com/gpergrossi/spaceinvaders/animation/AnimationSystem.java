package com.gpergrossi.spaceinvaders.animation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnimationSystem {

    /** A small number, used to aid in comparing floating point values */
    private static double EPSILON = 0.001;

    /** The animations managed by this animation system */
    private HashMap<Animation, AnimationStatus> animations;

    /** A list of animations that are to be removed */
    private List<Animation> removeList;

    /** A list of callbacks to be notified when all non-looping animations are completed */
    private ArrayList<Runnable> finishCallbacks;

    /** Default constructor */
    public AnimationSystem() {
        animations = new HashMap<>();
        removeList = new ArrayList<>();
        finishCallbacks = new ArrayList<>();
    }

    /** Update all animations in the animation system */
    public void update(long deltaMs) {

        // Compute time step in seconds.
        double timeStep = (deltaMs / 1000.0);

        for (Map.Entry<Animation, AnimationStatus> entry : animations.entrySet()) {
            Animation animation = entry.getKey();
            AnimationStatus status = entry.getValue();

            // Do not process any animations that are paused
            if (status.isPaused()) continue;

            // Time can pass differently based on animation PlaybackSpeed.
            double entityTimePassed = timeStep * status.getPlaybackSpeed();

            // The "remaining" time step is used to carry residual time forward when looping.
            double remainingStep = 0.0;

            // Update the animation
            if (entityTimePassed < animation.getTimeRemaining()) {
                // We have enough time left in the animation to advance normally
                animation.update(entityTimePassed);
            } else {
                // The animation ends before the time step is over. Advance to end of animation and
                // record the remaining time step, which we will to apply looped animations later.
                double remaining = animation.getTimeRemaining();
                animation.update(remaining);
                remainingStep = entityTimePassed - remaining;
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
                    double seekTo = position + remainingStep;
                    seekTo -= Math.floor(seekTo / animation.getDuration()) * animation.getDuration();
                    animation.update(seekTo);

                } else {
                    // Seek to end of animation so onComplete sees consistent position
                    animation.finish();

                    // Pause playback
                    status.pause();

                    // Call onComplete handlers
                    status.onComplete();

                    // Remove this animation from the system
                    removeList.add(animation);
                }
            }
        }

        // Remove completed animations
        for (Animation animation : removeList) {
            animations.remove(animation);
        }
        removeList.clear();

        // If we have finish callbacks, check for completion of all non-looping animations
        if (finishCallbacks.size() > 0) {
            boolean anyUnfinished = false;
            for (Map.Entry<Animation, AnimationStatus> entry : animations.entrySet()) {
                Animation animation = entry.getKey();
                AnimationStatus status = entry.getValue();

                if (!status.isLooping() && animation.getCurrentTime() < animation.getDuration()) {
                    anyUnfinished = true;
                    break;
                }
            }

            if (!anyUnfinished) {
                // All animations are finished! Notify all callback functions.
                for (Runnable cb : finishCallbacks) {
                    cb.run();
                }

                // Also clear the callback list, since it doesn't make sense to leave them.
                finishCallbacks.clear();
            }
        }

        //printDebug();
    }

    /** Prints a log of all animations to the console for debugging purposes. */
    private void printDebug() {
        System.out.println("Number of animations running: " + animations.size());
        for (Map.Entry<Animation, AnimationStatus> entry : animations.entrySet()) {
            Animation animation = entry.getKey();
            AnimationStatus status = entry.getValue();

            String name = "<anon>";

            if (animation instanceof TweenSequence) {
                TweenSequence ts = (TweenSequence) animation;
                name = ts.getName();
            }

            System.out.println("   " + name + " ("
                    + animation.getCurrentTime() + "/" + animation.getDuration()
                    + ", " + (status.isPaused() ? "paused" : "running")
                    + ", " + (status.isLooping() ? "looping" : "not looping")
                    + ")");
        }
    }

    /**
     * Gets the status of an animation that may or may not be managed by this animation system.
     * If the animation is not already in this system, then it can be created depending on
     * the @p create parameter.
     *
     * @param animation The animation to lookup.
     * @param create If true, the animation will be created and returned if needed.
     * @return An AnimationStatus object for the requested animation, or null if @p create is false.
     */
    private AnimationStatus getStatus(Animation animation, boolean create) {
        if (animations.containsKey(animation)) {
            // If the animation is registered, get its status.
            return animations.get(animation);
        } else if (create) {
            // Otherwise, if we are allowed to create, create a new status object for this animation.
            AnimationStatus status = new AnimationStatus();
            animations.put(animation, status);
            return status;
        } else {
            // If not, then return null to indicate failure.
            return null;
        }
    }

    /**
     * Starts an animation playing from the beginning.
     * If the animation is already managed by this system, it will rewind back to the start and remain playing.
     */
    public boolean start(Animation animation) {
        return this.start(animation, 0, false, null);
    }

    /**
     * Starts an animation playing with a few options to control the playback.
     * @param animation The animation to play.
     * @param startTime The start time for the animation. Can be negative, indicating a delayed start.
     * @param looping If true, the animation will be set to loop and excluded from the awaitFinish callback.
     * @param listener An animation listener can be attached to know when the animation finishes or loops.
     * @return
     */
    public boolean start(Animation animation, double startTime, boolean looping, AnimationListener listener) {
        // Get or create a status object for this animation
        AnimationStatus status = getStatus(animation, true);

        // Start animation from requested time
        animation.seek(startTime);

        // Add a listener if there is one
        if (listener != null) { status.setListener(listener); }

        // Looping?
        status.setLooping(looping);

        // Begin playing
        status.play();

        // Return success
        return true;
    }

    /**
     * Remove an animation from this system.
     * @param animation The animation to be removed.
     * @return True if the animation was registered. Note: It is not actually removed yet, that happens in update().
     */
    public boolean remove(Animation animation) {
        if (!animations.containsKey(animation)) return false;

        removeList.add(animation);
        return true;
    }

    /**
     * Pause an animation's playback.
     * If the animation is not managed by this animation system, nothing will happen.
     * @param animation The animation to be paused.
     */
    public void pause(Animation animation) {
        AnimationStatus status = getStatus(animation, false);
        if (status != null) {
            status.pause();
        }
    }

    /** Check if an animation is being played by this animation system. */
    public boolean isPlaying(Animation animation) {
        AnimationStatus status = getStatus(animation, false);
        return (status != null) && !status.isPaused();
    }

    /**
     * Check if an animation is being looped.
     * Returns false if the animation is not managed by this animation system.
     * @param animation The animation to be looped.
     * @return True if the animation is looping, false if not or if the animation does not belong to this system.
     */
    public boolean isLooping(Animation animation) {
        AnimationStatus status = getStatus(animation, false);
        return (status != null) && status.isLooping();
    }

    /** Force an animation to complete. If the animation is not managed by this system then nothing will happen. */
    public void finish(Animation animation) {
        AnimationStatus status = getStatus(animation, false);
        if (status != null) {
            animation.finish();
            status.setLooping(false);
        }
    }

    /** Force finish all animations in this animation system */
    public void finishAll() {
        for (Map.Entry<Animation, AnimationStatus> entry : animations.entrySet()) {
            Animation animation = entry.getKey();
            AnimationStatus status = entry.getValue();

            animation.finish();
            status.setLooping(false);
        }
    }

    /**
     * Register a callback to listen for completion of all animations.
     *
     * Note: If more animations are added before the existing animations are complete,
     * the callback will continue waiting for all additional animations.
     */
    public void awaitFinish(Runnable finishCallback) {
        finishCallbacks.add(finishCallback);
    }

    /** Remove all animations and finish callbacks from this animation system */
    public void clear() {
        this.animations.clear();
        this.removeList.clear();
        this.finishCallbacks.clear();
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



    /** Internal class to track the status of animations in the animation system */
    private static class AnimationStatus {

        /** Is this animation paused? */
        private boolean paused;

        /** Is this animation looping */
        private boolean looping;

        /** The playback speed of this animation */
        private double speed;

        /** Each animation can have one animation listener */
        private AnimationListener listener;

        /** Default constructor */
        public AnimationStatus() {
            this.paused = true;
            this.looping = false;
            this.speed = 1.0f;
            this.listener = null;
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

        public void setListener(AnimationListener listener) {
            this.listener = listener;
        }

        /** Tell all listeners that the animation is complete */
        private void onComplete() {
            if (listener != null) {
                listener.onAnimationComplete();
            }
        }

        /** Tell all listeners that the animation has completed a loop */
        private void onLoop() {
            if (listener != null) {
                listener.onLoop();
            }
        }
    }

}
