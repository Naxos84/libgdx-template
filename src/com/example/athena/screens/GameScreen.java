package com.example.athena.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.example.athena.AthenaGame;
import com.example.athena.DialogStage;
import com.example.athena.controller.AudioController;
import com.example.athena.controller.MapController;
import com.example.athena.controller.PlayerController;
import com.example.athena.manager.AssetManager;
import com.example.athena.manager.EventManager;
import com.example.athena.manager.events.EventType;
import com.example.athena.utils.Globals;

public class GameScreen implements Screen {

    private final AthenaGame game;
    private AssetManager assetManager;
    private OrthographicCamera camera;
    private AudioController audioController;
    private MapController mapController;
    private PlayerController playerController;
    private EventManager eventManager;
    private Stage gameStage;
    private DialogStage dialogStage;
    private InputMultiplexer inputs;

    public GameScreen(final AthenaGame game) {
        this.game = game;

        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        width = 320;
        height = 240;

        dialogStage = new DialogStage(new Skin(Gdx.files.internal("skin/star-soldier/star-soldier-ui.json")));

        inputs = new InputMultiplexer();
        inputs.addProcessor(dialogStage);

        // Camera setup
        camera = new OrthographicCamera();
        camera.setToOrtho(false, width, height);
        camera.update();

        // Init asset manager
        assetManager = new AssetManager();
        assetManager.loadInitialAssets();

        // Init event dispatcher
        eventManager = new EventManager();
        eventManager.register(EventType.DIALOG, dialogStage);

        // Init Audiocontroller
        audioController = new AudioController(assetManager, eventManager);

        // Init map and player
        mapController = new MapController("maps/start.tmx", eventManager);
        playerController = new PlayerController(50, 50, assetManager.getCharactersTexture(), mapController, eventManager);
        inputs.addProcessor(playerController);

        Gdx.input.setInputProcessor(inputs);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(final float delta) {
        // Init opengl
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        dialogStage.act(delta);

        playerController.update(Gdx.graphics.getDeltaTime());
        // Camera
//        Gdx.app.debug("Game", "Camera is at: " + camera.position.x + ":" + camera.position.y);
        camera.position.set(
                playerController.getPlayerPositionX() * Globals.CELL_SIZE,
                playerController.getPlayerPositionY() * Globals.CELL_SIZE,
                0);

        camera.update();

        // Render map (without over layer)
        mapController.render(camera);

        // Render player with texture
        SpriteBatch spriteBatch = new SpriteBatch();

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();

        playerController.render(spriteBatch);

        spriteBatch.end();

        // Render map (only over layer)
        mapController.renderOver(camera);
        dialogStage.draw();
    }

    @Override
    public void resize(final int width, final int height) {
        dialogStage.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        mapController.dispose();
        assetManager.dispose();
    }
}
