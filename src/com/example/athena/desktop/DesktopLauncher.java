package com.example.athena.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.example.athena.AthenaGame;

public class DesktopLauncher {
    public static void main(final String[] arg) {

        //Erstellen einer Konfiguration
        final LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        //Setzen der Breite f�r die Konfiguration
        config.width = 800;
        //Setzen der H�he f�r die Konfiguration
        config.height = 600;

        //Erstellen einer Applikation mit unserem Spiel und der erstellten Konfiguration
        new LwjglApplication(new AthenaGame(), config);
    }
}
