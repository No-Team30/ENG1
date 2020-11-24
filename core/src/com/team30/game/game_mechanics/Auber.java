package com.team30.game.game_mechanics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.team30.game.game_mechanics.Infiltrators.Hallucinogenic;
import com.team30.game.game_mechanics.Infiltrators.Infiltrator;

public class Auber extends Entity {
    private final int maxHealth = 100;
    /**
     * Range which the Auber must be in to be:
     * healed by the health system
     * damaged by a broken system
     */
    private final float healthRange = 2.0f;
    private final float damageRange = 2.0f;
    private final float teleportRange = 1.0f;
    /**
     * The current and maximum health of the Auber
     */
    private int health;
    private float teleportCoolDown;

    /**
     * The time for hallucination
     */
    public float hallucinationTime;
    public Auber(TiledMapTileLayer roomTiles) {
        super(new ID(EntityType.Auber), new Texture("Auber.png"), roomTiles, 1, 1);
        this.VELOCITY_CHANGE = 2f;
        this.MAX_VELOCITY *= 1.5;
        this.health = 100;
        this.teleportCoolDown = 0.0f;
    }

    public Auber(TiledMapTileLayer roomTiles, int health) {
        super(new ID(EntityType.Auber), new Texture("Auber.png"), roomTiles, 1, 1);
        this.VELOCITY_CHANGE = 2f;
        this.MAX_VELOCITY *= 1.5;
        this.health = health;
        this.teleportCoolDown = 0.0f;
    }

    /**
     * Returns the closest system that is active
     *
     * @param position The position to start from
     * @param systems  The container with positions of all active systems
     *                 //TODO Find out how to properly document nullable
     * @return ID            The ID of the target system (will default to first system returned by getActiveSystems)
     */
    public static ID getClosestSystem(Vector2 position, SystemContainer systems) {
        float minDistance = Float.MAX_VALUE;
        Integer closestSystem = null;
        for (Integer id : systems.getActiveSystems()) {
            float currentDistance = position.dst(systems.getEntityPosition(systems.integerIdLookup(id)));
            if (currentDistance < minDistance) {
                minDistance = currentDistance;
                closestSystem = id;
            }
        }
        return systems.integerIdLookup(closestSystem);
    }

    /**
     * Heals the auber if they are close a healing system
     *
     * @param systems  SystemContainer of systems on the station
     * @param healRate Health to be added to auber
     */
    public void healFromSystem(SystemContainer systems, int healRate) {
        ID closest = getClosestSystem(position, systems);
        if (health < maxHealth && systems.getEntity(closest).type.equals("Healing") && position.dst(systems.getEntityPosition(closest)) <= healthRange) {
            health += healRate;
        }
        if (health <= 0) {
            System.out.println("Auber dead");
        }
    }

    /**
     * Damages the auber if they are close to a broken system
     * Used to end the game if the auber's health is <= 0
     *
     * @param systems
     * @param damageRate The damage rate to be inflicted
     * @return true if the health is below 0
     */
    public boolean damageFromSystem(SystemContainer systems, float damageRate) {
        ID closest = getClosestSystem(position, systems);
        StationSystem system = systems.getEntity(closest);
        if (health > 0 && !(system.type.equals("Healing")) && position.dst(system.position) <= damageRange) {
            //TODO Balance this heuristic
            health -= (int) damageRate * (systems.getEntity(closest).maxHealth - systems.getEntity(closest).getHealth());
        }
        return health <= 0;
    }

    /**
     * Updates the auber's telepportation cooldown
     *
     * @param deltaTime Time passed since last run
     */
    public void updateTeleportCoolDown(float deltaTime) {
        if (teleportCoolDown > 0.0) {
            this.teleportCoolDown -= deltaTime;
        }
        if (teleportCoolDown < 0.0) {
            this.teleportCoolDown = 0.0f;
        }
    }

    /**
     * Checks if the auber can teleport and if so performs it and adds cooldown
     *
     * @param systems SystemContainer of systems on the station
     */
    public void teleport(SystemContainer systems) {
        ID closest = getClosestSystem(position, systems);
        StationSystem teleporter = systems.getEntity(closest);
        if (teleporter.type.equals("Teleportation") && position.dst(teleporter.position) < teleportRange && teleportCoolDown <= 0.0) {
            this.position = systems.getEntityByInt(teleporter.pair).position.cpy();
            System.out.println("Teleporting");
            this.teleportCoolDown = 5.0f;
        }
    }

    /**
     * Attempts to move to a new cell (from the current velocity), if all corners are inside room tiles,when hallucinationTime is zero.
     *
     * @param deltaTime The time since last update
     * @param room      The room layer for collision detection
     */
    @Override
    public void updatePosition(float deltaTime, TiledMapTileLayer room) {
        hallucinationTime -= deltaTime;
        if (hallucinationTime > 0) {
            setXVelocity(0);
            setYVelocity(0);
            return;
        }
        hallucinationTime = 0;
        super.updatePosition(deltaTime, room);
    }

    /**
     * Get Hallucinations ability
     * if Auber is nearby and cooldown is over, infiltrator will use the ability
     *
     * @param infiltrator To create a infiltrator get coolDownTime
     */
    public void getHallucinations(Infiltrator infiltrator) {
        hallucinationTime = 2;
        infiltrator.coolDown = infiltrator.coolDownTime;
    }

    /**
     * Check hallucinations
     */
    public void checkHallucinations(TiledMapTileLayer room, InfiltratorContainer infiltrators) {

        //get Infiltrators around auber
        for (Entity infiltrator : infiltrators.getAllEntities()) {
            if (infiltrator instanceof Hallucinogenic && ((Hallucinogenic) infiltrator).coolDown <= 0 && infiltrators.collisionCheck(this, (Infiltrator) infiltrator, 1)) {
                getHallucinations((Infiltrator) infiltrator);
            }
        }
    }
}
