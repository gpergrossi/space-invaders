package com.gpergrossi.spaceinvaders.entity;

import java.util.ArrayList;

public class AlienSwarm {

    private ArrayList<AlienEntity> aliens;

    private boolean directionChangeRequested;

    public AlienSwarm() {
        aliens = new ArrayList<>();
        directionChangeRequested = false;
    }

    /**
     * Adds an alien entity to the swarm
     */
    public boolean addAlien(AlienEntity alien) {
        return aliens.add(alien);
    }

    /**
     * Removes an alien entity from the swarm
     */
    public boolean removeAlien(AlienEntity alien) {
        return aliens.remove(alien);
    }

    /**
     * Clears all alien entities from the swam.
     */
    public void clear() {
        aliens.clear();
    }

    /**
     * @return Number of aliens in the swarm
     */
    public int count() {
        return aliens.size();
    }

    /**
     * Used to notify all aliens in the formation that one of the aliens has
     * reached the edge of the screen. The actual change of direction will happen
     * after the AlienFormation itself is updated using the update() method.
     */
    public void requestChangeDirection() {
        directionChangeRequested = true;
    }

    /**
     * Called once per loop to manage event communication between aliens in the Formation.
     */
    public void update() {
        if (directionChangeRequested) {
            for (AlienEntity alien : aliens) {
                alien.changeDirection();
            }
            directionChangeRequested = false;
        }
    }

    public void increaseSpeed(float factor) {
        for (AlienEntity alien : aliens) {
            // speed up by 2%
            alien.setVelocityX(alien.getVelocityX() * factor);
        }
    }
}
