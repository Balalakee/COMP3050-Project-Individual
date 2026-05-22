package com.questshaper.game.model.tiles;

public class Tile {
    private final TileType type;
    private final TileLayer layer;

    public Tile(TileType type, TileLayer layer) {
        this.type = type;
        this.layer = layer;
    }

    public TileType getType() {
        return type;
    }

    public TileLayer getLayer() {
        return layer;
    }

    public boolean isBlockingTile() {
        return type.blocking;
    }

    public char getCode() {
        return type.code;
    }
}
