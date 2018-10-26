package com.example.athena.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.example.athena.model.Player;
import com.example.athena.utils.Animator;
import com.example.athena.utils.Globals;

public class PlayerView {

    Animation<TextureRegion> standDown;
    Animation<TextureRegion> standLeft;
    Animation<TextureRegion> standRight;
    Animation<TextureRegion> standUp;

    Animation<TextureRegion> walkDown;
    Animation<TextureRegion> walkLeft;
    Animation<TextureRegion> walkRight;
    Animation<TextureRegion> walkUp;

    TextureRegion currentAnimationFrame;

    float stateTime = 0;

    public PlayerView(final Texture textureRegion) {

        float timePerFrame = 0.100f;

        standDown = Animator.getAnimation(textureRegion, 16, 16, 7, 7, timePerFrame);
        standLeft = Animator.getAnimation(textureRegion, 16, 16, 19, 19, timePerFrame);
        standRight = Animator.getAnimation(textureRegion, 16, 16, 31, 31, timePerFrame);
        standUp = Animator.getAnimation(textureRegion, 16, 16, 43, 43, timePerFrame);

        walkDown = Animator.getAnimation(textureRegion, 16, 16, 6, 8, timePerFrame);
        walkLeft = Animator.getAnimation(textureRegion, 16, 16, 18, 20, timePerFrame);
        walkRight = Animator.getAnimation(textureRegion, 16, 16, 30, 32, timePerFrame);
        walkUp = Animator.getAnimation(textureRegion, 16, 16, 42, 44, timePerFrame);
    }

    public void update(final float deltaTime, final Player player) {
        stateTime += deltaTime;
        switch (player.viewDirection) {
            case UP: {

                if (player.isMoving) {
                    currentAnimationFrame = walkUp.getKeyFrame(stateTime, true);
                } else {
                    currentAnimationFrame = standUp.getKeyFrame(stateTime, true);
                }

                break;
            }
            case RIGHT: {
                if (player.isMoving) {
                    currentAnimationFrame = walkRight.getKeyFrame(stateTime, true);
                } else {
                    currentAnimationFrame = standRight.getKeyFrame(stateTime, true);
                }

                break;
            }
            case DOWN: {
                if (player.isMoving) {
                    currentAnimationFrame = walkDown.getKeyFrame(stateTime, true);
                } else {
                    currentAnimationFrame = standDown.getKeyFrame(stateTime, true);
                }

                break;
            }
            case LEFT: {
                if (player.isMoving) {
                    currentAnimationFrame = walkLeft.getKeyFrame(stateTime, true);
                } else {
                    currentAnimationFrame = standLeft.getKeyFrame(stateTime, true);
                }

                break;
            }
        }
    }

    public void render(SpriteBatch spriteBatch, Player player) {
//        Gdx.app.debug("PlayerView", "Rendering player on grid at: " + player.getX() + ":" + player.getY());
        spriteBatch.draw(currentAnimationFrame, player.getX() * Globals.CELL_SIZE, player.getY() * Globals.CELL_SIZE);
    }
}
