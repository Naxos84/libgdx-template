package com.example.athena;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.*;

public class AthenaGame extends Game {

    //Kamera zum Anzeigen der Elemente
    private OrthographicCamera camera;
    //Batch zum rendern von Texturen etc.
    private SpriteBatch batch;
    //TextureRegion (Bild) die gerendert werden soll
    private TextureRegion textureRegion;
    int posx;
    int posy;
    int width;
    int height;
    int rotation;

    @Override
    public void create() {
        //Diese Methode wird !!!ein einziges!!! mal zu Beginn des Spiels aufgerufen

        //Erstellen der Kamera
        camera = new OrthographicCamera();
        //Setzen des Anzeigebereichs der Kamera
        camera.setToOrtho(false, 800, 600);
        //Erstellen des Batches
        batch = new SpriteBatch();
        //Erstellen der TextureRegion(Bild) mit "characters.png"
        textureRegion = new TextureRegion(new Texture(Gdx.files.internal("sprites/characters.png")));
        //Alle Werte einmal festlegen, damit das Bild gemalt werden kann
        initializeValues();
    }

    //Kann aufgerufen werden um fest definierte Werte zu nutzen
    private void initializeValues() {
        posx = 0;
        posy = 0;
        width = textureRegion.getRegionWidth();
        height = textureRegion.getRegionHeight();
        rotation = 0;
    }

    @Override
    public void render() {
        //Diese Methode wird immer wieder aufgerufen, solange das Spiel läuft
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            //X-Positions Wert um 1 verringern
            posx--;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            //X-Positions Wert um 1 erhöhen
            posx++;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            //Y-Positions Wert um 1 verringern
            posy--;
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            //Y-Positions Wert um 1 erhöhen
            posy++;
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



        //Bevor ein Bild gerendert werden kann, muss der Batch gestartet werden
        batch.begin();
        //Rendern des Bildes an der Stelle (posx,posy) mit der Höhe (height) der Breite (width) und der Drehung (rotation)
        batch.draw(textureRegion, posx, posy,0, 0, width, height, 1, 1, rotation);
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
