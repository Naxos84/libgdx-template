package com.example.athena.controller;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.example.athena.AthenaGame;
import com.example.athena.data.Player;
import com.example.athena.data.Sign;
import com.example.athena.data.Warp;

public class MapController {

    public TiledMap tiledMap;

    private TiledMapRenderer tiledMapRenderer;

    private static final String OVER_LAYER = "Over";
    private static final String COLLISION_LAYER = "Collision";

    private int indexOfOverLayer;
    private Player player;

    public MapController(final Player player) {
        this.player = player;
    }

    public void loadMap(String filename) {

        if (tiledMap != null) {
            tiledMap.dispose();
        }

        tiledMap = new TmxMapLoader().load(filename);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        // Get indices
        indexOfOverLayer = tiledMap.getLayers().getIndex(OVER_LAYER);

        // Set visibility off
        tiledMap.getLayers().get(indexOfOverLayer).setVisible(false);

        TiledMapTileLayer collisionLayer = (TiledMapTileLayer) tiledMap.getLayers().get(COLLISION_LAYER);
        collisionLayer.setVisible(false);
    }

    public boolean isBlocked(int x, int y) {

        // Convert player pixel coordinates to grid coordinates
        x = x / AthenaGame.GRID_WIDTH;
        y = y / AthenaGame.GRID_HEIGHT;

        // Get collision layer and cell
        TiledMapTileLayer collisionLayer = (TiledMapTileLayer) tiledMap.getLayers().get(COLLISION_LAYER);
        TiledMapTileLayer.Cell cell = collisionLayer.getCell(x, y);

        // Return true if cell is blocked
        return cell != null;
    }

    private Array<Warp> getWarps() {
        Array<Warp> warps = new Array<>();
        MapLayer objectLayer = tiledMap.getLayers().get("Objects");

        for (MapObject mapObject : objectLayer.getObjects()) {

            if (mapObject.getProperties().containsKey("type")) {

                String type = mapObject.getProperties().get("type", String.class);

                if (type.equals("WARP")) {
                    String map = mapObject.getProperties().get("DEST_MAP", String.class);
                    Float posX = mapObject.getProperties().get("x", Float.class);
                    Float posY = mapObject.getProperties().get("y", Float.class);
                    Float width = mapObject.getProperties().get("width", Float.class);
                    Float height = mapObject.getProperties().get("height", Float.class);
                    Integer dest_x = mapObject.getProperties().get("DEST_X", Integer.class);
                    Integer dest_y = mapObject.getProperties().get("DEST_Y", Integer.class);

                    warps.add(new Warp(posX / AthenaGame.GRID_WIDTH,
                            posY / AthenaGame.GRID_HEIGHT,
                            width / AthenaGame.GRID_WIDTH,
                            height / AthenaGame.GRID_HEIGHT,
                            map,
                            dest_x,
                            dest_y));
                }
            }
        }
        return warps;
    }

    public void checkWarps() {
        Array<Warp> warps = getWarps();


        Rectangle playerRect = new Rectangle(player.x / AthenaGame.GRID_WIDTH, player.y / AthenaGame.GRID_HEIGHT, 1, 1);
//        Gdx.app.debug("playerRect", player.getX() + ":" + player.getY() + " - " + player.getWidth() + ":" + player.getHeight());
        for (Warp warp : warps) {

            if (Intersector.overlaps(playerRect, warp.getWarpZone())) {

                loadMap("maps/" + warp.getMap() + ".tmx");

                Integer mapWidth = tiledMap.getProperties().get("width", Integer.class);
                Integer mapHeight = tiledMap.getProperties().get("height", Integer.class);

                int dest_x = warp.getDestX();
                int dest_y = mapHeight - warp.getDestY() - 1;

                player.x = dest_x * AthenaGame.GRID_WIDTH;
                player.y = dest_y * AthenaGame.GRID_HEIGHT;
            }
        }
    }

    public void renderBackground(final OrthographicCamera camera) {
        tiledMap.getLayers().get(indexOfOverLayer).setVisible(false);

        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
    }

    public void renderOverLayer() {
        // Render over layer
        tiledMap.getLayers().get(indexOfOverLayer).setVisible(true);
        tiledMapRenderer.render(new int[]{indexOfOverLayer});
    }

    public Sign hasDialog(final int x, final int y) {
        MapLayer objectLayer = tiledMap.getLayers().get("Signs");
        if (objectLayer != null) {
            Array<RectangleMapObject> signs = objectLayer.getObjects().getByType(RectangleMapObject.class);
            Rectangle playerRect = new Rectangle(x, y, AthenaGame.GRID_WIDTH, AthenaGame.GRID_HEIGHT);
            for (RectangleMapObject sign : signs) {
                Rectangle signRect = sign.getRectangle();
                if (Intersector.overlaps(signRect, playerRect)) {
                    return new Sign(signRect, sign.getProperties().get("text", String.class));
                }
            }
        }
        return null;
    }

//    public String[] getDialogText(final int x, final int y) {
//        MapLayer objectLayer = tiledMap.getLayers().get("Signs");
//        Array<RectangleMapObject> signs = objectLayer.getObjects().getByType(RectangleMapObject.class);
//        Rectangle playerRect = new Rectangle(x, y, AthenaGame.GRID_WIDTH, AthenaGame.GRID_HEIGHT);
//
//        for (RectangleMapObject sign : signs) {
//            if (Intersector.overlaps(sign.getRectangle(), playerRect)) {
//                return sign.getProperties().get("text", String.class).split("#");
//            }
//
//        }
//        //leeres Array zurück geben damit keine NPE
//        return new String[0];
//    }
}
