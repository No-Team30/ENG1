package com.team30.game.game_mechanics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

/**
 * Handles updating position and collision detection
 */
public class Movement {
    /**
     * The default max velocity of a entity
     */
    public float MAX_VELOCITY = 5f;
    /**
     * The amount velocity increases, when input is recorded
     */
    public float VELOCITY_CHANGE = 2f;
    /**
     * The current position of the entity
     */
    public Vector2 position;
    /**
     * The current movement of the entity
     */
    public Vector2 velocity;
    /**
     * The width of the entity (in grid tiles)
     */
    public int width;
    /**
     * The height of the entity (in grid tiles)
     */
    public int height;
    /**
     * The texture used to render the entity
     */
    private TextureRegion region;

    /**
     * Creates a new entity at a random position
     *
     * @param texture   The texture of the given entity
     * @param roomTiles The map layer of valid room cells
     * @param width     The width of the entity
     * @param height    The height of the entity
     */
    public Movement(Texture texture, TiledMapTileLayer roomTiles, int width, int height) {
        if (texture != null) {
            this.region = new TextureRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());
        }
        this.width = width;
        this.height = height;

        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.moveRandomCell(roomTiles);
    }

    /**
     * Creates a new entity at the given position
     *
     * @param texture The texture of the given entity
     * @param xPos    The x coordinate of the entity
     * @param yPos    The y coordinate of the entity
     * @param width   The width of the entity
     * @param height  The height of the entity
     */
    public Movement(Texture texture, int xPos, int yPos, int width, int height) {
        if (texture != null) {
            this.region = new TextureRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());
        }
        this.width = width;
        this.height = height;

        this.position = new Vector2(xPos, yPos);
        this.velocity = new Vector2(0, 0);
    }

    /**
     * Moves the current entity to a random valid room cell
     *
     * @param roomTiles The map of valid roomTiles cells
     */
    public void moveRandomCell(TiledMapTileLayer roomTiles) {
        Random rand = new Random();
        int x = rand.nextInt(roomTiles.getWidth());
        int y = rand.nextInt(roomTiles.getHeight());
        while (roomTiles.getCell(x, y) == null) {
            x = rand.nextInt(roomTiles.getWidth());
            y = rand.nextInt(roomTiles.getHeight());
        }
        this.position.x = x;
        this.position.y = y;
    }

    public void draw(Batch batch) {
        batch.draw(region, getXPosition(), getYPosition(), width / 2f, height / 2f, width, height, 1f, 1f, velocity.angle() + 90);
    }

    /**
     * Draws the invisible infiltrator
     */
    public void drawInvisible(Batch batch, SpecialAbilities invisible) {
        batch.draw(region, getXPosition(), getYPosition(), width / 2f, height / 2f, width, height, 1f, 1f, velocity.angle() + 90);
        invisible.draw(batch);
    }
    /**
     * Attempts to move to a new cell, if all corners are inside room tiles
     *
     * @param deltaTime The time since last update
     * @param room      The room layer for collision detection
     */
    public void updatePosition(float deltaTime, TiledMapTileLayer room) {
        if (deltaTime == 0) return;

        if (deltaTime > 0.1f) {
            deltaTime = 0.1f;
        }
        // Limit the maximum speed
        velocity.clamp(-this.MAX_VELOCITY, this.MAX_VELOCITY);

        // Scale for time passed
        velocity.scl(deltaTime);

        //Check all corners are in room tiles
        // Bottom Left Corner
        if (room.getCell((int) (getXPosition() + getXVelocity()), (int) (getYPosition() + getYVelocity())) == null) {
            velocity.scl((1 / deltaTime));
            return;
        }
        // Bottom Right Corner
        if (room.getCell((int) (getXPosition() + getXVelocity() + width), (int) (getYPosition() + getYVelocity())) == null) {
            velocity.scl((1 / deltaTime));
            return;
        }
        // Top Left Corner
        if (room.getCell((int) (getXPosition() + getXVelocity()), (int) (getYPosition() + getYVelocity()) + height) == null) {
            velocity.scl((1 / deltaTime));
            return;
        }
        // Top Right Corner
        if (room.getCell((int) (getXPosition() + getXVelocity() + width), (int) (getYPosition() + getYVelocity()) + height) == null) {
            velocity.scl((1 / deltaTime));
            return;
        }
        position.add(velocity);
        velocity.scl((1 / deltaTime));
    }

    public float getXPosition() {
        return position.x;
    }

    public float getYPosition() {
        return position.y;
    }

    public float getXVelocity() {
        return velocity.x;
    }

    public float getYVelocity() {
        return velocity.y;
    }

    public void setXVelocity(float velocity) {
        this.velocity.x = velocity;
    }

    public void setYVelocity(float velocity) {
        this.velocity.y = velocity;
    }

    public void setXPosition(float xPosition) {
        this.position.x = xPosition;
    }

    public void setYPosition(float yPosition) {
        this.position.y = yPosition;
    }
}
