package com.team30.game.game_mechanics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class Auber extends Movement {
	/**
	 * the value for infiltrator ability hallucinations loop
	 */
	int i=0;
	/**
	 * The copy of time for coolDown special ability
	 */
	public int coolDown;
	/**
	 * The copy of time for Auber hallucination
	 */
	public int hallucinationTime;

    public Auber(TiledMapTileLayer roomTiles) {
        super(new Texture("Auber.png"), roomTiles, 1, 1);
        this.VELOCITY_CHANGE = 2f;
    }
	/**
	 * Get Hallucinations ability
	 *if Auber is nearby and cooldown is over,infiltrator will use the ability
	 *@param auber To get player character position
	 *@param infiltrator To get infiltrator position
	 */
	public void getHallucinations(Infiltrator infiltrator) {
		hallucinationTime=infiltrator.hallucinationTime;
		coolDown=infiltrator.coolDown;

		while(i==0) {

			if (infiltrator.position.x-this.position.x < 2 | infiltrator.position.y-this.position.y< 2 ) {
				this.setXVelocity(0);
				this.setYVelocity(0);
				this.VELOCITY_CHANGE=0;

				while(i==0) {
					if(infiltrator.hallucinationTime == 0 ) {
						this.VELOCITY_CHANGE = 2f;
						break;
					}
					else {
						continue;
					}
				}

				while(i==0) {
					if(infiltrator.coolDown == 0) {
						break;
					}

					else {
						continue;
					}
				}

				hallucinationTime=infiltrator.hallucinationTime;
				coolDown=infiltrator.coolDown;

			}
		}
	}
	/**
	 * @param time The time that has passed
	 */
	public void updateHallucinationTime(float delta) {
		this.hallucinationTime -= delta;
	}
	/**
	 * @param time The time that has passed
	 */
	public void updateCoolDown(float delta) {
		this.coolDown -= delta;
	}
}
