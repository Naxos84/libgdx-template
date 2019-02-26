package com.example.athena;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class AthenaGame extends Game {

    private static final int GRID_WIDTH = 16;
    private static final int GRID_HEIGHT = 16;

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
    int x;
    int y;
    int width;
    int height;

    //grid-cells per second
    float walkTimer = 0;
    boolean canWalk = false;

    Direction currentDirection;

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
        //Erstellen des Batches
        // Create sprite batch
        batch = new SpriteBatch();

        // Create texture region from image characters.png
        textureRegion = new TextureRegion(new Texture(Gdx.files.internal("sprites/characters.png")), 0, 0, GRID_WIDTH, GRID_HEIGHT);
        textureRegionUp = new TextureRegion(new Texture(Gdx.files.internal("sprites/characters.png")), 16, 48, GRID_WIDTH, GRID_HEIGHT);
        textureRegionDown = new TextureRegion(new Texture(Gdx.files.internal("sprites/characters.png")), 16, 0, GRID_WIDTH, GRID_HEIGHT);
        textureRegionLeft = new TextureRegion(new Texture(Gdx.files.internal("sprites/characters.png")), 16, 16, GRID_WIDTH, GRID_HEIGHT);
        textureRegionRight = new TextureRegion(new Texture(Gdx.files.internal("sprites/characters.png")), 16, 32, GRID_WIDTH, GRID_HEIGHT);

        // Load start map and init render
        tiledMap = new TmxMapLoader().load("maps/start.tmx");

        indexOfOverLayer = tiledMap.getLayers().getIndex(OVER_LAYER);

        TiledMapTileLayer collisionLayer = (TiledMapTileLayer) tiledMap.getLayers().get(COLLISION_LAYER);
        collisionLayer.setVisible(false);

        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);


        // Set initial values
        initializeValues();
    }

    // Init values
    private void initializeValues() {

        x = 0;
        y = 12 * GRID_HEIGHT;

        width = textureRegion.getRegionWidth();
        height = textureRegion.getRegionHeight();

        currentDirection = Direction.Down;
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

    // Called on each time a new frame is rendered
    @Override
    public void render() {

        walkTimer -= Gdx.graphics.getDeltaTime();
        if (walkTimer < 0) {
            walkTimer = 1 / WALK_SPEED;
            canWalk = true;
        }

        // Clear background with background color
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Walking
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            currentDirection = Direction.Left;

            if (canWalk) {
                if (!isBlocked(x - GRID_WIDTH, y)) {
                    x -= GRID_WIDTH;
                    canWalk = false;
                }
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            currentDirection = Direction.Right;

            if (canWalk) {

                if (!isBlocked(x + GRID_WIDTH, y)) {
                    x += GRID_WIDTH;
                    canWalk = false;
                }
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            currentDirection = Direction.Down;

            if (canWalk) {

                if (!isBlocked(x, y - GRID_HEIGHT)) {
                    y -= GRID_HEIGHT;
                    canWalk = false;
                }
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            currentDirection = Direction.Up;

            if (canWalk) {

                if (!isBlocked(x, y + GRID_HEIGHT)) {
                    y += GRID_HEIGHT;
                    canWalk = false;
                }
            }
        }

        // Set camera position to player an update camera
        camera.position.set(x, y, 0);
        camera.update();

        // Set projection matrix from camera to batch
        batch.setProjectionMatrix(camera.combined);

        // Render map
        tiledMap.getLayers().get(indexOfOverLayer).setVisible(false);

        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        // Render sprite batch with character
        batch.begin();

        switch (currentDirection) {
            case Up: {
                batch.draw(textureRegionUp, x, y, width, height);
                break;
            }
            case Down: {
                batch.draw(textureRegionDown, x, y, width, height);
                break;
            }
            case Left: {
                batch.draw(textureRegionLeft, x, y, width, height);
                break;
            }
            case Right: {
                batch.draw(textureRegionRight, x, y, width, height);
                break;
            }
        }

        batch.end();

        // Render over layer
        tiledMap.getLayers().get(indexOfOverLayer).setVisible(true);
        tiledMapRenderer.render(new int[]{indexOfOverLayer});
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
}
