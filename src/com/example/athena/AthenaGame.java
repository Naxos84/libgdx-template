package com.example.athena;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.example.athena.screens.MenuScreen;

public class AthenaGame extends Game {


    @Override
    public void create() {
        Gdx.app.setLogLevel(Gdx.app.LOG_DEBUG);
        this.setScreen(new MenuScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {

    }

}
