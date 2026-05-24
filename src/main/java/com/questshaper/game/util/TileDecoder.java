package com.questshaper.game.util;

import com.questshaper.game.model.tiles.*;

public class TileDecoder {

    public static TileStack decode(String code) {

        TileStack stack = new TileStack();

        if (code == null || code.isEmpty()) {
            return stack;
        }

        // first char = floor

        char base = code.charAt(0);

        switch (base) {

            case 'B' -> stack.add(
                    new Tile(
                            TileType.BRICK_WALL,
                            TileLayer.FLOOR));

            case '_' -> stack.add(
                    new Tile(
                            TileType.DIRT,
                            TileLayer.FLOOR));

            case 'f' -> stack.add(
                    new Tile(
                            TileType.FLAGSTONES,
                            TileLayer.FLOOR));

            case 'g' -> stack.add(
                    new Tile(
                            TileType.GRASS,
                            TileLayer.FLOOR));

            case 'p' -> stack.add(
                    new Tile(
                            TileType.PEBBLES,
                            TileLayer.FLOOR));

            case '.' -> stack.add(
                    new Tile(
                            TileType.ROCKS_ONE,
                            TileLayer.FLOOR));

            case ',' -> stack.add(
                    new Tile(
                            TileType.ROCKS_TWO,
                            TileLayer.FLOOR));

            case ':' -> stack.add(
                    new Tile(
                            TileType.ROCKS_THREE,
                            TileLayer.FLOOR));

            case ';' -> stack.add(
                    new Tile(
                            TileType.ROCKS_SIX,
                            TileLayer.FLOOR));

            case 'S' -> stack.add(
                    new Tile(
                            TileType.STONE_WALL,
                            TileLayer.FLOOR));

            case 's' -> stack.add(
                    new Tile(
                            TileType.SAND,
                            TileLayer.FLOOR));

            case 't' -> stack.add(
                    new Tile(
                            TileType.TREE,
                            TileLayer.FLOOR));

            case 'W' -> stack.add(
                    new Tile(
                            TileType.WATER_WAVES,
                            TileLayer.FLOOR));

            case 'w' -> stack.add(
                    new Tile(
                            TileType.WOODEN_BOARDS,
                            TileLayer.FLOOR));
        }

        // remaining chars = overlays/items

        for (int i = 1; i < code.length(); i++) {

            char c = code.charAt(i);

            switch (c) {

                // structures

                case 'D' -> stack.add(
                        new Tile(
                                TileType.DOOR,
                                TileLayer.STRUCTURE));

                case 'd' -> stack.add(
                        new Tile(
                                TileType.DOOR_OPEN,
                                TileLayer.STRUCTURE));

                case 'b' -> stack.add(
                        new Tile(
                                TileType.BRIDGE,
                                TileLayer.STRUCTURE));

                // items

                case 'a' -> stack.add(
                        new Tile(
                                TileType.AXE,
                                TileLayer.ITEM));

                case 'c' -> stack.add(
                        new Tile(
                                TileType.CYAN_POTION,
                                TileLayer.ITEM));

                case 'h' -> stack.add(
                        new Tile(
                                TileType.HEART_POTION,
                                TileLayer.ITEM));

                case 'k' -> stack.add(
                        new Tile(
                                TileType.KEY,
                                TileLayer.ITEM));
            }
        }

        return stack;
    }
}