package com.team30.game.game_mechanics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Wrapper class for all infiltrators, and handles the movement and rendering of them
 */
public class InfiltratorContainer {
    /**
     * The maximum number of infiltrators to spawn
     */
    private static final int MAX_INFILTRATORS = 10;
    /**
     * The infiltrators that are currently "alive" on the map
     */
    private List<Infiltrator> currentInfiltrators;
    /**
     * The number of infiltrators that have been spawned so far
     */
    private int spawnedInfiltrators;
    /**
     * The time since an infiltrator was last spawned
     */
    private float timeSinceLastSpawn;


    public InfiltratorContainer() {
        this.spawnedInfiltrators = 0;
        this.timeSinceLastSpawn = 0;
        this.currentInfiltrators = new ArrayList<>();
    }

    /**
     * Checks whether to spawn a new infiltrator <br>
     * Checks all active infiltrators and whether they need moving, then updates their position
     * and draws them
     *
     * @param deltaTime       The time passed, since this was last called
     * @param room            The map layer containing all valid room tiles
     * @param systemContainer The container for all the ship systems
     * @param batch           Libgdx drawing system
     */
    public void updateAndDraw(float deltaTime, TiledMapTileLayer room, SystemContainer systemContainer, Batch batch) {
        timeSinceLastSpawn += deltaTime;
        if (timeSinceLastSpawn > 2) {
            spawnInfiltrator(room);
        }

        for (Infiltrator infiltrator : currentInfiltrators) {
            infiltrator.incrementTimeSinceLastUpdate(deltaTime);
            if (infiltrator.getTimeSinceLastUpdate() > 0.2) {
                infiltrator.moveInfiltrator(room, systemContainer);
                infiltrator.resetTimeSinceLastUpdate();
            }
            infiltrator.updatePosition(deltaTime, room);
            infiltrator.draw(batch);

        }
    }

    /**
     * Horrible logic statement for collision checking<br>
     * <p>
     * Consists of 4 parts<br>
     * If the leftmost auber point (including range) is less than the rightmost infiltrator point <br>
     * <p>
     * AND the leftmost infiltrator point  is less than the rightmost auber point (including range)<br>
     * <p>
     * AND the lowest auber point (including range) is less than the topmost infiltrator point<br>
     * <p>
     * AND the lowest infiltrator point  is less than the topmost auber point (including range)<br>
     *
     * @param auber       The player character to check a boundry around
     * @param infiltrator The infiltrator to check if it is insde the boundry box
     * @param range       The range around the auber to check (in the x and y axis)
     * @return True if the infiltrator is inside the collision box
     */
    boolean collisionCheck(Auber auber, Infiltrator infiltrator, float range) {
        return ((((auber.getXPosition() - (((float) auber.width) / 2) - range) < (infiltrator.getXPosition() + ((float) infiltrator.width) / 2))
                && ((infiltrator.getXPosition() - ((float) infiltrator.width) / 2) < (auber.getXPosition() + range + ((float) auber.width) / 2)))
                && (((auber.getYPosition() - (((float) auber.height) / 2) - range) < (infiltrator.getYPosition() + ((float) infiltrator.height) / 2)))
                && ((infiltrator.getYPosition() - ((float) infiltrator.height) / 2) < (auber.getYPosition() + range + ((float) auber.height) / 2)));
    }

    /**
     * Deletes any infiltrators in range of the Auber
     *
     * @param auber - The entity to do collision checking on
     */
    public void checkCaptured(Auber auber) {
        float range = 0.1f;
        currentInfiltrators = currentInfiltrators.stream().filter(infiltrator -> !collisionCheck(auber, infiltrator, range)).collect(Collectors.toList());

    }


    /**
     * Attempts to spawn a new infiltrator
     *
     * @param room The map of valid room tiles
     */
    public void spawnInfiltrator(TiledMapTileLayer room) {
        if (spawnedInfiltrators < MAX_INFILTRATORS) {
            spawnedInfiltrators += 1;
            timeSinceLastSpawn = 0;
            currentInfiltrators.add(new Infiltrator(room, "inf_" + this.spawnedInfiltrators));
        }
    }

    /**
     * @return True if all infiltrators have been spawned and defeated
     */
    public boolean hasPlayerWon() {
        return (MAX_INFILTRATORS == spawnedInfiltrators && currentInfiltrators.size() == 0);
    }
}
