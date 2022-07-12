package com.gpergrossi.spaceinvaders.animation;

public interface Animation {

    /** Advance the animation by delta milliseconds */
    void update(double delta);

    /** Get the total duration of the animation (in ms) */
    double getDuration();

    /** Get the current playback position of the animation (in ms) */
    double getCurrentTime();

    /** Get the remaining time left in the animation (in ms) */
    double getTimeRemaining();

    /** Seek to a specified playback time (in ms) */
    void seek(double time);

    /** Set the playback position to the start of the animation */
    default void reset() { seek(0); }

    /** Skip to the end of the animation */
    default void finish() { seek(getDuration()); }

}
