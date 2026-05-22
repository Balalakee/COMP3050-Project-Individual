package com.questshaper.game.util;
import com.questshaper.game.model.tiles.*;

public class TileDecoder {

    public static TileStack decode(String code) {

        TileStack stack = new TileStack();

        if (code == null || code.isEmpty()) return stack;

        // -------------------------
        // BASE TILE (first char)
        // -------------------------
        char base = code.charAt(0);

        switch (base) {

            case 'B' -> stack.add(new Tile(TileType.BRICK_WALL, TileLayer.FLOOR));
            case '_' -> stack.add(new Tile(TileType.DIRT, TileLayer.FLOOR));
            case 'f' -> stack.add(new Tile(TileType.FLAGSTONES, TileLayer.FLOOR));
            case 'g' -> stack.add(new Tile(TileType.GRASS, TileLayer.FLOOR));
            case 'p' -> stack.add(new Tile(TileType.PEBBLES, TileLayer.FLOOR));

            case '.' -> stack.add(new Tile(TileType.ROCKS_ONE, TileLayer.FLOOR));
            case ',' -> stack.add(new Tile(TileType.ROCKS_TWO, TileLayer.FLOOR));
            case ':' -> stack.add(new Tile(TileType.ROCKS_THREE, TileLayer.FLOOR));
            case ';' -> stack.add(new Tile(TileType.ROCKS_SIX, TileLayer.FLOOR));

            case 'S' -> stack.add(new Tile(TileType.STONE_WALL, TileLayer.FLOOR));
            case 's' -> stack.add(new Tile(TileType.SAND, TileLayer.FLOOR));
            case 't' -> stack.add(new Tile(TileType.TREE, TileLayer.FLOOR));
            case 'W' -> stack.add(new Tile(TileType.WATER_WAVES, TileLayer.FLOOR));
            case 'w' -> stack.add(new Tile(TileType.WOODEN_BOARDS, TileLayer.FLOOR));

            default -> throw new IllegalArgumentException("Unknown base tile: " + base);
        }

        // -------------------------
        // OVERLAY (optional second char)
        // -------------------------
        if (code.length() > 1) {

            char overlay = code.charAt(1);

            switch (overlay) {

                case 'D' ->
                    stack.add(new Tile(TileType.DOOR, TileLayer.OBJECT));

                case 'd' ->
                    stack.add(new Tile(TileType.DOOR_OPEN, TileLayer.OBJECT));

                case 'b' ->
                    stack.add(new Tile(TileType.BRIDGE, TileLayer.OBJECT));

                default -> {
                    // ignore unknown overlays (safe fallback)
                }
            }
        }

        return stack;
    }
}
