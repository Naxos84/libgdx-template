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

    //Kamera zum Anzeigen der Elemente
    private OrthographicCamera camera;
    //Batch zum rendern von Texturen etc.
    private SpriteBatch batch;
    //TextureRegion (Bild) die gerendert werden soll
    private TextureRegion textureRegion;

    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;

    int posX;
    int posY;
    int width;
    int height;
    int rotation;

    float speed = 10;

    @Override
    public void create() {
        //Diese Methode wird !!!ein einziges!!! mal zu Beginn des Spiels aufgerufen

        //Erstellen der Kamera
        camera = new OrthographicCamera();

        //Setzen des Anzeigebereichs der Kamera
        camera.setToOrtho(false, 320, 240);

        //Erstellen des Batches
        batch = new SpriteBatch();

        // Projektionsmatrix wird gesetzt
        batch.setProjectionMatrix(camera.combined);

        //Erstellen der TextureRegion(Bild) mit "characters.png"
        textureRegion = new TextureRegion(new Texture(Gdx.files.internal("sprites/characters.png")), 0, 0, 16, 16);

        // Load map
        tiledMap = new TmxMapLoader().load("maps/start.tmx");
//        tiledMap = new TmxMapLoader().load("maps/house.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        //Alle Werte einmal festlegen, damit das Bild gemalt werden kann
        initializeValues();
    }

    //Kann aufgerufen werden um fest definierte Werte zu nutzen
    private void initializeValues() {
        posX = 0;
        posY = 0;
        width = textureRegion.getRegionWidth();
        height = textureRegion.getRegionHeight();
        rotation = 0;
    }

    @Override
    public void render() {
        //Diese Methode wird immer wieder aufgerufen, solange das Spiel läuft
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float delta = Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            //X-Positions Wert um 1 verringern
//            posX--;
            posX-=2;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            //X-Positions Wert um 1 erhöhen
//            posX++;
            posX+=2;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            //Y-Positions Wert um 1 verringern
//            posY--;
            posY-=2;
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            //Y-Positions Wert um 1 erhöhen
//            posY++;
            posY+=2;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.PLUS)) {
            //Breite und Höhe um 1 erhöhen
            width++;
            height++;
        } else if (Gdx.input.isKeyPressed(Input.Keys.MINUS)) {
            //Breite und Höhe um 1 verringern
            width--;
            height--;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.PAGE_UP)) {
            //Drehung um 1 erhöhen (nach rechts drehen um 1°)
            rotation++;
        } else if (Gdx.input.isKeyPressed(Input.Keys.PAGE_DOWN)) {
            //Dehung um 1 verringern (nach links drehen um 1°)
            rotation--;
        }

        //#####
        //BONUS
        //#####
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            //Zurücksetzen aller Werte auf StandardWerte
            initializeValues();
        }

        // cam

        camera.position.set(posX, posY,  0);
        camera.update();

        batch.setProjectionMatrix(camera.combined);


        // Render map
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        //Bevor ein Bild gerendert werden kann, muss der Batch gestartet werden
        batch.begin();
        //Rendern des Bildes an der Stelle (posX,posY) mit der Höhe (height) der Breite (width) und der Drehung (rotation)
        batch.draw(textureRegion, posX, posY,0, 0, width, height, 1, 1, rotation);
        //Nachdem alles gerendet wurde, muss der Batch beendet werden.
        batch.end();
    }

    @Override
    public void dispose() {
        //Wenn das Programm beendet wird, wird diese Methode aufgerufen
        //Hier müssen einige Sachen wieder freigegeben werden, damit die nach Beenden des Spiels nicht mehr im Speicher bleiben!

        //Speicher von der Textur wieder freigeben!
        textureRegion.getTexture().dispose();
        //Speicher vom Batch wieder freigeben!
        batch.dispose();
    }

}
