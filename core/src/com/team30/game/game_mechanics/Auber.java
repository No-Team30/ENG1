package com.team30.game.game_mechanics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.team30.game.game_mechanics.Infiltrators.Hallucinogenic;
import com.team30.game.game_mechanics.Infiltrators.Infiltrator;

public class Auber extends Entity {
    /**
     * The time for hallucination
     */
    public float hallucinationTime;


    public Auber(TiledMapTileLayer roomTiles) {
        super(new ID(EntityType.Auber), new Texture("Auber.png"), roomTiles, 1, 1);
        this.VELOCITY_CHANGE = 2f;
        this.MAX_VELOCITY *= 1.5;
    }

    /**
     * Attempts to move to a new cell (from the current velocity), if all corners are inside room tiles,when hallucinationTime is zero.
     *
     * @param deltaTime The time since last update
     * @param room      The room layer for collision detection
     */
    @Override
    public void updatePosition(float deltaTime, TiledMapTileLayer room) {
        hallucinationTime -= deltaTime;
        if (hallucinationTime > 0) {
            setXVelocity(0);
            setYVelocity(0);
            return;
        }
        hallucinationTime = 0;
        super.updatePosition(deltaTime, room);
    }

    /**
     * Get Hallucinations ability
     * if Auber is nearby and cooldown is over, infiltrator will use the ability
     *
     * @param infiltrator To create a infiltrator get coolDownTime
     */
    public void getHallucinations(Infiltrator infiltrator) {
        hallucinationTime = 2;
        infiltrator.coolDown = infiltrator.coolDownTime;
    }

    /**
     * Check hallucinations
     */
    public void checkHallucinations(TiledMapTileLayer room, InfiltratorContainer infiltrators) {

        //get Infiltrators around auber
        for (Infiltrator infiltrator : infiltrators.getAllInfiltrators()) {
            if (infiltrator instanceof Hallucinogenic && infiltrator.coolDown <= 0 && infiltrators.collisionCheck(this, infiltrator, 1)) {
                getHallucinations(infiltrator);
            }
        }
    }
}

