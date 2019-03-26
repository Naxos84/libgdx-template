package com.example.athena;

public class Player {


    AthenaGame.Direction currentDirection;

    int x;
    int y;
    int width;
    int height;

    public Player() {
        currentDirection = AthenaGame.Direction.Down;
    }
}
