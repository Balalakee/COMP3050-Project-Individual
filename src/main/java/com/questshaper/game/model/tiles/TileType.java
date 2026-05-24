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
    TREE('t', false),
    WATER_WAVES('W', true),
    WOODEN_BOARDS('w', false),

    // overlays
    DOOR('D', true),
    DOOR_OPEN('d', false),
    BRIDGE('b', false),

    // Items
    AXE('a', false),
    CYAN_POTION('c', false),
    HEART_POTION('h', false),
    KEY('k', false),

    PLAYER_0('0', true), // '0' to '9' for players 1 to 10
    PLAYER_1('1', true),
    PLAYER_2('2', true),
    PLAYER_3('3', true),
    PLAYER_4('4', true),
    PLAYER_5('5', true),
    PLAYER_6('6', true),
    PLAYER_7('7', true),
    PLAYER_8('8', true),
    PLAYER_9('9', true),

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