package com.team30.game.game_mechanics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.team30.game.Recording.Action;

import java.util.ArrayList;
import java.util.List;

public interface EntityContainer {

    /**
     * Returns the Entity matching the provided ID
     *
     * @param id The id of the entity to retrieve
     * @return The entity instance
     */
    Entity getEntity(ID id);

    /**
     * Returns a copy of the position of the entity
     *
     * @param id The id of the entity to locate
     * @return The position of the entity
     */
    Vector2 getEntityPosition(ID id);

    /**
     * Returns all known entities
     *
     * @return A list of  Entities
     */
    List<Entity> getAllEntities();

    /**
     * Calculates the position of every active entity
     * from the entities velocity.
     */
    void calculatePosition(float deltaTime, TiledMapTileLayer room);

    /**
     * This is where the next movements for entities are calculated, and velocity is altered
     */
    void updateMovements(float deltaTime, TiledMapTileLayer room);

    /**
     * Draws all active entities
     *
     * @param batch The batch to render to
     */
    void draw(Batch batch);

    /**
     * Returns all actions that took place in this snapshot<br>
     * And resets the recording list
     *
     * @return The actions that took place
     */
    ArrayList<Action> record();

    /**
     * Applies the given action from a snapshot, to the relevant entity
     *
     * @param action The action to apply
     */
    void applyAction(Action action);
}
