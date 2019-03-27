package com.example.athena.data;

import com.badlogic.gdx.math.Rectangle;

public class Warp {

    private Rectangle warpZone;

    private String map;

    private Integer destX;
    private Integer destY;

    public Warp(float x, float y, float width, float height, String map, Integer destX, Integer destY) {

        this.warpZone = new Rectangle(x, y, width, height);

        this.map = map;
        this.destX = destX;
        this.destY = destY;
    }

    public Rectangle getWarpZone() {
        return warpZone;
    }

    public String getMap() {
        return map;
    }

    public Integer getDestX() {
        return destX;
    }

    public Integer getDestY() {
        return destY;
    }
}
