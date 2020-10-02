package com.example.plain.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.example.plain.PlainGame;

public class DesktopLauncher {
    public static void main(final String[] arg) {

        // Create configuration
        final LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        // Set config width
        config.width = 800;

        // Set config height
        config.height = 600;
        config.forceExit = false;

        // Create a application with AthenaGame and the config
        new LwjglApplication(new PlainGame(), config);
    }
}
