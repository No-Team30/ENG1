package com.team30.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.team30.game.GameContainer;
import com.team30.game.Recording.RecordingContainer;

import static com.team30.game.GameContainer.SCREEN_HEIGHT;
import static com.team30.game.GameContainer.SCREEN_WIDTH;

public class MainMenu extends ScreenAdapter {
    private final GameContainer game;
    /**
     * The input handler for the buttons
     */
    private final Stage stage;
    OrthographicCamera camera;

    public MainMenu(final GameContainer game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);
        stage = new Stage();
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        TextButton start = new TextButton("Start (Normal Game)", game.skin);
        start.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Start Game");
                game.setScreen(new GameScreen(game, false));
                dispose();
            }
        });

        TextButton startRecording = new TextButton("Start (Recording)", game.skin);
        startRecording.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Start Game Recording");
                game.setScreen(new GameScreen(game, true));
                dispose();
            }
        });

        TextButton playback = new TextButton("Playback Recording", game.skin);
        playback.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Playback Recording");
                game.setScreen(new GameScreen(game, new RecordingContainer("Test.json")));
                dispose();
            }
        });

        TextButton quit = new TextButton("Quit", game.skin);
        quit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                dispose();
                Gdx.app.exit();
            }
        });

        table.add(start).space(16).row();
        table.add(startRecording).space(16).row();
        table.add(playback).space(16).row();
        table.add(quit).space(16).row();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.batch.draw(game.teamLogoImg, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        game.batch.end();
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
