package com.questshaper.game.util;

import com.questshaper.game.model.tiles.*;

public class TileEncoder {

    public static String encode(TileStack stack) {

        if (stack == null ||
            stack.getLayers() == null ||
            stack.getLayers().isEmpty()) {
            return "g";
        }

        StringBuilder sb =
                new StringBuilder();

        // Encode in correct draw order
        encodeLayer(stack, TileLayer.FLOOR, sb);
        encodeLayer(stack, TileLayer.STRUCTURE, sb);
        encodeLayer(stack, TileLayer.ITEM, sb);
        encodeLayer(stack, TileLayer.CHARACTER, sb);

        return sb.toString();
    }

    private static void encodeLayer(
            TileStack stack,
            TileLayer layer,
            StringBuilder sb) {

        for (Tile tile : stack.getLayers()) {

            if (tile.getLayer() == layer) {

                sb.append(
                        tile.getType().code
                );
            }
        }
    }
}