package com.questshaper.game.model.tiles;

import java.util.ArrayList;
import java.util.List;

public class TileStack {

    private final List<Tile> layers = new ArrayList<>();

    public void add(Tile tile) {
        layers.removeIf(t -> t.getLayer() == tile.getLayer());
        layers.add(tile);
    }

    public List<Tile> getLayers() {
        return layers;
    }

    public boolean isBlocked() {
        for (Tile t : layers) {
            if (t.isBlocking()) return true;
        }
        return false;
    }
}

