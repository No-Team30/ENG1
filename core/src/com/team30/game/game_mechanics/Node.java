package com.team30.game.game_mechanics;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;


/**
 * Helper class for A* algorithm<br>
 * Basically checks if nearby tiles are accessible
 */
class Node {
    private final Node parent;
    private final Vector2 position;
    private final Movements move;
    private final int cost;
    private final float heuristic;

    public Node(Node parent, Vector2 position, Movements move, int cost, float heuristic) {
        this.parent = parent;
        this.position = position;
        this.move = move;
        this.cost = cost;
        this.heuristic = heuristic;
    }

    public Node(Vector2 position) {
        this.position = position;
        this.parent = null;
        this.move = null;
        this.cost = 0;
        this.heuristic = 0;
    }

    /**
     * Converts a Movements enum to a vector
     *
     * @param move The enum to convert
     * @return A Vector2 of the movement
     */
    public static Vector2 getMovement(Movements move) {
        switch (move) {
            case LEFT:
                return new Vector2(-1, 0);
            case RIGHT:
                return new Vector2(1, 0);
            case UP:
                return new Vector2(0, 1);
            case DOWN:
                return new Vector2(0, -1);
        }
        return null;
    }

    public Vector2 getPosition() {
        return position;
    }

    public int getCost() {
        return cost;
    }

    public float getHeuristic() {
        return heuristic;
    }

    /**
     * Checks the 4 neighbouring cells (In the given Movements Enum), to see if they are valid room tiles<br>
     * And returns the valid ones
     *
     * @param room The map containing valid room tiles
     * @return The list of valid movements
     */
    public ArrayList<Movements> getValidMoves(TiledMapTileLayer room) {
        ArrayList<Movements> moves = new ArrayList<>();
        for (Movements movement : Movements.values()) {
            Vector2 newPosition = position.cpy().add(getMovement(movement));
            if (room.getCell((int) newPosition.x, (int) newPosition.y) != null) {
                moves.add(movement);
            }
        }
        return moves;
    }

    /**
     * Backtracks up the tree, returning the path of movements to get to the target
     *
     * @return A queue of movements
     */
    public Queue<Movements> exportPath() {
        // At the top of the tree
        if (move == null) {
            return new LinkedList<>();
        }
        if (parent != null) {
            Queue<Movements> moves = this.parent.exportPath();
            moves.add(this.move);
            return moves;
        }
        return new LinkedList<>();
    }

    /**
     * Represents all possible movement directions
     */
    public enum Movements {
        LEFT,
        RIGHT,
        UP,
        DOWN
    }
}


