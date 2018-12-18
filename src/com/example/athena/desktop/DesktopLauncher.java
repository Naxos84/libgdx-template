package com.example.athena.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.example.athena.AthenaGame;

public class DesktopLauncher {
    public static void main(final String[] arg) {

        // Create configuration
        final LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        // Set config width
        config.width = 800;

        // Set config height
        config.height = 600;

        // Create a application with AthenaGame and the config
        new LwjglApplication(new AthenaGame(), config);
    }
}
