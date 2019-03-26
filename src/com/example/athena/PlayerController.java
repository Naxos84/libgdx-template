package com.example.athena;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class PlayerController implements InputProcessor {

    AthenaGame game;
    private float cooldown = 0;

    PlayerController(AthenaGame game) {
        this.game = game;
    }

    private boolean isKeyPressed(int keyCode) {
        return Gdx.input.isKeyPressed(keyCode);
    }

    public void update(final float deltaTime) {
            cooldown -= deltaTime;
        boolean isPlayerMoving = isKeyPressed(Input.Keys.UP)
                || isKeyPressed(Input.Keys.RIGHT)
                || isKeyPressed(Input.Keys.DOWN)
                || isKeyPressed(Input.Keys.LEFT);

            if (cooldown < 0 && isPlayerMoving) {

                cooldown = 0;

                switch(game.player.currentDirection) {
                    case Left:
                        moveLeft();
                        break;
                    case Right:
                        moveRight();
                        break;
                    case Up:
                        moveUp();
                        break;
                    case Down:
                        moveDown();
                        break;
                }

                cooldown += 0.15;
            }
    }

    void moveLeft() {
        if (!game.isBlocked(game.player.x - game.GRID_WIDTH, game.player.y)) {
            game.player.x -= game.GRID_WIDTH;
        }
    }

    void moveRight() {
        if (!game.isBlocked(game.player.x + game.GRID_WIDTH, game.player.y)) {
            game.player.x += game.GRID_WIDTH;
        }
    }

    void moveUp() {
        if (!game.isBlocked(game.player.x, game.player.y + game.GRID_HEIGHT)) {
            game.player.y += game.GRID_HEIGHT;
        }
    }

    void moveDown() {
        if (!game.isBlocked(game.player.x, game.player.y - game.GRID_HEIGHT)) {
            game.player.y -= game.GRID_HEIGHT;
        }
    }


    @Override
    public boolean keyDown(final int keycode) {
        if (keycode == Input.Keys.LEFT) {
            game.player.currentDirection = AthenaGame.Direction.Left;


        } else if (keycode == Input.Keys.RIGHT) {
            game.player.currentDirection = AthenaGame.Direction.Right;

        }else if (keycode == Input.Keys.DOWN) {
            game.player.currentDirection = AthenaGame.Direction.Down;

        }else if (keycode == Input.Keys.UP) {
            game.player.currentDirection = AthenaGame.Direction.Up;

        }
        return false;
    }

    @Override
    public boolean keyUp(final int keycode) {
        if (keycode == Input.Keys.ENTER) {
            if (game.hasDialog(game.player.x, game.player.y)) {
                String[] dialogText = game.getDialogText(game.player.x, game.player.y);
                game.showDialog(dialogText);
            }
        }
        return false;
    }

    @Override
    public boolean keyTyped(final char c) {
        return false;
    }

    @Override
    public boolean touchDown(final int i, final int i1, final int i2, final int i3) {
        return false;
    }

    @Override
    public boolean touchUp(final int i, final int i1, final int i2, final int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(final int i, final int i1, final int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(final int i, final int i1) {
        return false;
    }

    @Override
    public boolean scrolled(final int i) {
        return false;
    }
}
