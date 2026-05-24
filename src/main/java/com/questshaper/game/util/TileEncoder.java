package com.questshaper.game.util;

import com.questshaper.game.model.tiles.*;

public class TileEncoder {

    public static String encode(TileStack stack) {

        if (stack == null ||
            stack.getLayers().isEmpty()) {
            return "g";
        }

        Tile floor = null;
        Tile structure = null;
        Tile item = null;
        Tile character = null;

        for (Tile t : stack.getLayers()) {

            switch (t.getLayer()) {

                case FLOOR ->
                        floor = t;

                case STRUCTURE ->
                        structure = t;

                case ITEM ->
                        item = t;

                case CHARACTER ->
                        character = t;
            }
        }

        StringBuilder sb =
                new StringBuilder();

        if (floor != null)
            sb.append(floor.getType().code);

        if (structure != null)
            sb.append(structure.getType().code);

        if (item != null)
            sb.append(item.getType().code);

        if (character != null)
            sb.append(character.getType().code);

        return sb.toString();
    }
}