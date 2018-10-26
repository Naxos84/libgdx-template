package com.example.athena.manager;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class AssetManager {

    private final com.badlogic.gdx.assets.AssetManager manager = new com.badlogic.gdx.assets.AssetManager();

    // Music
    private final String townMusic = "music/Town.ogg";

    // SFX
    private final String footstepSfx0 = "sfx/footstep00.ogg";
    private final String footstepSfx1 = "sfx/footstep01.ogg";
    private final String footstepSfx2 = "sfx/footstep02.ogg";
    private final String footstepSfx3 = "sfx/footstep03.ogg";
    private final String footstepSfx4 = "sfx/footstep04.ogg";
    private final String footstepSfx5 = "sfx/footstep05.ogg";
    private final String footstepSfx6 = "sfx/footstep06.ogg";
    private final String footstepSfx7 = "sfx/footstep07.ogg";
    private final String footstepSfx8 = "sfx/footstep08.ogg";
    private final String footstepSfx9 = "sfx/footstep09.ogg";

    // Textures
    private final String charactersTextureFilename = "sprites/characters.png";

    public void loadInitialAssets() {

        // Load Music
        manager.load(townMusic, Music.class);

        // Load sfx
        manager.load(footstepSfx0, Sound.class);
        manager.load(footstepSfx1, Sound.class);
        manager.load(footstepSfx2, Sound.class);
        manager.load(footstepSfx3, Sound.class);
        manager.load(footstepSfx4, Sound.class);
        manager.load(footstepSfx5, Sound.class);
        manager.load(footstepSfx6, Sound.class);
        manager.load(footstepSfx7, Sound.class);
        manager.load(footstepSfx8, Sound.class);
        manager.load(footstepSfx9, Sound.class);

        // Load textures
        manager.load(charactersTextureFilename, Texture.class);

        manager.finishLoading();
    }

    public Music getTownMusic() {
        return manager.get(townMusic);
    }

    public Sound getFootstepSfx0() {
        return manager.get(footstepSfx0);
    }

    public Sound getFootstepSfx1() {
        return manager.get(footstepSfx0);
    }

    public Sound getFootstepSfx2() {
        return manager.get(footstepSfx0);
    }

    public Sound getFootstepSfx3() {
        return manager.get(footstepSfx0);
    }

    public Sound getFootstepSfx4() {
        return manager.get(footstepSfx0);
    }

    public Sound getFootstepSfx5() {
        return manager.get(footstepSfx0);
    }

    public Sound getFootstepSfx6() {
        return manager.get(footstepSfx0);
    }

    public Sound getFootstepSfx7() {
        return manager.get(footstepSfx0);
    }

    public Sound getFootstepSfx8() {
        return manager.get(footstepSfx0);
    }

    public Sound getFootstepSfx9() {
        return manager.get(footstepSfx0);
    }

    public Texture getCharactersTexture() {
        return manager.get(charactersTextureFilename);
    }

    public void dispose() {
        manager.dispose();
    }
}
