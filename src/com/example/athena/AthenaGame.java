package com.example.athena;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.example.athena.controller.MapController;
import com.example.athena.controller.PlayerController;
import com.example.athena.data.Player;

public class AthenaGame extends Game {

    public static final int GRID_WIDTH = 16;
    public static final int GRID_HEIGHT = 16;
    private static final int VIEWPORT_WIDTH = 400;
    private static final int VIEWPORT_HEIGHT = 300;
    private static final int PLAYER_INITIAL_X = 50;
    private static final int PLAYER_INITIAL_Y = 50;

    // Camera for scene
    private OrthographicCamera camera;

    // Batch for rendering the sprites
    private SpriteBatch batch;

    // Texture region for rendering
    private TextureRegion textureRegion;
    private TextureRegion textureRegionUp;
    private TextureRegion textureRegionDown;
    private TextureRegion textureRegionLeft;
    private TextureRegion textureRegionRight;

    private static final String CHARACTER_ASSET_PATH = "sprites/characters.png";

    //grid-cells per second

    private Player player;
    private PlayerController playerController;
    private DialogStage dialogStage;

    private MapController mapController;


    @Override
    public void create() {

        // Create camera
        camera = new OrthographicCamera();

        // Create view port for camera
        camera.setToOrtho(false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);

        // Create sprite batch
        batch = new SpriteBatch();

        // Create texture region from image characters.png
        textureRegion = new TextureRegion(new Texture(Gdx.files.internal(CHARACTER_ASSET_PATH)), 0, 0, GRID_WIDTH, GRID_HEIGHT);
        textureRegionUp = new TextureRegion(new Texture(Gdx.files.internal(CHARACTER_ASSET_PATH)), 16, 48, GRID_WIDTH, GRID_HEIGHT);
        textureRegionDown = new TextureRegion(new Texture(Gdx.files.internal(CHARACTER_ASSET_PATH)), 16, 0, GRID_WIDTH, GRID_HEIGHT);
        textureRegionLeft = new TextureRegion(new Texture(Gdx.files.internal(CHARACTER_ASSET_PATH)), 16, 16, GRID_WIDTH, GRID_HEIGHT);
        textureRegionRight = new TextureRegion(new Texture(Gdx.files.internal(CHARACTER_ASSET_PATH)), 16, 32, GRID_WIDTH, GRID_HEIGHT);

        initializeValues();
        mapController = new MapController(player);
        // Load start map and init render
        mapController.loadMap("maps/start.tmx");

        // Set initial values
        playerController = new PlayerController(player, mapController,this);

        dialogStage = new DialogStage(new Skin(Gdx.files.internal("skin/star-soldier/star-soldier-ui.json")));

        InputMultiplexer inputs = new InputMultiplexer();
        inputs.addProcessor(dialogStage);
        inputs.addProcessor(playerController);

        Gdx.input.setInputProcessor(inputs);
    }

    // Init values
    private void initializeValues() {

        player = new Player();
        player.x = PLAYER_INITIAL_X * GRID_WIDTH;
        player.y = PLAYER_INITIAL_Y * GRID_HEIGHT;

        player.width = textureRegion.getRegionWidth();
        player.height = textureRegion.getRegionHeight();

    }

    // Called on each time a new frame is rendered
    @Override
    public void render() {

        // Clear background with background color
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        dialogStage.act(Gdx.graphics.getDeltaTime());
        // Check warps
        mapController.checkWarps();

        // Set camera position to player an update camera
        camera.position.set(player.x, player.y, 0);
        camera.update();

        // Set projection matrix from camera to batch
        batch.setProjectionMatrix(camera.combined);

        // Render map
        mapController.tiledMap.getLayers().get(mapController.indexOfOverLayer).setVisible(false);

        mapController.tiledMapRenderer.setView(camera);
        mapController.tiledMapRenderer.render();

        // Render sprite batch with character
        batch.begin();

        switch (player.currentDirection) {
            case UP:
                batch.draw(textureRegionUp, player.x, player.y, player.width, player.height);
                break;
            case DOWN:
                batch.draw(textureRegionDown, player.x, player.y, player.width, player.height);
                break;
            case LEFT:
                batch.draw(textureRegionLeft, player.x, player.y, player.width, player.height);
                break;
            case RIGHT:
                batch.draw(textureRegionRight, player.x, player.y, player.width, player.height);
                break;
        }

        batch.end();

        // Render over layer
        mapController.tiledMap.getLayers().get(mapController.indexOfOverLayer).setVisible(true);
        mapController.tiledMapRenderer.render(new int[]{mapController.indexOfOverLayer});


        playerController.update(Gdx.graphics.getDeltaTime());
        dialogStage.draw();
    }

    //TODO move this to MapController
    public boolean hasDialog(final int x, final int y) {
        MapLayer objectLayer = mapController.tiledMap.getLayers().get("Signs");
        Array<RectangleMapObject> signs = objectLayer.getObjects().getByType(RectangleMapObject.class);
        Rectangle playerRect = new Rectangle(x, y, GRID_WIDTH, GRID_HEIGHT);
        for (RectangleMapObject sign : signs) {
            if (Intersector.overlaps(sign.getRectangle(), playerRect)) {
                return true;
            }
        }
        return false;
    }

    //TODO move this to MapController
    public String[] getDialogText(final int x, final int y) {
        MapLayer objectLayer = mapController.tiledMap.getLayers().get("Signs");
        Array<RectangleMapObject> signs = objectLayer.getObjects().getByType(RectangleMapObject.class);
        Rectangle playerRect = new Rectangle(x, y, GRID_WIDTH, GRID_HEIGHT);

        for (RectangleMapObject sign : signs) {
            if (Intersector.overlaps(sign.getRectangle(), playerRect)) {
                return sign.getProperties().get("text", String.class).split("#");
            }

        }
        //leeres Array zurück geben damit keine NPE
        return new String[0];
    }

    public void showDialog(String[] dialogTexts) {
        dialogStage.setTexts(dialogTexts);
        dialogStage.show();
    }

    @Override
    public void dispose() {
        textureRegion.getTexture().dispose();
        textureRegionUp.getTexture().dispose();
        textureRegionDown.getTexture().dispose();
        textureRegionLeft.getTexture().dispose();
        textureRegionRight.getTexture().dispose();
        batch.dispose();
    }

    @Override
    public void resize(final int width, final int height) {
        super.resize(width, height);
        dialogStage.resize(width, height);
    }
}
