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
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class AthenaGame extends Game {

    private static final int GRID_WIDTH = 16;
    private static final int GRID_HEIGHT = 16;

    // Camera for scene
    private OrthographicCamera camera;

    // Batch for rendering the sprites
    private SpriteBatch batch;

    // Texture region for rendering
    private TextureRegion textureRegion;

    // Tiled map for store the map data in memory
    TiledMap tiledMap;

    // Renderer which renders a map to screen
    TiledMapRenderer tiledMapRenderer;

    // Variables for position, sizes and speed
    int x;
    int y;
    int width;
    int height;

    //seconds per step
    private static final float WALKSPEED = 0.15f;
    float walkTimer = 0;
    boolean canWalk = false;

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

        // Load start map and init render
        tiledMap = new TmxMapLoader().load("maps/start.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        // Set initial values
        initializeValues();
    }

    // Init values
    private void initializeValues() {

        x = 0;
        y = 0;

        width = textureRegion.getRegionWidth();
        height = textureRegion.getRegionHeight();
    }

    // Called on each time a new frame is rendered
    @Override
    public void render() {

        walkTimer -= Gdx.graphics.getDeltaTime();
        if (walkTimer < 0) {
            walkTimer = 0.15f;
            canWalk = true;
        }


        // Clear background with background color
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            if (canWalk) {
                x -= 16;
                canWalk = false;
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if (canWalk) {
                x += 16;
                canWalk = false;
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            if (canWalk) {
                y -= 16;
                canWalk = false;
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            if (canWalk) {
                y += 16;
                canWalk = false;
            }
        }

        // Set camera position to player an update camera
        camera.position.set(x, y,  0);
        camera.update();

        // Set projection matrix from camera to batch
        batch.setProjectionMatrix(camera.combined);

        // Render map
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        // Render sprite batch with character
        batch.begin();
        batch.draw(textureRegion, x, y, width, height);
        batch.end();
    }

    @Override
    public void dispose() {
        textureRegion.getTexture().dispose();
        batch.dispose();
    }
}
