package com.team30.game.game_mechanics.Infiltrators;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.team30.game.game_mechanics.ID;


public class Invisible extends Infiltrator {
    /**
     * the time for invisible
     */
    private float invisibleTime = 3;


    public Invisible(TiledMapTileLayer roomTiles) {
        super(roomTiles);
        this.infiltratorType = InfiltratorType.Invisible;
        System.out.println("Spawning invisible infiltrator");
    }

    public Invisible(ID id, float xPosition, float yPosition) {
        super(id, xPosition, yPosition);
        this.infiltratorType = InfiltratorType.Invisible;
    }

    /**
     * invisible infiltrator draw
     */
    @Override
    public void draw(Batch batch) {
        if (coolDown <= 0) {
            invisibleTime = 3;
            coolDown = coolDownTime;
        }
        if (invisibleTime > 0) {
            super.draw(batch);
        }

    }

    /**
     * Update invisible time
     *
     * @param deltaTime The time since last update
     * @param room      The room layer for collision detection
     */
    @Override
    public void updatePosition(float deltaTime, TiledMapTileLayer room) {
        super.updatePosition(deltaTime, room);
        if (invisibleTime >= 0) {
            invisibleTime -= deltaTime;
            return;
        }
        invisibleTime = 0;
    }
}
     
	

