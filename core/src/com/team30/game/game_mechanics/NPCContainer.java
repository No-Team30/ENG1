package com.team30.game.game_mechanics;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.team30.game.Recording.Action;
import com.team30.game.Recording.ActionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles all concurrent npcs, and rendering of them
 */
public class NPCContainer implements EntityContainer {
    private static final int NPC_AMOUNT = 5;
    private final HashMap<Integer, NPC> npcs;
    private ArrayList<Action> recordedActions;
    private final float timeSinceMove;

    public NPCContainer() {
        npcs = new HashMap<>();
        recordedActions = new ArrayList<Action>();
        timeSinceMove = 0f;

    }

    /**
     * Spawns the maximum number of entities on random room tiles
     *
     * @param room The map layer of valid room tiles
     */
    public void spawnNpcs(TiledMapTileLayer room) {
        for (int index = 0; index < NPC_AMOUNT; index++) {
            NPC npc = new NPC(room);
            npcs.put(npc.id.ID, npc);
            recordedActions.add(new Action(npc.id, ActionType.Spawn, npc.getXPosition(), npc.getYPosition(), npc.getXVelocity(), npc.getYVelocity(), null));
        }
    }

    @Override
    public Entity getEntity(ID id) {
        return npcs.get(id.ID);
    }

    @Override
    public Vector2 getEntityPosition(ID id) {
        return getEntity(id).getPosition().cpy();
    }

    @Override
    public List<Entity> getAllEntities() {
        return npcs.values().stream().map(e -> (Entity) e).collect(Collectors.toList());
    }

    /**
     * For pure random direction
     * //TODO Can probably remove
     *
     * @param deltaTime
     * @param room
     */
    @Override
    public void calculatePosition(float deltaTime, TiledMapTileLayer room) {
        for (NPC npc : npcs.values()) {
            npc.incrementTimeSinceLastUpdate(deltaTime);
            if (npc.getTimeSinceLastUpdate() > 0.1) {
                npc.calculateNewVelocity(room);
                recordedActions.add(new Action(npc.id, ActionType.Move, npc.getXPosition(), npc.getYPosition(), npc.getXVelocity(), npc.getYVelocity(), null));

                npc.resetTimeSinceLastUpdate();
            }
        }
    }

    @Override
    public void updateMovements(float deltaTime, TiledMapTileLayer room) {
        for (NPC npc : npcs.values()) {
            npc.updatePosition(deltaTime, room);
        }
    }

    /**
     * Renders all active NPC's
     *
     * @param batch Where to render the textures
     */
    public void draw(Batch batch) {
        for (NPC npc : npcs.values()) {
            npc.draw(batch);
        }
    }

    @Override
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
                NPC newNPC = new NPC(action.getId(), (int) action.getXPosition(), (int) action.getYPosition());
                npcs.put(action.getId().ID, newNPC);
                break;

            default:
                break;
        }
    }

    public void applyMovementAction(Action action) {
        NPC npc =
                this.npcs.get(action.getId().ID);
        if (npc != null) {
            npc.applyMovementAction(action);
        } else {
            System.out.println("Don't exist:" + action.getId());
        }
    }
}
