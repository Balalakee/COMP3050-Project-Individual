package com.questshaper.game.model.tiles;

import java.util.ArrayList;
import java.util.List;

public class TileStack {
    private final List<Tile> layers = new ArrayList<>();

    public void add(Tile tile) {
        layers.removeIf(t -> t.getLayer() == tile.getLayer()); // ensure only one tile per layer
        layers.add(tile);
    }

    public List<Tile> getLayers() {
        return layers;
    }

    public Tile getByLayer(TileLayer layer) {
        for (Tile t : layers) {
            if (t.getLayer() == layer) return t;
        }
        return null;
    }

    public void removeLayer(TileLayer layer) {
    layers.removeIf(t -> t.getLayer() == layer);
}

    public boolean isBlockingStack() {

    Tile structure = null;
    Tile floor = null;

    for (Tile t : layers) {

        if (t.getLayer() ==
            TileLayer.STRUCTURE) {

            structure = t;
        }

        if (t.getLayer() ==
            TileLayer.FLOOR) {

            floor = t;
        }
    }

    // structures override floor

    if (structure != null) {
        return structure.isBlockingTile();
    }

    return floor != null &&
           floor.isBlockingTile();
}
    public TileStack copy() {

    TileStack clone = new TileStack();

    for (Tile tile : layers) {
        clone.add(tile);
    }

    return clone;
}
}
