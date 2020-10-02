package com.example.plain;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class PlainGame extends Game {


    @Override
    public void create() {
    }

    // Called on each time a new frame is rendered
    @Override
    public void render() {

        // Clear background with background color
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void dispose() {
    }

    @Override
    public void resize(final int width, final int height) {
        super.resize(width, height);
    }
}
