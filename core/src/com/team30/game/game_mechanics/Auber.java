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
	 * Range which the Auber must be in to be healed by the health station
	 */
	private float healthRange = 0.5f;

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
	 * @param systemContainer The container with positions of all active systems
	 *                        //TODO Find out how to properly document nullable
	 * @return ID    The ID of the target system (Could be null if no systems are found!)
	 */
	public static ID getClosestSystem(Vector2 position, SystemContainer systemContainer) {
		float minDistance = Float.MAX_VALUE;
		Integer closestSystem = null;
		for (Integer id : systemContainer.getActiveSystems()) {
			float currentDistance = position.dst(systemContainer.getEntityPosition(systemContainer.integerIdLookup(id)));
			if (currentDistance < minDistance) {
				minDistance = currentDistance;
				closestSystem = id;
			}
		}
		return systemContainer.integerIdLookup(closestSystem);
	}

	public void healFromSystem(SystemContainer systems, int healRate) {
		ID closest = getClosestSystem(position, systems);
		if (health < maxHealth && position.dst(systems.getEntityPosition(closest)) <= healthRange) {
			health += healRate;
		}
	}
}
