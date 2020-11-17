package com.team30.game.game_mechanics;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.team30.game.Recording.Action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles all concurrent npcs, and rendering of them
 */
public class NpcContainer implements EntityContainer {
    private static final int NPC_AMOUNT = 50;
    private final HashMap<ID, Npc> npcs;

    public NpcContainer(TiledMapTileLayer room) {
        npcs = new HashMap<>();
        for (int index = 0; index < NPC_AMOUNT; index++) {
            Npc npc = new Npc(room);
            npcs.put(npc.id, npc);
        }
    }

    @Override
    public Entity getEntity(ID id) {
        return npcs.get(id);
    }

    @Override
    public Vector2 getEntityPosition(ID id) {
        return getEntity(id).getPosition().cpy();
    }

    @Override
    public List<Entity> getAllEntities() {
        return npcs.values().stream().map(e -> (Entity) e).collect(Collectors.toList());
    }

    @Override
    public void calculatePosition(float deltaTime, TiledMapTileLayer room) {
        for (Npc npc : npcs.values()) {
            npc.calculateNewVelocity();
        }
    }

    @Override
    public void updateMovements(float deltaTime, TiledMapTileLayer room) {
        for (Npc npc : npcs.values()) {
            npc.updatePosition(deltaTime, room);
        }
    }

    /**
     * Renders all active NPC's
     *
     * @param batch Where to render the textures
     */
    public void draw(Batch batch) {
        for (Npc npc : npcs.values()) {
            npc.draw(batch);
        }
    }

    @Override
    public ArrayList<Action> record() {
        return null;
    }

    @Override
    public void applyAction(Action action) {

    }

    public void applyMovementAction(Action action) {
        this.npcs.get(action.getId()).applyMovementAction(action);
    }
}
