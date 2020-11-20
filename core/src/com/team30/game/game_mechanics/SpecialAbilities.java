package com.team30.game.game_mechanics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.team30.game.game_mechanics.Auber;
import com.team30.game.game_mechanics.Infiltrator;


public class SpecialAbilities {
	/**
	 * Render time for the animation
	 */
	public static float statetime;
	/**
	 * 	Manage animation settings for play mode and play order
	 */
	Animation animation;
	/**
     * The texture of the given invisible picture
     */
	Texture texture;
	/**
     * The texture used to render the invisible picture
     */
	TextureRegion frame;
	/**
     * The texture list used to get picture with current frame
     */
	TextureRegion[]currentFrame;
	
	/**
	 * invisible ability
     * @param delay The time to change diagram.( It can be used as cooling down time.)
     */
	public void invisible(float delay) {
		texture = new Texture(Gdx.files.internal("data/Infiltrator_change.png"));
		
		frame = new TextureRegion( texture );
		currentFrame = frame.split( 64, 64 )[0];
		animation = new Animation(delay, currentFrame[0], currentFrame[1]);
		statetime = 0;
		}
	
	/**
	 * invisible infiltrator draw
     */
	public void draw(SpriteBatch batch){
    	statetime += Gdx.graphics.getDeltaTime();
		frame=animation.getKeyFrame(statetime,true);
		batch.begin();
		batch.draw(frame, 100, 100);
		batch.end();
    }

	/**
	* @param inflicted The amount of damage to be inflicted to the system
	*/
	public void highDamage(int inflicted) {
		//When creating an applydamage(int inflicted) method in GameSystem,fill in a higher value in applydamage.
	}
	
     }
     
	

