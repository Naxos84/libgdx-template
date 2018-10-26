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
import com.example.athena.manager.EventManager;
import com.example.athena.manager.events.Event;
import com.example.athena.manager.events.EventListener;
import com.example.athena.manager.events.EventType;
import com.example.athena.model.Entity;

public class MapController {

    private static final String OVER_LAYER = "Over";
    private static final String COLLISION_LAYER = "Collision";

    private TiledMap tiledMap;
    private TiledMapRenderer tiledMapRenderer;

    private int indexOfOverLayer;
    private TiledMapTileLayer collisionLayer;

    private EventManager eventManager;


    private Integer tileWidth;
    private Integer tileHeight;

    public MapController(String filename, EventManager eventManager) {

        this.eventManager = eventManager;
        registerGameEvents(eventManager);

        loadMap(filename);
    }

    private void registerGameEvents(EventManager eventManager) {

        eventManager.register(EventType.PLAYER_MOVED, new EventListener() {
            @Override
            public void executeEvent(final Event e) {
                checkWarps((Entity) e.getSource());
            }
        });
    }

    private void loadMap(String filename) {

        if (tiledMap != null) {
            tiledMap.dispose();
        }

        tiledMap = new TmxMapLoader().load(filename);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        // Get indices
        indexOfOverLayer = tiledMap.getLayers().getIndex(OVER_LAYER);
        collisionLayer = (TiledMapTileLayer) tiledMap.getLayers().get(COLLISION_LAYER);

        // Set visibility off
        tiledMap.getLayers().get(indexOfOverLayer).setVisible(false);
        collisionLayer.setVisible(false);

        tileWidth = tiledMap.getProperties().get("tilewidth", Integer.class);
        tileHeight = tiledMap.getProperties().get("tileheight", Integer.class);

        // Fire events
        eventManager.submit(new Event(tiledMap, EventType.MAP_LOADED));
    }

    /**
     * Checks the collision layer of the map and checks wether the cell given by the coordinates is occupied.
     *
     * @param x the x-coordinate of the cell to check
     * @param y the y-coordinate of the cell to check
     * @return true if Cell[x, y] is blocked within the collision layer
     */
    public boolean isBlocked(int x, int y) {

        TiledMapTileLayer.Cell cell = collisionLayer.getCell(x, y);

        return cell != null;
    }

    public void render(OrthographicCamera camera) {

        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
    }

    public void renderOver(OrthographicCamera camera) {

        tiledMapRenderer.setView(camera);
        tiledMap.getLayers().get(indexOfOverLayer).setVisible(true);

        tiledMapRenderer.render(new int[]{indexOfOverLayer});
        tiledMap.getLayers().get(indexOfOverLayer).setVisible(false);
    }

    public void dispose() {
        tiledMap.dispose();
    }

    private void checkWarps(final Entity player) {
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

                    warps.add(new Warp(posX / tileWidth,
                            posY / tileHeight,
                            width / tileWidth,
                            height / tileHeight,
                            map,
                            dest_x,
                            dest_y));
                }
            }
        }

        Rectangle playerRect = new Rectangle(player.getX(), player.getY(), 1, 1);
//        Gdx.app.debug("playerRect", player.getX() + ":" + player.getY() + " - " + player.getWidth() + ":" + player.getHeight());
        for (Warp warp : warps) {

            if (Intersector.overlaps(playerRect, warp.warpZone)) {

                loadMap("maps/" + warp.map + ".tmx");

                Integer mapWidth = tiledMap.getProperties().get("width", Integer.class);
                Integer mapHeight = tiledMap.getProperties().get("height", Integer.class);

                int dest_x = warp.destX;
                int dest_y = mapHeight - warp.destY - 1;

                player.setX(dest_x);
                player.setY(dest_y);
            }
        }
    }

    public boolean hasDialog(final int x, final int y) {
        MapLayer objectLayer = tiledMap.getLayers().get("Signs");
        Array<RectangleMapObject> signs = objectLayer.getObjects().getByType(RectangleMapObject.class);
        Rectangle playerRect = new Rectangle(x * tileWidth, y * tileHeight, tileWidth, tileHeight);
        for (RectangleMapObject sign : signs) {
                return Intersector.overlaps(sign.getRectangle(), playerRect);
        }
        return false;
    }

    public String[] getDialogText(final int x, final int y) {
        MapLayer objectLayer = tiledMap.getLayers().get("Signs");
        Array<RectangleMapObject> signs = objectLayer.getObjects().getByType(RectangleMapObject.class);
        Rectangle playerRect = new Rectangle(x * tileWidth, y * tileHeight, tileWidth, tileHeight);

        for (RectangleMapObject sign : signs) {
            if (Intersector.overlaps(sign.getRectangle(), playerRect)) {
                return sign.getProperties().get("text", String.class).split("#");
            }

        }
        return new String[0];
    }

    private class Warp {

        private Rectangle warpZone;

        private String map;

        private Integer destX;
        private Integer destY;

        Warp(float x, float y, float width, float height, String map, Integer destX, Integer destY) {

            this.warpZone = new Rectangle(x, y, width, height);

            this.map = map;
            this.destX = destX;
            this.destY = destY;
        }
    }
}

