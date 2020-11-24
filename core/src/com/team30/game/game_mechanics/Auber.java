package com.team30.game.game_mechanics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.team30.game.GameContainer;

import java.util.ArrayList;

public class Auber extends Entity {
    /**
    * The current and maximum health of the Auber
     */
	private int health;
	private final int maxHealth = 100;

	/**
	 * Range which the Auber must be in to be:
	 * 	healed by the health system
	 * 	damaged by a broken system
	 */
	private final float healthRange = 2.0f;
	private final float damageRange = 2.0f;
	private final float teleportRange = 1.0f;
	private float teleportCoolDown;

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
	 * @param position		The position to start from
	 * @param systems 		The container with positions of all active systems
	 *                        //TODO Find out how to properly document nullable
	 * @return ID 			The ID of the target system (will default to first system returned by getActiveSystems)
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

	/**s
	 *	Heals the auber if they are close a healing system
	 *
	 * @param systems		SystemContainer of systems on the station
	 * @param healRate		Health to be added to auber
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
	 *
	 * Damages the auber if they are close to a broken system
	 * Used to end the game if the auber's health is <= 0
	 *
	 * @param systems
	 * @param damageRate The damage rate to be inflicted
	 *
	 * @return	true if the health is below 0
	 */
	public boolean damageFromSystem(SystemContainer systems, float damageRate) {
		ID closest = getClosestSystem(position, systems);
		StationSystem system = systems.getEntity(closest);
		if (health > 0 && !(system.type.equals("Healing")) && position.dst(system.position) <= damageRange) {
			//TODO Balance this heuristic
			health -= (int) damageRate * (systems.getEntity(closest).maxHealth - systems.getEntity(closest).getHealth());
		}
		if (health <= 0) {
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * Updates the auber's telepportation cooldown
	 *
	 * @param deltaTime	Time passed since last run
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
	 * @param systems	SystemContainer of systems on the station
	 */
	public void teleport(final GameContainer game, SystemContainer systems) {
		ID closest = getClosestSystem(position, systems);
		StationSystem teleporter = systems.getEntity(closest);
		Stage stage = new Stage();
		if (teleporter.type.equals("Teleportation") && position.dst(teleporter.position) < 1.0f && teleportCoolDown <= 0.0) {
			this.position = systems.getEntityByInt(teleporter.pair).position;
			System.out.println("Teleporting");
			this.teleportCoolDown = 5.0f;
		}
	}
}
