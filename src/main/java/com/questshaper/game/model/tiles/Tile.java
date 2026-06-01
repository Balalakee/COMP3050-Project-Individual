package com.questshaper.game.model.tiles;

public class Tile {

    private final TileType type;

    public Tile(TileType type) {
        this.type = type;
    }

    public TileType getType() {
        return type;
    }

    public Layer getLayer() {
        return type.layer;
    }

    public boolean isBlocking() {
        return type.blocking;
    }
}