package com.team30.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.team30.game.screen.MainMenu;

public class GameContainer extends Game {
	// TODO Get rid of this
	public static final float SCREEN_WIDTH = 640;
	public static final float SCREEN_HEIGHT = 480;
	public static final String RECORDING_FILE_NAME = "Recording.json";
	public BitmapFont font;
	public Skin skin;
	public SpriteBatch batch;
	public Texture teamLogoImg;

	@Override
	public void create() {
		batch = new SpriteBatch();
		font = new BitmapFont();
		teamLogoImg = new Texture("No.png");
		skin = new Skin(Gdx.files.internal("clean-crispy/skin/clean-crispy-ui.json"));
		this.setScreen(new MainMenu(this));
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		batch.dispose();
		teamLogoImg.dispose();
	}
}
