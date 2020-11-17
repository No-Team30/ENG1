package com.team30.game.game_mechanics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import java.util.Random;

public class Npc extends Entity {
    public Npc(TiledMapTileLayer room) {
        super(new ID(IDType.Npc), new Texture(("NPC.png")), 0, 0, 1, 1);
        this.moveRandomCell(room);
    }


    /**
     * Sets the velocity for the npc in a random direction
     */
    public void calculateNewVelocity() {
        Random rand = new Random();
        this.velocity.x += (((float) rand.nextInt((int) (this.VELOCITY_CHANGE * 100))) / 100) - (this.VELOCITY_CHANGE / 2);
        this.velocity.y += (((float) rand.nextInt((int) (this.VELOCITY_CHANGE * 100))) / 100) - (this.VELOCITY_CHANGE / 2);
    }
}
