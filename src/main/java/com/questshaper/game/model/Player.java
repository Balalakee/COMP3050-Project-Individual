package com.questshaper.game.model;

import com.questshaper.game.model.tiles.TileType;
import java.util.ArrayList;
import java.util.List;

public class Player {

    private String username;
    private int y;
    private int x;
    private char avatar;

    private List<TileType> inventory = new ArrayList<>();

    public Player(String username, int y, int x, char avatar) {
        this.username = username;
        this.y = y;
        this.x = x;
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public char getAvatar() {
        return avatar;
    }

    public void setPosition(int y, int x) {
        this.y = y;
        this.x = x;
    }

    public List<TileType> getInventory() {
        return inventory;
    }
}