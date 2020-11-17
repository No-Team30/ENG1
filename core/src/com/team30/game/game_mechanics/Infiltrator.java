package com.team30.game.game_mechanics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TreeMap;

public class Infiltrator extends Entity {
    /**
     * The amount of damage applied in "one" attack
     */
    public static final int DAMAGE_DEALT = 50;
    // TODO Convert to an ID
    public String name;
    /**
     * The list of movements to take
     */
    Queue<Movements> moves;
    private ID targetSystem;

    /**
     * The time since the movements of the infiltrators were last updated
     */
    private float timeSinceLastUpdate;

    /**
     * Spawns a new infiltrator at a random position
     *
     * @param roomTiles The map of valid tiles
     */
    public Infiltrator(TiledMapTileLayer roomTiles, String name) {
        super(new ID(EntityType.Infiltrator), new Texture(("Infiltrator.png")), roomTiles, 1, 1);
        this.name = name;
        this.targetSystem = null;
        this.timeSinceLastUpdate = 0f;
        moves = new LinkedList<>();
        System.out.println("Spawned infiltrator:" + this.name + " at: " + this.position.toString());
    }

    /**
     * Spawns a new NPC with the given ID, and at the given position
     *
     * @param id        The ID of the NPC
     * @param xPosition The x coordinate to spawn on
     * @param yPosition The y coordinate to spawn on
     */
    // TODO Check for clashing ID'S?
    public Infiltrator(ID id, int xPosition, int yPosition) {
        super(id, new Texture(("NPC.png")), xPosition, yPosition, 1, 1);
        this.name = null;
        this.targetSystem = null;
        this.timeSinceLastUpdate = 0f;
        moves = new LinkedList<>();
        System.out.println("Spawned infiltrator:" + this.name + " at: " + this.position.toString());
    }

    public float getTimeSinceLastUpdate() {
        return timeSinceLastUpdate;
    }

    public void incrementTimeSinceLastUpdate(float incrementTime) {
        this.timeSinceLastUpdate += incrementTime;
    }

    public void resetTimeSinceLastUpdate() {
        this.timeSinceLastUpdate = 0;
    }


    /**
     * Returns the vector containing the direction to the closest attackable system
     *
     * @param position        The position to start from
     * @param systemContainer The container with positions of all active systems
     * @return Vector2 The direction of the target system
     */
    public static Vector2 getClosestSystemVect(Vector2 position, SystemContainer systemContainer) {
        float minDistance = Float.MAX_VALUE;
        Vector2 direction = new Vector2();
        for (ID id : systemContainer.getAttackableSystems()) {
            GameSystem system = (GameSystem) systemContainer.getEntity(id);
            float currentDistance = position.dst(system.position);
            if (currentDistance < minDistance) {
                minDistance = currentDistance;
                direction = system.position.cpy().sub(position);
            }
        }
        return direction;
    }

    /**
     * Returns the closest system that is active and not on cool down
     *
     * @param position        The position to start from
     * @param systemContainer The container with positions of all active systems
     *                        //TODO Find out how to properly document nullable
     * @return ID    The ID of the target system (Could be null if no systems are found!)
     */
    public static ID getClosestSystem(Vector2 position, SystemContainer systemContainer) {
        float minDistance = Float.MAX_VALUE;
        ID closestSystem = null;
        for (ID id : systemContainer.getAttackableSystems()) {
            float currentDistance = position.dst(systemContainer.getEntityPosition(id));
            if (currentDistance < minDistance) {
                minDistance = currentDistance;
                closestSystem = id;
            }
        }
        return closestSystem;
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

    /**
     * Executes the next move, on its path
     * If the path is empty, triggers damaging the system
     *
     * @param room    The map of valid room tiles
     * @param systems The location of all systems
     * @return True if it can start damaging a system
     */
    public boolean moveInfiltrator(TiledMapTileLayer room, SystemContainer systems) {
        if (moves.isEmpty() && targetSystem == null) {
            Node node = getMove(room, systems);
            moves = node.exportPath();
        }
        this.velocity.x = 0;
        this.velocity.y = 0;
        if (targetSystem != null) {
            systems.applyDamage(this.id, targetSystem, DAMAGE_DEALT);
            if (!systems.getActiveSystems().contains(targetSystem)) {
                targetSystem = null;
                //TODO look at moving the infiltrator away from just attacked system to avoid detection and make game harder
            }
        } else if (!moves.isEmpty()) {
            Movements move = moves.remove();
            this.velocity.add(getMovement(move)).scl(this.MAX_VELOCITY);
            // We have reached the target system
            if (moves.isEmpty()) {
                targetSystem = getClosestSystem(position, systems);
                System.out.println("At target system: " + targetSystem);
            }
        }
        return false;
    }

    /**
     * Uses A* to find the closest system to the infiltrator
     *
     * @param room    The map of valid room tiles
     * @param systems The location of all systems
     * @return The node of the path to the closest system
     */
    private Node getMove(TiledMapTileLayer room, SystemContainer systems) {
        TreeMap<Float, Node> possibleMoves = new TreeMap<>();
        LinkedList<Vector2> visited = new LinkedList<>();

        Node currentNode = new Node(null, position, null, 0, 0);
        possibleMoves.put(currentNode.getHeuristic(), currentNode);

        while (!possibleMoves.isEmpty()) {
            currentNode = possibleMoves.remove(possibleMoves.firstKey());
            visited.add(currentNode.getPosition());
            // Checks if we are at a system
            Vector2 closestSystem = getClosestSystemVect(currentNode.getPosition(), systems);
            if (closestSystem.len() < 1) {
                return currentNode;
            }
            // Add the valid children to the queue
            for (Movements move : currentNode.getValidMoves(room)) {
                Vector2 position = currentNode.getPosition().cpy().add(getMovement(move));
                if (!visited.contains(position)) {
                    int cost = currentNode.getCost() + 1;
                    float heuristic = getClosestSystemVect(position, systems).len();
                    possibleMoves.put(heuristic + cost, new Node(currentNode, position, move, cost, heuristic));
                }
            }

        }
        return currentNode;

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

/**
 * Helper class for A* algorithm
 */
class Node {
    private final Node parent;
    private final Vector2 position;
    private final Infiltrator.Movements move;
    private final int cost;
    private final float heuristic;

    public Node(Node parent, Vector2 position, Infiltrator.Movements move, int cost, float heuristic) {
        this.parent = parent;
        this.position = position;
        this.move = move;
        this.cost = cost;
        this.heuristic = heuristic;
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
    public ArrayList<Infiltrator.Movements> getValidMoves(TiledMapTileLayer room) {
        ArrayList<Infiltrator.Movements> moves = new ArrayList<>();
        for (Infiltrator.Movements movement : Infiltrator.Movements.values()) {
            Vector2 newPosition = position.cpy().add(Infiltrator.getMovement(movement));
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
    public Queue<Infiltrator.Movements> exportPath() {
        // At the top of the tree
        if (move == null) {
            return new LinkedList<>();
        }
        Queue<Infiltrator.Movements> moves = this.parent.exportPath();
        moves.add(this.move);
        return moves;
    }
}


