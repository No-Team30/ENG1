package com.team30.game.game_mechanics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class Auber extends Movement {
    public Auber(TiledMapTileLayer roomTiles) {
        super(new Texture("Auber.png"), roomTiles, 1, 1);
        this.VELOCITY_CHANGE = 2f;
        this.MAX_VELOCITY *= 1.5;
    }
}
