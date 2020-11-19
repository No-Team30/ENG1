package com.team30.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.team30.game.GameContainer;


// TODO We can probably get rid of this
public class Settings extends ScreenAdapter {
    private final GameContainer game;
    private final Stage stage;

    public Settings(final GameContainer game) {
        this.game = game;

        stage = new Stage();

        Label nameLabel = new Label("Max Speed:", game.skin);
        Slider slider = new Slider(0f, 20f, 1, false, game.skin);


        TextButton accept = new TextButton("Accept", game.skin);
        accept.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Save Settings");
                game.setScreen(new MainMenu(game));
                dispose();
            }
        });
        TextButton cancel = new TextButton("Cancel", game.skin);
        cancel.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Discard Settings");
                game.setScreen(new MainMenu(game));
                dispose();
            }
        });

        Table scrollTable = new Table();
        scrollTable.add(nameLabel);
        scrollTable.add(slider);
        scrollTable.row();
        scrollTable.row().height(200);
        scrollTable.add(accept).padTop(200).padLeft(25);
        scrollTable.add(cancel).padTop(200).padRight(25);


        ScrollPane scroller = new ScrollPane(scrollTable);
        Table container = new Table();
        container.setFillParent(true);
        container.add(scroller).fill().expand();
        this.stage.addActor(container);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {

    }
}
