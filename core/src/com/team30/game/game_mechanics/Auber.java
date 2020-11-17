package com.team30.game.game_mechanics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class Auber extends Entity {
    public Auber(TiledMapTileLayer roomTiles) {
        super(new ID(EntityType.Auber), new Texture("Auber.png"), roomTiles, 1, 1);
        this.VELOCITY_CHANGE = 2f;
    }
}
