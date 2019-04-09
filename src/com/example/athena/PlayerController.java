package com.example.athena;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.example.athena.data.Direction;

public class PlayerController implements InputProcessor {

    private AthenaGame game;
    private float coolDown = 0;

    PlayerController(AthenaGame game) {
        this.game = game;
    }

    private boolean isKeyPressed(int keyCode) {
        return Gdx.input.isKeyPressed(keyCode);
    }

    public void update(final float deltaTime) {
            coolDown -= deltaTime;
        boolean isPlayerMoving = isKeyPressed(Input.Keys.UP)
                || isKeyPressed(Input.Keys.RIGHT)
                || isKeyPressed(Input.Keys.DOWN)
                || isKeyPressed(Input.Keys.LEFT);

            if (coolDown < 0 && isPlayerMoving) {

                coolDown = 0;

                switch(game.player.currentDirection) {
                    case LEFT:
                        moveLeft();
                        break;
                    case RIGHT:
                        moveRight();
                        break;
                    case UP:
                        moveUp();
                        break;
                    case DOWN:
                        moveDown();
                        break;
                }

                coolDown += 0.15;
            }
    }

    private void moveLeft() {
        if (game.isNotBlocked(game.player.x - AthenaGame.GRID_WIDTH, game.player.y)) {
            game.player.x -= AthenaGame.GRID_WIDTH;
        }
    }

    private void moveRight() {
        if (game.isNotBlocked(game.player.x + AthenaGame.GRID_WIDTH, game.player.y)) {
            game.player.x += AthenaGame.GRID_WIDTH;
        }
    }

    private void moveUp() {
        if (game.isNotBlocked(game.player.x, game.player.y + AthenaGame.GRID_HEIGHT)) {
            game.player.y += AthenaGame.GRID_HEIGHT;
        }
    }

    private void moveDown() {
        if (game.isNotBlocked(game.player.x, game.player.y - AthenaGame.GRID_HEIGHT)) {
            game.player.y -= AthenaGame.GRID_HEIGHT;
        }
    }


    @Override
    public boolean keyDown(final int keycode) {
        if (keycode == Input.Keys.LEFT) {
            game.player.currentDirection = Direction.LEFT;


        } else if (keycode == Input.Keys.RIGHT) {
            game.player.currentDirection = Direction.RIGHT;

        }else if (keycode == Input.Keys.DOWN) {
            game.player.currentDirection = Direction.DOWN;

        }else if (keycode == Input.Keys.UP) {
            game.player.currentDirection = Direction.UP;

        }
        return false;
    }

    @Override
    public boolean keyUp(final int keycode) {
        if (keycode == Input.Keys.ENTER && game.hasDialog(game.player.x, game.player.y)) {
            String[] dialogText = game.getDialogText(game.player.x, game.player.y);
            game.showDialog(dialogText);
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
