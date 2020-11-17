package com.team30.game.game_mechanics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.team30.game.Recording.Action;
import com.team30.game.Recording.ActionType;
import com.team30.game.screen.GameScreen;

import java.util.*;

/**
 * Handles all systems, and rendering of them
 */
public class SystemContainer implements EntityContainer {
    /**
     * All systems in the ship
     */
    private final HashMap<ID, GameSystem> systems;
    /**
     * The ID's of all systems that are not destroyed
     */
    private final Set<ID> activeSystems;
    /**
     * The ID'S of all systems that can be attacked
     * (Cooldown has expired)
     */
    private final Set<ID> attackableSystems;

    /**
     * Stores all actions taken, in the current snapshot
     */
    private ArrayList<Action> recordedActions;

    /**
     * Builds all systems from the map
     * Each object should have these properties:
     * name
     * x
     * y
     * width
     * height
     * health
     * active
     *
     * @param map The object layer containing systems
     */
    public SystemContainer(MapLayer map) {
        this.systems = new HashMap<>();
        this.activeSystems = new HashSet<>();
        this.attackableSystems = new HashSet<>();
        this.recordedActions = new ArrayList<>();
        for (MapObject object : map.getObjects()) {
            MapProperties properties = object.getProperties();

            // Check all required fields are valid
            Object xPosObject = properties.get("x");
            Object yPosObject = properties.get("y");
            Object widthObject = properties.get("width");
            Object heightObject = properties.get("height");
            if (xPosObject == null || yPosObject == null || widthObject == null || heightObject == null) {
                //TODO Convert to error logging
                System.out.println("ERROR MapObject is missing required fields!!!");
                continue;
            }

            int x_pos = ((int) (float) xPosObject) / GameScreen.TILE_SIZE;
            int y_pos = ((int) (float) yPosObject) / GameScreen.TILE_SIZE;
            int width = ((int) (float) widthObject) / GameScreen.TILE_SIZE;
            int height = ((int) (float) heightObject) / GameScreen.TILE_SIZE;
            GameSystem system = new GameSystem(object.getName(), x_pos, y_pos, width, height, 100);
            this.systems.put(system.id, system);
            this.activeSystems.add(system.id);
            this.attackableSystems.add(system.id);
        }

    }

    @Override
    public Entity getEntity(ID id) {
        return systems.get(id);
    }


    public Vector2 getEntityPosition(ID id) {
        return systems.get(id).getPosition();
    }

    @Override
    public List<Entity> getAllEntities() {
        return null;
    }

    /**
     * @return The ArrayList of currently active systems
     */
    // TODO? consider updating when systems are damaged
    public Set<ID> getActiveSystems() {
        return activeSystems;
    }


    /**
     * @return The list of currently active systems that can aren't on cool down
     */
    // TODO? consider updating when systems are damaged
    public Set<ID> getAttackableSystems() {
        return attackableSystems;
    }

    /**
     * @return Gets the amount of systems with health > 0
     */
    public int getAmountOfActiveSystems() {
        return activeSystems.size();
    }

    /**
     * Applies the given damage to a system, if the cooldown has expired
     *
     * @param attackerID The id of the attacker
     * @param systemID   The id of the system to damage
     * @param damage     The amount of damage to deal
     */
    public void applyDamage(ID systemID, ID attackerID, int damage) {
        GameSystem system = systems.get(systemID);
        if (system.getCoolDown() == 0) {
            if (system.applyDamage(damage) == 0) {
                this.attackableSystems.remove(systemID);
                this.activeSystems.remove(systemID);
            }
            this.recordedActions.add(new Action(systemID, ActionType.SystemDamage, system.getXPosition(), system.getYPosition(), system.getXVelocity(), system.getYVelocity(), attackerID));
        }
    }

    @Override
    public void calculatePosition(float deltaTime, TiledMapTileLayer room) {
    }

    /**
     * Refreshes the cooldown on all active systems<br>
     * And if the cooldown has expired, adds it to the attackable systems
     *
     * @param deltaTime takes the time passed since last updated
     */
    @Override
    public void updateMovements(float deltaTime, TiledMapTileLayer room) {
        for (ID id : activeSystems) {
            GameSystem system = systems.get(id);
            if (system.updateCoolDown(deltaTime)) {
                this.attackableSystems.add(id);
            }
        }
    }

    @Override
    public void draw(Batch batch) {
        // Not required as textures are in map layer 0
    }

    /**
     * Returns all actions that took place in this snapshot<br>
     * And resets the recording list
     */
    public ArrayList<Action> record() {
        ArrayList<Action> actions = new ArrayList<>(recordedActions);
        recordedActions = new ArrayList<>();
        System.out.println(actions);
        return actions;
    }

    @Override
    public void applyAction(Action action) {

    }
}
