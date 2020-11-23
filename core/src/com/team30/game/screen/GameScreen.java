package com.team30.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.team30.game.GameContainer;
import com.team30.game.Recording.Action;
import com.team30.game.Recording.ActionType;
import com.team30.game.Recording.RecordingContainer;
import com.team30.game.game_mechanics.Auber;
import com.team30.game.game_mechanics.InfiltratorContainer;
import com.team30.game.game_mechanics.NpcContainer;
import com.team30.game.game_mechanics.SystemContainer;

import java.util.LinkedList;

public class GameScreen extends ScreenAdapter implements InputProcessor {
    /**
     * The size of the tiles in pixels
     */
    public static final int TILE_SIZE = 64;
    /**
     * The amount of tiles rendered around the Auber
     */
    private static final int VIEW_DISTANCE = 10;
    private static final float SNAPSHOT_INTERVAL = 0.1f;
    private final Auber auber;

    /**
     * A map layer, representing valid tiles for characters to enter (Room Tiles)
     * Used for collision detection
     */
    private final TiledMapTileLayer roomTiles;
    private final MapLayer systemsMap;

    private final InfiltratorContainer infiltrators;
    private final SystemContainer systemContainer;
    /**
     * Used for selecting the view window for the player
     */
    OrthographicCamera camera;
    OrthogonalTiledMapRenderer tiledMapRenderer;
    TiledMap tiledMap;
    GameContainer game;
    NpcContainer npcs;

    float timeSinceLastSnapshot;
    RecordingContainer recording;
    Boolean shouldRecord;
    Boolean isPlayback;

    /**
     * Builds the map, camera and entities for a game
     */
    private GameScreen() {
        float width = GameContainer.SCREEN_WIDTH;
        float height = GameContainer.SCREEN_HEIGHT;

        // Map setup
        tiledMap = new TmxMapLoader().load("Map.tmx");
        //tiledMap = new TmxMapLoader().load("test_map.tmx");
        MapLayers layers = tiledMap.getLayers();
        roomTiles = (TiledMapTileLayer) layers.get("Rooms");
        systemsMap = layers.get("Systems");

        // Builds the renderer and sets the grid to one "tile"
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, (float) 1 / TILE_SIZE);

        camera = new OrthographicCamera();
        // Sets the view distance, adjusting for aspect ratio
        camera.setToOrtho(false, (width / height) * VIEW_DISTANCE, VIEW_DISTANCE);
        camera.update();

        // Create all entities
        // TODO Think of a better way of assigning
        auber = new Auber(roomTiles);
        npcs = new NpcContainer();

        systemContainer = new SystemContainer(systemsMap);
        infiltrators = new InfiltratorContainer(systemContainer);
        Gdx.input.setInputProcessor(this);
    }

    /**
     * Starts a new game with player controlled Auber
     *
     * @param game         The parent container
     * @param shouldRecord Whether this game should be recorded
     */
    GameScreen(GameContainer game, Boolean shouldRecord) {
        this();
        this.game = game;
        // Recording setup
        this.timeSinceLastSnapshot = 0;
        this.shouldRecord = shouldRecord;
        this.isPlayback = false;
        this.recording = new RecordingContainer();

        //Spawn entities
        npcs.spawnNpcs(roomTiles);
    }

    /**
     * Creates a new playback instance from the given recording
     *
     * @param game      The parent container
     * @param recording The recording to playback from
     */
    GameScreen(GameContainer game, RecordingContainer recording) {
        this();
        this.game = game;

        // Recording setup
        this.timeSinceLastSnapshot = 0;
        this.shouldRecord = false;
        this.isPlayback = true;
        this.recording = recording;
    }

    @Override
    public void render(float delta) {
        // Set black background anc clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        timeSinceLastSnapshot += delta;

        // Tries to playback recording
        if (isPlayback & timeSinceLastSnapshot > SNAPSHOT_INTERVAL) {
            timeSinceLastSnapshot -= SNAPSHOT_INTERVAL;
            LinkedList<Action> actions = recording.getSnapshot();
            for (Action action : actions) {
                switch (action.getEntityType()) {
                    case Auber:
                        auber.applyMovementAction(action);
                        break;
                    case Infiltrator:
                        infiltrators.applyAction(action);
                        break;
                    case Npc:
                        npcs.applyAction(action);
                        break;
                    case StationSystem:
                        systemContainer.applyAction(action);
                        break;
                }
            }
        }

        // Move entities if not in playback mode
        if (!isPlayback) {
            infiltrators.calculatePosition(delta, roomTiles);
            npcs.calculatePosition(delta, roomTiles);
        }
        auber.updatePosition(delta, roomTiles);

        infiltrators.updateMovements(delta, roomTiles);
        npcs.updateMovements(delta, roomTiles);
        systemContainer.updateMovements(delta, roomTiles);

        auber.healFromSystem(systemContainer, 1);
        infiltrators.checkCaptured(auber);
        // Set the camera to focus on Auber
        camera.position.x = auber.getXPosition();
        camera.position.y = auber.getYPosition();

        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        Batch batch = tiledMapRenderer.getBatch();
        batch.begin();
        auber.draw(batch);
        infiltrators.draw(batch);
        npcs.draw(batch);
        if (systemContainer.getAmountOfActiveSystems() < 1) {
            System.out.println("Game ends");
            game.pause();
            game.setScreen(new MainMenu(game));
            //TODO game end condition
        }
        batch.end();

        // Records any movements made
        if (shouldRecord & timeSinceLastSnapshot > SNAPSHOT_INTERVAL) {
            recording.newSnapshot();
            recording.addAction(new Action(auber.id, ActionType.Move, auber.getXPosition(), auber.getYPosition(), auber.getXVelocity(), auber.getYVelocity(), null));
            recording.addAllAction(npcs.record());
            recording.addAllAction(infiltrators.record());
            recording.addAllAction(systemContainer.record());
            timeSinceLastSnapshot = 0;
        }
    }


    /**
     * Key not being pressed, so set velocity to zero
     *
     * @param keycode the keycode of the pressed key
     * @return false
     */
    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            if (shouldRecord) {
                recording.exportRecording();
            }
            game.setScreen(new MainMenu(game));
        }
        // Disable player input for playback
        if (!isPlayback) {
            if (keycode == Input.Keys.LEFT) {
                auber.setXVelocity(0);
            }
            if (keycode == Input.Keys.RIGHT) {
                auber.setXVelocity(0);
            }
            if (keycode == Input.Keys.UP) {
                auber.setYVelocity(0);
            }
            if (keycode == Input.Keys.DOWN) {
                auber.setYVelocity(0);
            }
            if (keycode == Input.Keys.SPACE) {
                recording.exportRecording();
            }
        }
        return false;
    }

    /**
     * Key not being pressed, so set velocity to max
     *
     * @param keycode the keycode of the pressed key
     * @return false a generic return
     */
    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.LEFT) {
            auber.setXVelocity(-auber.VELOCITY_CHANGE);
        }
        if (keycode == Input.Keys.RIGHT) {
            auber.setXVelocity(auber.VELOCITY_CHANGE);
        }
        if (keycode == Input.Keys.UP) {
            auber.setYVelocity(auber.VELOCITY_CHANGE);
        }
        if (keycode == Input.Keys.DOWN) {
            auber.setYVelocity(-auber.VELOCITY_CHANGE);
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {

        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

}