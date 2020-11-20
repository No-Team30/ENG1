package com.team30.game.game_mechanics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TreeMap;

public class Infiltrator extends Movement {
    /**
     * The amount of damage applied in "one" attack
     */
    public static final int damageDealt = 50;
    // TODO Convert to an ID
    public String name;
    /**
     * The time for coolDown special ability
     */
    public int coolDown;
    /**
     * The time for Auber hallucination
     */
    public int hallucinationTime;
    /**
     * The list of movements to take
     */
    Queue<Movements> moves;
    private GameSystem targetSystem;

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
        super(new Texture(("Infiltrator.png")), roomTiles, 1, 1);
        this.name = name;
        this.targetSystem = null;
        this.timeSinceLastUpdate = 0f;
        moves = new LinkedList<>();
        System.out.println("Spawned infiltrator:" + this.name + " at: " + this.position.toString());
    }
    /**
     * Spawns a new infiltrator at a random position and has hallucination ability
     *
     * @param roomTiles The map of valid tiles
     * @param coolDown  The time for coolDown special ability
     * @param hallucinationTime The time for Auber hallucination
     */
    public Infiltrator(TiledMapTileLayer roomTiles, String name, int coolDown, int hallucinationTime) {
        super(new Texture(("Infiltrator.png")), roomTiles, 1, 1);
        if (coolDown > hallucinationTime){
            this.name = name;
            this.targetSystem = null;
            this.coolDown = coolDown;
            this.hallucinationTime = hallucinationTime;
            moves = new LinkedList<>();
            System.out.println("Spawned infiltrator:" + this.name + " at: " + this.position.toString());
        }
        else {
            throw new Exception("Time for coolDown should longer than hallucinationTime");
        }
    }

    /**
     * Spawns a new infiltrator at a random position and has faster speed
     *
     * @param roomTiles The map of valid tiles
     * @param VELOCITY_CHANGE Higher velocity change for special infiltrator
     * @param MAX_VELOCITY Higher Max velocity for special infiltrator
     */
    public Infiltrator(TiledMapTileLayer roomTiles, String name, float VELOCITY_CHANGE, float MAX_VELOCITY) {
        super(new Texture(("Infiltrator.png")), roomTiles, 1, 1);
        this.name = name;
        this.targetSystem = null;
        this.VELOCITY_CHANGE = VELOCITY_CHANGE;
        this.MAX_VELOCITY = MAX_VELOCITY;
        moves = new LinkedList<>();
        System.out.println("Spawned infiltrator:" + this.name + " at: " + this.position.toString());
    }


    /**
     * Spawns a new infiltrator at a random position and has invisible ability creation
     *
     * @param roomTiles The map of valid tiles
     * @param invisible To create a invisible ability
     * @param delay The time to change invisible diagram.( It can be used as cooling down time.)
     */
    public Infiltrator(TiledMapTileLayer roomTiles, String name,Invisible invisible, float delay) {
        super(new Texture(("Infiltrator.png")), roomTiles, 1, 1);
        invisible.show(delay);
        this.name = null;
        this.targetSystem = null;
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
        for (GameSystem system : systemContainer.getAttackableSystems()) {
            float currentDistance = position.dst(system.position);
            if (currentDistance < minDistance && system.getCoolDown() <= 0.0) {
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
     * @return GameSystem    The target system
     */
    public static GameSystem getClosestSystem(Vector2 position, SystemContainer systemContainer) {
        float minDistance = Float.MAX_VALUE;
        GameSystem closestSystem = systemContainer.systems[0];
        for (GameSystem system : systemContainer.getAttackableSystems()) {
            float currentDistance = position.dst(system.position);
            if (currentDistance < minDistance && system.getCoolDown() <= 0.0) {
                minDistance = currentDistance;
                closestSystem = system;
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
     */
    public void moveInfiltrator(TiledMapTileLayer room, SystemContainer systems) {
        if (moves.isEmpty() && targetSystem == null) {
            Node node = getMove(room, systems);
            moves = node.exportPath();
        }
        this.velocity.x = 0;
        this.velocity.y = 0;
        if (targetSystem != null) {
            targetSystem.applyDamage(damageDealt);
            System.out.println("Infiltrator: " + this.name + " attacking: " + targetSystem.name + " with health remaining: " + targetSystem.health);
            if (targetSystem.health <= 0) {
                targetSystem = null;
                //TODO look at moving the infiltrator away from just attacked system to avoid detection and make game harder
            }
        } else if (!moves.isEmpty()) {
            Movements move = moves.remove();
            this.velocity.add(getMovement(move)).scl(this.MAX_VELOCITY);
            // We have reached the target system
            if (moves.isEmpty()) {
                targetSystem = getClosestSystem(position, systems);
                System.out.println("At target system: " + targetSystem.name);
            }
        }

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


