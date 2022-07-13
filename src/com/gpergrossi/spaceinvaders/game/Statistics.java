package com.gpergrossi.spaceinvaders.game;

import com.gpergrossi.spaceinvaders.entity.ShotEntity;

import java.util.HashMap;
import java.util.Map;

public class Statistics {

    private int shotsFired;
    private int shotsHit;
    private int shotsMissed;
    private int hitCombo;
    private int maxCombo;
    private int lastMissedShotIndex;

    private int nextShotIndex;
    private HashMap<ShotEntity, ShotInfo> activeShots;

    private static class ShotInfo {
        private int index;
        private boolean alive;
        private boolean hit;
        private boolean missed;

        /**
         * Used to tracks if this shot is part of the combo score already.
         *
         * If the combo was incremented with a different lastMissedShotIndex,
         * then this shot can be re-applied to the combo when it is confirmed
         * hit by the scanActiveShots() method.
         */
        private int comboLastMiss;

        public ShotInfo(int index) {
            this.index = index;
            this.alive = true;
            this.hit = false;
            this.missed = false;
            this.comboLastMiss = -1;
        }
    }

    public Statistics() {
        this.activeShots = new HashMap<>();
        this.reset();
    }

    public void reset() {
        this.shotsFired = 0;
        this.shotsHit = 0;
        this.shotsMissed = 0;
        this.hitCombo = 0;
        this.maxCombo = 0;
        this.nextShotIndex = 0;
        this.lastMissedShotIndex = -1;
        activeShots.clear();
    }

    public void trackShotFired(ShotEntity shot) {
        shotsFired++;
        if (!activeShots.containsKey(shot)) {
            ShotInfo info = new ShotInfo(nextShotIndex);
            activeShots.put(shot, info);
            nextShotIndex++;
        }
    }

    public void trackShotHit(ShotEntity shot) {
        shotsHit++;
        hitCombo++;
        if (hitCombo > maxCombo) { maxCombo = hitCombo; }
        if (activeShots.containsKey(shot)) {
            ShotInfo info = activeShots.get(shot);
            info.alive = false;
            info.hit = true;
            info.comboLastMiss = lastMissedShotIndex;
            scanActiveShots();
        }
    }

    public void trackShotMissed(ShotEntity shot) {
        shotsMissed++;
        if (activeShots.containsKey(shot)) {
            ShotInfo info = activeShots.get(shot);
            info.alive = false;
            info.missed = true;
            scanActiveShots();
        }
    }

    /** Checks for consecutive hits and clean up expired shots. */
    private void scanActiveShots() {

        while (true) {
            int minIndex = -1;
            Map.Entry<ShotEntity, ShotInfo> minShot = null;

            // Find lowest shot index
            for (Map.Entry<ShotEntity, ShotInfo> entry : activeShots.entrySet()) {
                ShotInfo info = entry.getValue();
                if (minIndex == -1 || info.index < minIndex) {
                    minIndex = info.index;
                    minShot = entry;
                }
            }

            if (minShot == null) {
                // No shots left
                break;
            } else {
                // Confirm shot combo
                ShotEntity entity = minShot.getKey();
                ShotInfo info = minShot.getValue();

                if (!info.alive) {
                    // This shot is now dead
                    activeShots.remove(entity);

                    if (info.missed) {
                        // Reset the combo due to a miss
                        hitCombo = 0;
                        lastMissedShotIndex = info.index;
                    } else if (info.hit) {
                        // Re-apply shots-that-have-hit-since-the-last-miss to the combo
                        if (info.comboLastMiss != lastMissedShotIndex) {
                            hitCombo++;
                            info.comboLastMiss = lastMissedShotIndex;
                        }
                    }
                } else {
                    // Encountered a living shot, stop scanning. We'll update again when another shot hits/misses.
                    break;
                }
            }
        }
    }

    public int getShotsFired() {
        return shotsFired;
    }

    public int getShotsHit() {
        return shotsHit;
    }

    /**
     * Accuracy is determined by shots missed instead of shots hit.
     * This is important because it means that accuracy doesn't go
     * down every time the player fires a shot.
     */
    public float getAccuracy() {
        if (shotsFired == 0) return 1.0f;
        return (float) (shotsFired - shotsMissed) / shotsFired;
    }

    public int getHitCombo() {
        return hitCombo;
    }

    public int getMaxHitCombo() {
        return maxCombo;
    }

}
