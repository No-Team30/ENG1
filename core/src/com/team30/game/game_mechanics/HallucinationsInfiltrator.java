package com.team30.game.game_mechanics;


import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class HallucinationsInfiltrator extends Infiltrator {


    public HallucinationsInfiltrator(TiledMapTileLayer roomTiles, String name) {
        super(roomTiles, name);
    }

    public HallucinationsInfiltrator(ID id, int xPosition, int yPosition) {
        super(id, xPosition, yPosition);
    }
}