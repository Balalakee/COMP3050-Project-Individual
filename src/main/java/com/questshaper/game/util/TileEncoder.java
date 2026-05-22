package com.questshaper.game.util;

import com.questshaper.game.model.tiles.*;

public class TileEncoder {

    public static String encode(TileStack stack) {

        if (stack == null || stack.getLayers().isEmpty()) {
        return "g"; // fallback instead of empty/black
    }

        Tile base = null;
        Tile overlay = null;

        for (Tile t : stack.getLayers()) {
            if (t.getLayer() == TileLayer.FLOOR) base = t;
            if (t.getLayer() == TileLayer.STRUCTURE) overlay = t;
        }

        StringBuilder sb = new StringBuilder();

        if (base != null) sb.append(base.getType().code);
        if (overlay != null) sb.append(overlay.getType().code);

        return sb.toString();
    }
}