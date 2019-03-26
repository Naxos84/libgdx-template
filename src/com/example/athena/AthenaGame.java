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
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

public class AthenaGame extends Game {

    static final int GRID_WIDTH = 16;
    static final int GRID_HEIGHT = 16;

    private static final float WALK_SPEED = 8;

    private static final String OVER_LAYER = "Over";
    private static final String COLLISION_LAYER = "Collision";

    private int indexOfOverLayer;

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

    // Tiled map for store the map data in memory
    TiledMap tiledMap;

    // Renderer which renders a map to screen
    TiledMapRenderer tiledMapRenderer;

    // Variables for position, sizes and speed


    //grid-cells per second

    Player player;
    PlayerController playerController;
    DialogStage dialogStage;

    enum Direction {
        Up,
        Down,
        Left,
        Right
    }

    @Override
    public void create() {

        // Create camera
        camera = new OrthographicCamera();

        // Create view port for camera
        camera.setToOrtho(false, 400, 300);

        // Create sprite batch
        batch = new SpriteBatch();

        // Create texture region from image characters.png
        textureRegion = new TextureRegion(new Texture(Gdx.files.internal("sprites/characters.png")), 0, 0, GRID_WIDTH, GRID_HEIGHT);
        textureRegionUp = new TextureRegion(new Texture(Gdx.files.internal("sprites/characters.png")), 16, 48, GRID_WIDTH, GRID_HEIGHT);
        textureRegionDown = new TextureRegion(new Texture(Gdx.files.internal("sprites/characters.png")), 16, 0, GRID_WIDTH, GRID_HEIGHT);
        textureRegionLeft = new TextureRegion(new Texture(Gdx.files.internal("sprites/characters.png")), 16, 16, GRID_WIDTH, GRID_HEIGHT);
        textureRegionRight = new TextureRegion(new Texture(Gdx.files.internal("sprites/characters.png")), 16, 32, GRID_WIDTH, GRID_HEIGHT);

        // Load start map and init render
        loadMap("maps/start.tmx");

        // Set initial values
        initializeValues();
        playerController = new PlayerController(this);

        dialogStage = new DialogStage(new Skin(Gdx.files.internal("skin/star-soldier/star-soldier-ui.json")));

        InputMultiplexer inputs = new InputMultiplexer();
        inputs.addProcessor(dialogStage);
        inputs.addProcessor(playerController);

        Gdx.input.setInputProcessor(inputs);
    }

    // Init values
    private void initializeValues() {

        player = new Player();
        player.x = 50 * GRID_WIDTH;
        player.y = 50 * GRID_HEIGHT;

        player.width = textureRegion.getRegionWidth();
        player.height = textureRegion.getRegionHeight();

    }

    public boolean isBlocked(int x, int y) {

        // Convert player pixel coordinates to grid coordinates
        x = x / GRID_WIDTH;
        y = y / GRID_HEIGHT;

        // Get collision layer and cell
        TiledMapTileLayer collisionLayer = (TiledMapTileLayer) tiledMap.getLayers().get(COLLISION_LAYER);
        TiledMapTileLayer.Cell cell = collisionLayer.getCell(x, y);

        // Return true if cell is blocked
        return cell != null;
    }

    private class Warp {

        private Rectangle warpZone;

        private String map;

        private Integer destX;
        private Integer destY;

        Warp(float x, float y, float width, float height, String map, Integer destX, Integer destY) {

            this.warpZone = new Rectangle(x, y, width, height);

            this.map = map;
            this.destX = destX;
            this.destY = destY;
        }
    }

    private void loadMap(String filename) {

        if (tiledMap != null) {
            tiledMap.dispose();
        }

        tiledMap = new TmxMapLoader().load(filename);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        // Get indices
        indexOfOverLayer = tiledMap.getLayers().getIndex(OVER_LAYER);

        // Set visibility off
        tiledMap.getLayers().get(indexOfOverLayer).setVisible(false);

        TiledMapTileLayer collisionLayer = (TiledMapTileLayer) tiledMap.getLayers().get(COLLISION_LAYER);
        collisionLayer.setVisible(false);
    }

    private void checkWarps() {
        Array<Warp> warps = new Array<>();
        MapLayer objectLayer = tiledMap.getLayers().get("Objects");

        for (MapObject mapObject : objectLayer.getObjects()) {

            if (mapObject.getProperties().containsKey("type")) {

                String type = mapObject.getProperties().get("type", String.class);

                if (type.equals("WARP")) {
                    String map = mapObject.getProperties().get("DEST_MAP", String.class);
                    Float posX = mapObject.getProperties().get("x", Float.class);
                    Float posY = mapObject.getProperties().get("y", Float.class);
                    Float width = mapObject.getProperties().get("width", Float.class);
                    Float height = mapObject.getProperties().get("height", Float.class);
                    Integer dest_x = mapObject.getProperties().get("DEST_X", Integer.class);
                    Integer dest_y = mapObject.getProperties().get("DEST_Y", Integer.class);

                    warps.add(new Warp(posX / GRID_WIDTH,
                            posY / GRID_HEIGHT,
                            width / GRID_WIDTH,
                            height / GRID_HEIGHT,
                            map,
                            dest_x,
                            dest_y));
                }
            }
        }

        Rectangle playerRect = new Rectangle(player.x / GRID_WIDTH, player.y / GRID_HEIGHT, 1, 1);
//        Gdx.app.debug("playerRect", player.getX() + ":" + player.getY() + " - " + player.getWidth() + ":" + player.getHeight());
        for (Warp warp : warps) {

            if (Intersector.overlaps(playerRect, warp.warpZone)) {

                loadMap("maps/" + warp.map + ".tmx");

                Integer mapWidth = tiledMap.getProperties().get("width", Integer.class);
                Integer mapHeight = tiledMap.getProperties().get("height", Integer.class);

                int dest_x = warp.destX;
                int dest_y = mapHeight - warp.destY - 1;

                player.x = dest_x * GRID_WIDTH;
                player.y = dest_y * GRID_HEIGHT;
            }
        }
    }

    // Called on each time a new frame is rendered
    @Override
    public void render() {

        // Clear background with background color
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        dialogStage.act(Gdx.graphics.getDeltaTime());
        // Check warps
        checkWarps();

        // Set camera position to player an update camera
        camera.position.set(player.x, player.y, 0);
        camera.update();

        // Set projection matrix from camera to batch
        batch.setProjectionMatrix(camera.combined);

        // Render map
        tiledMap.getLayers().get(indexOfOverLayer).setVisible(false);

        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        // Render sprite batch with character
        batch.begin();

        switch (player.currentDirection) {
            case Up: {
                batch.draw(textureRegionUp, player.x, player.y, player.width, player.height);
                break;
            }
            case Down: {
                batch.draw(textureRegionDown, player.x, player.y, player.width, player.height);
                break;
            }
            case Left: {
                batch.draw(textureRegionLeft, player.x, player.y, player.width, player.height);
                break;
            }
            case Right: {
                batch.draw(textureRegionRight, player.x, player.y, player.width, player.height);
                break;
            }
        }

        batch.end();

        // Render over layer
        tiledMap.getLayers().get(indexOfOverLayer).setVisible(true);
        tiledMapRenderer.render(new int[]{indexOfOverLayer});


        playerController.update(Gdx.graphics.getDeltaTime());
        dialogStage.draw();
    }
    public boolean hasDialog(final int x, final int y) {
        MapLayer objectLayer = tiledMap.getLayers().get("Signs");
        Array<RectangleMapObject> signs = objectLayer.getObjects().getByType(RectangleMapObject.class);
        Rectangle playerRect = new Rectangle(x, y, GRID_WIDTH, GRID_HEIGHT);
        for (RectangleMapObject sign : signs) {
            return Intersector.overlaps(sign.getRectangle(), playerRect);
        }
        return false;
    }

    public String[] getDialogText(final int x, final int y) {
        MapLayer objectLayer = tiledMap.getLayers().get("Signs");
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

    void showDialog(String[] dialogTexts) {
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
