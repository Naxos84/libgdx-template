package com.example.athena.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.example.athena.AthenaGame;
import com.example.athena.data.Direction;
import com.example.athena.data.Player;

public class PlayerController implements InputProcessor {

    private AthenaGame game;
    private Player player;
    private MapController mapController;
    private float coolDown = 0;

    public PlayerController(Player player, MapController mapController, AthenaGame game) {
        this.game = game;
        this.player = player;
        this.mapController = mapController;
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

                switch(player.currentDirection) {
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
        if (!mapController.isBlocked(player.x - AthenaGame.GRID_WIDTH, player.y)) {
            player.x -= AthenaGame.GRID_WIDTH;
        }
    }

    private void moveRight() {
        if (!mapController.isBlocked(player.x + AthenaGame.GRID_WIDTH, player.y)) {
            player.x += AthenaGame.GRID_WIDTH;
        }
    }

    private void moveUp() {
        if (!mapController.isBlocked(player.x, player.y + AthenaGame.GRID_HEIGHT)) {
            player.y += AthenaGame.GRID_HEIGHT;
        }
    }

    private void moveDown() {
        if (!mapController.isBlocked(player.x, player.y - AthenaGame.GRID_HEIGHT)) {
            player.y -= AthenaGame.GRID_HEIGHT;
        }
    }


    @Override
    public boolean keyDown(final int keycode) {
        if (keycode == Input.Keys.LEFT) {
            player.currentDirection = Direction.LEFT;


        } else if (keycode == Input.Keys.RIGHT) {
            player.currentDirection = Direction.RIGHT;

        }else if (keycode == Input.Keys.DOWN) {
            player.currentDirection = Direction.DOWN;

        }else if (keycode == Input.Keys.UP) {
            player.currentDirection = Direction.UP;

        }
        return false;
    }

    @Override
    public boolean keyUp(final int keycode) {
        if (keycode == Input.Keys.ENTER && game.hasDialog(player.x, player.y)) {
            String[] dialogText = game.getDialogText(player.x, player.y);
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
