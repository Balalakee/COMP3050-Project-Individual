package com.questshaper.game.model.tiles;

public enum TileType {

    // ground
    BRICK_WALL('B', true),
    DIRT('_', false),
    FLAGSTONES('f', false),
    GRASS('g', false),
    PEBBLES('p', false),

    ROCKS_ONE('.', false),
    ROCKS_TWO(',', false),
    ROCKS_THREE(':', false),
    ROCKS_SIX(';', false),

    STONE_WALL('S', true),
    SAND('s', false),
    TREE('t', true),
    WATER_WAVES('W', true),
    WOODEN_BOARDS('w', false),

    // overlays
    DOOR('D', true),
    DOOR_OPEN('d', false),
    BRIDGE('b', false),

    UNKNOWN(' ', true);

    public final char code;
    public final boolean blocking;

    TileType(char code, boolean blocking) {
        this.code = code;
        this.blocking = blocking;
    }

    public static TileType fromChar(char c) {
        for (TileType t : values()) {
            if (t.code == c) return t;
        }
        return UNKNOWN;
    }
}
