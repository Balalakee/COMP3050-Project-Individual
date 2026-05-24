package com.questshaper.game.model.tiles;

public class Tile {
    private final TileType type;
    private final TileLayer layer;
    private final boolean blocking;
    private final char code;

    public Tile(TileType type, TileLayer layer) { // For Normal Tiles
        this.code = type.code;
        this.type = type;
        this.layer = layer;
        this.blocking = type.blocking;
    }

    public Tile(char code, TileLayer layer, boolean blocking) { // For Characters on Tiles
    this.type = null;
    this.code = code;
    this.layer = layer;
    this.blocking = blocking;
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
