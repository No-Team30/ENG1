package com.team30.game.game_mechanics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.team30.game.game_mechanics.Auber;
import com.team30.game.game_mechanics.Infiltrator;


public class Invisible extends Infiltrator {
    /**
     * the time for invisible
     */
    private float invisibleTime = 3;


    public Invisible(TiledMapTileLayer roomTiles, String name) {
        super(roomTiles, name);
    }

    public Invisible(ID id, int xPosition, int yPosition) {
        super(id, xPosition, yPosition);
    }


    /**
     * invisible infiltrator draw
     */
    public void draw(Batch batch) {
        if (coolDown <= 0) {
            invisibleTime = 3;
            coolDown = coolDownTime;
        }

        if (invisibleTime > 0) {
            super.draw(batch);
        } else {
            return;
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
     
	

