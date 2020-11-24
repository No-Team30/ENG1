package com.team30.game.game_mechanics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import java.util.List;
import java.util.Random;

public class Npc extends Entity {
    /**
     * Spawns a new NPC at a random position
     *
     * @param room The map layer of valid room tiles
     */
    public Npc(TiledMapTileLayer room) {
        super(new ID(EntityType.Npc), new Texture(("NPC.png")), room, 1, 1);
    }

    /**
     * Spawns a new NPC with the given ID, and at the given position
     *
     * @param id        The ID of the NPC
     * @param xPosition The x coordinate to spawn on
     * @param yPosition The y coordinate to spawn on
     */
    public Npc(ID id, int xPosition, int yPosition) {
        super(id, new Texture(("NPC.png")), xPosition, yPosition, 1, 1);
    }


    /**
     * Sets the velocity for the npc in a random direction
     *
     * @param roomTiles The map of valid tiles
     */
    public void calculateNewVelocity(TiledMapTileLayer roomTiles) {
        Node node = new Node(position);
        List<Node.Movements> possilbeMoves = node.getValidMoves(roomTiles);
        Random random = new Random();
        this.velocity = Node.getMovement(possilbeMoves.get(random.nextInt(possilbeMoves.size())));
    }

}
