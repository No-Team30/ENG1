package com.team30.game.game_mechanics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.team30.game.Recording.Action;
import com.team30.game.Recording.ActionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Wrapper class for all infiltrators, and handles the movement and rendering of them
 */
public class InfiltratorContainer implements EntityContainer {
    /**
     * The maximum number of infiltrators to spawn
     */
    private static final int MAX_INFILTRATORS = 10;
    /**
     * The infiltrators that are currently "alive" on the map
     */
    private final HashMap<Integer, Infiltrator> currentInfiltrators;
    private final SystemContainer systemContainer;
    /**
     * The number of infiltrators that have been spawned so far
     */
    private int spawnedInfiltrators;
    /**
     * The time since an infiltrator was last spawned
     */
    private float timeSinceLastSpawn;

    /**
     * Stores all actions taken, in the current snapshot
     */
    private ArrayList<Action> recordedActions;

    public InfiltratorContainer(SystemContainer systemContainer) {
        this.spawnedInfiltrators = 0;
        this.timeSinceLastSpawn = 0;
        this.currentInfiltrators = new HashMap<>();
        this.recordedActions = new ArrayList<>();
        this.systemContainer = systemContainer;
    }


    @Override
    public Entity getEntity(ID id) {
        return null;
    }

    @Override
    public Vector2 getEntityPosition(ID id) {
        return null;
    }

    @Override
    public List<Entity> getAllEntities() {
        return null;
    }

    /**
     * Checks whether to spawn a new infiltrator <br>
     * Checks all active infiltrators and whether they need moving
     *
     * @param deltaTime The time passed, since this was last called
     * @param room      The map layer containing all valid room tiles
     */
    @Override
    public void calculatePosition(float deltaTime, TiledMapTileLayer room) {
        timeSinceLastSpawn += deltaTime;
        if (timeSinceLastSpawn > 10) {
            spawnInfiltrator(room);
        }
        for (Infiltrator infiltrator : currentInfiltrators.values()) {
            infiltrator.incrementTimeSinceLastUpdate(deltaTime);
            if (infiltrator.getTimeSinceLastUpdate() > 0.2) {
                infiltrator.moveInfiltrator(room, systemContainer);
                recordedActions.add(new Action(infiltrator.id, ActionType.Move, infiltrator.getXPosition(), infiltrator.getYPosition(), infiltrator.getXVelocity(), infiltrator.getYVelocity(), null));
                infiltrator.resetTimeSinceLastUpdate();
            }
        }
    }

    @Override
    public void updateMovements(float deltaTime, TiledMapTileLayer room) {
        for (Infiltrator infiltrator : currentInfiltrators.values()) {
            infiltrator.updatePosition(deltaTime, room);
        }
    }

    public void draw(Batch batch) {
        for (Infiltrator infiltrator : currentInfiltrators.values()) {
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
        if ((((auber.getXPosition() - (((float) auber.width) / 2) - range) < (infiltrator.getXPosition() + ((float) infiltrator.width) / 2))
                && ((infiltrator.getXPosition() - ((float) infiltrator.width) / 2) < (auber.getXPosition() + range + ((float) auber.width) / 2)))
                && (((auber.getYPosition() - (((float) auber.height) / 2) - range) < (infiltrator.getYPosition() + ((float) infiltrator.height) / 2)))
                && ((infiltrator.getYPosition() - ((float) infiltrator.height) / 2) < (auber.getYPosition() + range + ((float) auber.height) / 2))) {
            System.out.println("Captured auber: " + infiltrator.id);
            recordedActions.add(new Action(infiltrator.id, ActionType.Capture, infiltrator.getXPosition(), infiltrator.getYPosition(), infiltrator.getXVelocity(), infiltrator.getYVelocity(), null));
            return true;
        }
        return false;
    }

    /**
     * Deletes any infiltrators in range of the Auber
     *
     * @param auber - The entity to do collision checking on
     */
    public void checkCaptured(Auber auber) {
        float range = 0.1f;
        currentInfiltrators.entrySet().removeIf(infiltrator -> collisionCheck(auber, infiltrator.getValue(), range));

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
            Infiltrator newInfiltrator = new Infiltrator(room, "inf_" + this.spawnedInfiltrators);
            currentInfiltrators.put(newInfiltrator.id.ID, newInfiltrator);
            recordedActions.add(new Action(newInfiltrator.id, ActionType.Spawn, newInfiltrator.getXPosition(), newInfiltrator.getYPosition(), newInfiltrator.getXVelocity(), newInfiltrator.getYVelocity(), null));

        }
    }

    /**
     * Returns all actions that took place in this snapshot<br>
     * And resets the recording list
     */
    public ArrayList<Action> record() {
        ArrayList<Action> actions = new ArrayList<>(recordedActions);
        recordedActions = new ArrayList<>();
        return actions;
    }

    @Override
    public void applyAction(Action action) {
        switch (action.getActionType()) {
            case Move:
                applyMovementAction(action);
                break;
            case Spawn:
                System.out.println("Spawning infiltrator");
                Infiltrator newInfiltator = new Infiltrator(action.getId(), (int) action.getXPosition(), (int) action.getYPosition());
                currentInfiltrators.put(newInfiltator.id.ID, newInfiltator);
                break;
            case Damage:
                // TODO Hopefully not needed?
                break;
            case Capture:
                currentInfiltrators.remove(action.getId().ID);
                // TODO Waiting for capture logic
                break;
            default:
                break;
        }
    }

    /**
     * @return True if all infiltrators have been spawned and defeated
     */
    public boolean hasPlayerWon() {
        return (MAX_INFILTRATORS == spawnedInfiltrators && currentInfiltrators.size() == 0);
    }

    /**
     * Updates the current position and velocity to match the action
     *
     * @param action The action containing the new variables
     */
    public void applyMovementAction(Action action) {
        Infiltrator infiltrator =
                this.currentInfiltrators.get(action.getId().ID);
        if (infiltrator != null) {
            infiltrator.applyMovementAction(action);
        }
    }
}
