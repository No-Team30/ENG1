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
    private final HashMap<ID, Infiltrator> currentInfiltrators;
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
     * Attempts to spawn a new infiltrator
     *
     * @param room The map of valid room tiles
     */
    public void spawnInfiltrator(TiledMapTileLayer room) {
        if (spawnedInfiltrators < MAX_INFILTRATORS) {
            spawnedInfiltrators += 1;
            timeSinceLastSpawn = 0;
            Infiltrator newInfiltrator = new Infiltrator(room, "inf_" + this.spawnedInfiltrators);
            currentInfiltrators.put(newInfiltrator.id, newInfiltrator);
            recordedActions.add(new Action(newInfiltrator.id, ActionType.Move, newInfiltrator.getXPosition(), newInfiltrator.getYPosition(), newInfiltrator.getXVelocity(), newInfiltrator.getYVelocity(), null));

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
                Infiltrator newInfiltator = new Infiltrator(action.getId(), (int) action.getXPosition(), (int) action.getYPosition());
                currentInfiltrators.put(newInfiltator.id, newInfiltator);
                break;
            case Damage:
                // TODO Hopefully not needed?
                break;
            case Capture:
                // TODO Waiting for capture logic
                break;
            default:
                break;
        }
    }

    /**
     * Updates the current position and velocity to match the action
     *
     * @param action The action containing the new variables
     */
    public void applyMovementAction(Action action) {
        this.currentInfiltrators.get(action.getId()).applyMovementAction(action);
    }
}
