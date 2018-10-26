package com.example.athena.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.example.athena.commands.Command;
import com.example.athena.commands.MoveDownCommand;
import com.example.athena.commands.MoveLeftCommand;
import com.example.athena.commands.MoveRightCommand;
import com.example.athena.commands.MoveUpCommand;
import com.example.athena.manager.EventManager;
import com.example.athena.manager.events.Event;
import com.example.athena.manager.events.EventType;
import com.example.athena.model.Direction;
import com.example.athena.model.Player;
import com.example.athena.utils.Globals;
import com.example.athena.view.PlayerView;

public class PlayerController implements InputProcessor {

    Command moveUpCommand;
    Command moveRightCommand;
    Command moveDownCommand;
    Command moveLeftCommand;

    Player player;

    PlayerView playerView;

    MapController mapController;

    EventManager eventManager;

    private float cooldown = 0;

    public PlayerController(int x, int y, Texture textureRegion, MapController mapController, EventManager eventManager) {

        player = new Player(x, y, Globals.CELL_SIZE, Globals.CELL_SIZE);
        playerView = new PlayerView(textureRegion);

        this.mapController = mapController;
        moveUpCommand = new MoveUpCommand();
        moveRightCommand = new MoveRightCommand();
        moveDownCommand = new MoveDownCommand();
        moveLeftCommand = new MoveLeftCommand();

        this.eventManager = eventManager;
    }

    public int getPlayerPositionX() {
        return player.getX();
    }

    public int getPlayerPositionY() {
        return player.getY();
    }

    private boolean isKeyPressed(int keyCode) {
        return Gdx.input.isKeyPressed(keyCode);
    }

    public void update(final float deltaTime) {
        cooldown -= deltaTime;
        player.isMoving = isKeyPressed(Input.Keys.UP)
                || isKeyPressed(Input.Keys.RIGHT)
                || isKeyPressed(Input.Keys.DOWN)
                || isKeyPressed(Input.Keys.LEFT);

        if (cooldown < 0) {

            cooldown = 0;

            Command command = handleInput();
            if (command != null) {
                command.execute(player);
                eventManager.submit(new Event(player, EventType.PLAYER_MOVED));
            }

            cooldown += 0.15;
        }
        playerView.update(Gdx.graphics.getDeltaTime(), player);
    }

    private Command handleInput() {
        if (isKeyPressed(Input.Keys.UP)) {
            player.viewDirection = Direction.UP;
            if (canPlayerMove()) {
                return moveUpCommand;
            }
        } else if (isKeyPressed(Input.Keys.RIGHT)) {
            player.viewDirection = Direction.RIGHT;
            if (canPlayerMoveRight()) {
                return moveRightCommand;
            }
        } else if (isKeyPressed(Input.Keys.DOWN)) {
            player.viewDirection = Direction.DOWN;
            if (canPlayerMoveDown()) {
                return moveDownCommand;
            }
        } else if (isKeyPressed(Input.Keys.LEFT)) {
            player.viewDirection = Direction.LEFT;
            if (canPlayerMoveLeft()) {
                return moveLeftCommand;
            }
        }
        return null;
    }

    public void render(SpriteBatch spriteBatch) {
        playerView.render(spriteBatch, player);
    }

    private boolean canPlayerMove() {
        return !mapController.isBlocked(player.getX(), player.getY() + 1);
    }

    private boolean canPlayerMoveRight() {
        return !mapController.isBlocked(player.getX() + 1, player.getY());
    }

    private boolean canPlayerMoveDown() {
        return !mapController.isBlocked(player.getX(), player.getY() - 1);
    }

    private boolean canPlayerMoveLeft() {
        return !mapController.isBlocked(player.getX() - 1, player.getY());
    }

    public void look(Direction direction) {

        switch (direction) {
            case UP: {
                break;
            }
            case RIGHT: {
                break;
            }
            case DOWN: {
                break;
            }
            case LEFT: {
                break;
            }
        }
    }

    @Override
    public boolean keyDown(final int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(final int keycode) {
        if (keycode == Input.Keys.ENTER) {
            if (mapController.hasDialog(player.getX(), player.getY())) {
                String[] dialogText = mapController.getDialogText(player.getX(), player.getY());
                eventManager.submit(new Event(dialogText, EventType.DIALOG));
            }
        }
        return false;
    }

    @Override
    public boolean keyTyped(final char character) {
        return false;
    }

    @Override
    public boolean touchDown(final int screenX, final int screenY, final int pointer, final int button) {
        return false;
    }

    @Override
    public boolean touchUp(final int screenX, final int screenY, final int pointer, final int button) {
        return false;
    }

    @Override
    public boolean touchDragged(final int screenX, final int screenY, final int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(final int screenX, final int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(final int amount) {
        return false;
    }
}
