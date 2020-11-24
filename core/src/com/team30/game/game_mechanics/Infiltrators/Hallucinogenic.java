package com.team30.game.game_mechanics.Infiltrators;


import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.team30.game.game_mechanics.ID;

public class Hallucinogenic extends Infiltrator {

    public Hallucinogenic(TiledMapTileLayer roomTiles, String name) {
        super(roomTiles, name);
        this.infiltratorType = InfiltratorType.Hallucinogenic;
        System.out.println("Spawning hallucinatory infiltrator");
    }

    public Hallucinogenic(ID id, float xPosition, float yPosition) {
        super(id, xPosition, yPosition);
        this.infiltratorType = InfiltratorType.Hallucinogenic;
        System.out.println("Spawning hallucinatory infiltrator");

    }
}