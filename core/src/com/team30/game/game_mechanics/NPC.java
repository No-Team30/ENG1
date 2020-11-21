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
    // TODO Check for clashing ID'S?
    public Npc(ID id, int xPosition, int yPosition) {
        super(id, new Texture(("NPC.png")), xPosition, yPosition, 1, 1);
    }


    /**
     * Sets the velocity for the npc in a random direction
     */
    public void calculateNewVelocity(TiledMapTileLayer room) {
        Node node = new Node(position);
        List<Node.Movements> possilbeMoves = node.getValidMoves(room);
        Random random = new Random();
        this.velocity = Node.getMovement(possilbeMoves.get(random.nextInt(possilbeMoves.size())));
/*        Random rand = new Random();
        this.velocity.x += (((float) rand.nextInt((int) (this.VELOCITY_CHANGE * 100))) / 100) - (this.VELOCITY_CHANGE / 2);
        this.velocity.y += (((float) rand.nextInt((int) (this.VELOCITY_CHANGE * 100))) / 100) - (this.VELOCITY_CHANGE / 2);*/
    }

}
