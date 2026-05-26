package com.questshaper.game.model.tiles;

public enum TileType {

    // ground
    BRICK_WALL('B', true, false, null),
    DIRT('_', false, false, null),
    FLAGSTONES('f', false, false, null),
    GRASS('g', false, false, null),
    PEBBLES('p', false, false, null),

    ROCKS_ONE('.', false, false, null),
    ROCKS_TWO(',', false, false, null),
    ROCKS_THREE(':', false, false, null),
    ROCKS_SIX(';', false, false, null),

    STONE_WALL('S', true, false, null),
    SAND('s', false, false, null),
    TREE('t', false, false, null),
    WATER_WAVES('W', true, false, null),
    WOODEN_BOARDS('w', false, false, null),

    // overlays
    DOOR('D', true, false, null),
    DOOR_OPEN('d', false, false, null),
    BRIDGE('b', false, false, null),

    // Items
    AXE('a', false, true, "tool"),
    CYAN_POTION('c', false, true, "drink"),
    HEART_POTION('h', false, true, "drink"),
    KEY('k', false, true, "artifact"),

    PLAYER_0('0', true, false, null), // '0' to '9' for players 1 to 10
    PLAYER_1('1', true, false, null),
    PLAYER_2('2', true, false, null),
    PLAYER_3('3', true, false, null),
    PLAYER_4('4', true, false, null),
    PLAYER_5('5', true, false, null),
    PLAYER_6('6', true, false, null),
    PLAYER_7('7', true, false, null),
    PLAYER_8('8', true, false, null),
    PLAYER_9('9', true, false, null),

    UNKNOWN(' ', true, false, null);

    public final char code;
    public final boolean blocking;
    public final boolean moveable;
    public final String itemClass;

    TileType(char code, boolean blocking, boolean moveable, String itemClass) {
        this.code = code;
        this.blocking = blocking;
        this.moveable = moveable;
        this.itemClass = itemClass;
    }

    public static TileType fromChar(char c) {
        for (TileType t : values()) {
            if (t.code == c) return t;
        }
        return UNKNOWN;
    }
}