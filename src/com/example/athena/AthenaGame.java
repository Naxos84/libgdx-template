package com.example.athena;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class AthenaGame extends Game {

    //Kamera zum Anzeigen der Elemente
    private OrthographicCamera camera;
    //Batch zum rendern von Texturen etc.
    private SpriteBatch batch;
    //Textur (Bild) die gerendert werden soll
    private Texture tex;

    @Override
    public void create() {
        //Diese Methode wird !!!ein einziges!!! mal zu Beginn des Spiels aufgerufen

        //Erstellen der Kamera
        camera = new OrthographicCamera();
        //Setzen des Anzeigebereichs der Kamera
        camera.setToOrtho(false, 800, 600);
        //Erstellen des Batches
        batch = new SpriteBatch();
        //Erstellen der Textur(Bild) mit "characters.png"
        tex = new Texture(Gdx.files.internal("sprites/characters.png"));
    }

    @Override
    public void render() {
        //Diese Methode wird immer wieder aufgerufen, solange das Spiel läuft
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Bevor ein Bild gerendert werden kann, muss der Batch gestartet werden
        batch.begin();
        //Rendern des Bildes an der Stelle (0,0) (links unten)
        batch.draw(tex, 0, 0);
        //Nachdem alles gerendet wurde, muss der Batch beendet werden.
        batch.end();
    }

    @Override
    public void dispose() {
        //Wenn das Programm beendet wird, wird diese Methode aufgerufen
        //Hier müssen einige Sachen wieder freigegeben werden, damit die nach Beenden des Spiels nicht mehr im Speicher bleiben!

        //Speicher von der Textur wieder freigeben!
        tex.dispose();
        //Speicher vom Batch wieder freigeben!
        batch.dispose();
    }

}
