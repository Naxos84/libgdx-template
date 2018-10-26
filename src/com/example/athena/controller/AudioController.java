package com.example.athena.controller;

import com.example.athena.manager.AssetManager;
import com.example.athena.manager.EventManager;
import com.example.athena.manager.events.Event;
import com.example.athena.manager.events.EventListener;
import com.example.athena.manager.events.EventType;
import com.example.athena.utils.Globals;

public class AudioController {

    AssetManager assetManager;

    public AudioController(AssetManager assetManager, EventManager eventManager) {

        this.assetManager = assetManager;

        registerGameEvents(eventManager);
    }

    private void registerGameEvents(EventManager eventManager) {

        eventManager.register(EventType.MAP_LOADED, new EventListener() {
            @Override
            public void executeEvent(final Event e) {
                // Todo get information object from map and load correct music
                assetManager.getTownMusic().play();
            }
        });

        eventManager.register(EventType.PLAYER_MOVED, new EventListener() {
            @Override
            public void executeEvent(final Event e) {
                playFootsteps();
            }
        });
    }

    private void playFootsteps() {
        int randomNumber = Globals.random.nextInt(10);

        switch (randomNumber) {
            case 0: {
                assetManager.getFootstepSfx0().play(50);
                break;
            }
            case 1: {
                assetManager.getFootstepSfx1().play(50);
                break;
            }
            case 2: {
                assetManager.getFootstepSfx2().play(50);
                break;
            }
            case 3: {
                assetManager.getFootstepSfx3().play(50);
                break;
            }
            case 4: {
                assetManager.getFootstepSfx4().play(50);
                break;
            }
            case 5: {
                assetManager.getFootstepSfx5().play(50);
                break;
            }
            case 6: {
                assetManager.getFootstepSfx6().play(50);
                break;
            }
            case 7: {
                assetManager.getFootstepSfx7().play(50);
                break;
            }
            case 8: {
                assetManager.getFootstepSfx8().play(50);
                break;
            }
            case 9: {
                assetManager.getFootstepSfx9().play(50);
                break;
            }
        }
    }
}
