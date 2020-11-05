package com.team30.game.game_mechanics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

/**
 * Handles updating position and collision detection
 */
public class Movement {
    public float VELOCITY_CHANGE = 2f;
    public final float MAX_VELOCITY = 5f;
    public Vector2 position;
    public Vector2 velocity;
    public int width;
    public int height;
    public Texture texture;

    public Movement(Texture texture, float x, float y, int width, int height) {
        this.texture = texture;
        this.width = width;
        this.height = height;

        this.position = new Vector2(x, y);
        this.velocity = new Vector2(0, 0);

    }

    public void draw(Batch batch) {
        batch.draw(texture, getXPosition(), getYPosition(), width, height);
    }

    /**
     * Attempts to move to a new cell, if all corners are inside room tiles
     *
     * @param deltaTime The time since last update
     * @param room      The room layer for collision detection
     */
    public void updateAuber(float deltaTime, TiledMapTileLayer room) {
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
}
