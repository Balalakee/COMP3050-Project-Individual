package com.questshaper.game.model;

public class Player {

    private int y;
    private int x;

    public Player(int y, int x) {
        this.y = y;
        this.x = x;
    }

    public int getY() { return y; }
    public int getX() { return x; }

    public void setPosition(int y, int x) {
        this.y = y;
        this.x = x;
    }
}