package com.questshaper.game.model.tiles;

public enum TileType {

    // floors / ground
    BRICK_WALL('B', Layer.FLOOR, true),
    DIRT('_', Layer.FLOOR, false),
    FLAGSTONES('f', Layer.FLOOR, false),
    GRASS('g', Layer.FLOOR, false),
    PEBBLES('p', Layer.FLOOR, false),

    ROCKS_ONE('.', Layer.FLOOR, false),
    ROCKS_TWO(',', Layer.FLOOR, false),
    ROCKS_THREE(':', Layer.FLOOR, false),
    ROCKS_SIX(';', Layer.FLOOR, false),

    STONE_WALL('S', Layer.FLOOR, true),
    SAND('s', Layer.FLOOR, false),
    TREE('t', Layer.FLOOR, false),
    WATER_WAVES('W', Layer.FLOOR, true),
    WOODEN_BOARDS('w', Layer.FLOOR, false),

    // structures
    BRIDGE('b', Layer.STRUCTURE, false),
    CLOSED_DOOR('D', Layer.STRUCTURE, true),
    OPEN_DOOR('d', Layer.STRUCTURE, false),

    // items
    AXE('a', Layer.ITEM, false),
    CYAN_POTION('c', Layer.ITEM, false),
    HEART_POTION('h', Layer.ITEM, false),
    KEY('k', Layer.ITEM, false),

    // players
    PLAYER0('0', Layer.CHARACTER, true),
    PLAYER1('1', Layer.CHARACTER, true),
    PLAYER2('2', Layer.CHARACTER, true),
    PLAYER3('3', Layer.CHARACTER, true),
    PLAYER4('4', Layer.CHARACTER, true),
    PLAYER5('5', Layer.CHARACTER, true),
    PLAYER6('6', Layer.CHARACTER, true),
    PLAYER7('7', Layer.CHARACTER, true),
    PLAYER8('8', Layer.CHARACTER, true),
    PLAYER9('9', Layer.CHARACTER, true);

    public final char code;
    public final Layer layer;
    public final boolean blocking;

    TileType(char code, Layer layer, boolean blocking) {
        this.code = code;
        this.layer = layer;
        this.blocking = blocking;
    }

    public static TileType fromCode(char code) {
        for (TileType type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        return null;
    }
}