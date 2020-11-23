package com.team30.game.game_mechanics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

public class Auber extends Entity {
    /**
    * The health of the Auber
     */
	private int health;
	private int maxHealth = 100;

	/**
	 * Range which the Auber must be in to be:
	 * 	healed by the health system
	 * 	damaged by a broken system
	 */
	private float healthRange = 0.5f;
	private float damageRange = 1.0f;

    public Auber(TiledMapTileLayer roomTiles) {
        super(new ID(EntityType.Auber), new Texture("Auber.png"), roomTiles, 1, 1);
        this.VELOCITY_CHANGE = 2f;
        this.MAX_VELOCITY *= 1.5;
        this.health = 100;
    }

	public Auber(TiledMapTileLayer roomTiles, int health) {
		super(new ID(EntityType.Auber), new Texture("Auber.png"), roomTiles, 1, 1);
		this.VELOCITY_CHANGE = 2f;
		this.MAX_VELOCITY *= 1.5;
		this.health = health;
	}

	/**
	 * Returns the closest system that is active
	 *
	 * @param position        The position to start from
	 * @param systems The container with positions of all active systems
	 *                        //TODO Find out how to properly document nullable
	 * @return ID    The ID of the target system (Could be null if no systems are found!)
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
	 *	Heals the auber if they are close a healing system
	 *
	 * @param systems		Systems on the station
	 * @param healRate		Health to be added to auber
	 */
	public void healFromSystem(SystemContainer systems, int healRate) {
		ID closest = getClosestSystem(position, systems);
		System.out.println("Closest system: " + systems.getEntity(closest).function);
		System.out.println("Distance: " + position.dst(systems.getEntityPosition(closest)) + "\n");
		//health < maxHealth &&
		if ( systems.getEntity(closest).function == "Healing" && position.dst(systems.getEntityPosition(closest)) <= healthRange) {
			System.out.println("Healing auber");
			health += healRate;
		}
		if (health <= 0)
		{
			System.out.println("Auber dead");
		}
		if (health < 100) {
			System.out.println("Auber at: " + health);
		}
	}

	/**
	 *	Damages the auber if they are close to a broken system
	 *
	 * @param systems		Systems on the station
	 * @param damageRate	The damage rate to be inflicted
	 */
	public void damageFromSystem(SystemContainer systems, float damageRate) {
		ID closest = getClosestSystem(position, systems);
		if (health > 0 && position.dst(systems.getEntityPosition(closest)) <= damageRange) {
			//TODO Balance this heuristic
			System.out.println("Damaging auber");
			health -= (int) damageRate * (systems.getEntity(closest).maxHealth - systems.getEntity(closest).getHealth());
		}
		if (health < 100) {
			System.out.println("Auber at: " + health);
		}
	}
}
