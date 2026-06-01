package com.questshaper.game.model;

import com.questshaper.game.model.tiles.TileType;

import java.util.LinkedHashMap;
import java.util.Map;

public class Player {

    private final String username;
    private final char avatar;

    private int y;
    private int x;

    // item class -> item type
    private final Map<String, TileType> inventory = new LinkedHashMap<>();

    public Player(String username, int y, int x, char avatar) {
        this.username = username;
        this.y = y;
        this.x = x;
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public char getAvatar() {
        return avatar;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public void setPosition(int y, int x) {
        this.y = y;
        this.x = x;
    }

    public Map<String, TileType> getInventory() {
        return inventory;
    }

    public TileType takeItem(TileType item) {
        String itemClass = itemClass(item);
        if (itemClass == null) return null;
        return inventory.put(itemClass, item);
    }

    public TileType itemToPlace() {
        TileType last = null;
        for (TileType item : inventory.values()) {
            last = item;
        }
        return last;
    }

    public void removeItem(TileType item) {
        String itemClass = itemClass(item);
        if (itemClass != null && inventory.get(itemClass) == item) {
            inventory.remove(itemClass);
        }
    }

    private String itemClass(TileType type) {
        return switch (type) {
            case AXE -> "tool";
            case CYAN_POTION, HEART_POTION -> "drink";
            case KEY -> "artifact";
            default -> null;
        };
    }
}