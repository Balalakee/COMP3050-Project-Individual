package com.questshaper.game.model.tiles;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TileStack {

    private final List<Tile> layers =
            new ArrayList<>();

    public List<Tile> getLayers() {
        return layers;
    }

    public void add(Tile tile) {

        removeLayer(tile.getLayer());

        layers.add(tile);

        layers.sort(
                Comparator.comparingInt(
                        t -> t.getLayer().ordinal()
                )
        );
    }

    public void removeLayer(
            Layer layer) {

        layers.removeIf(
                t -> t.getLayer() == layer
        );
    }

    public Tile getLayer(
            Layer layer) {

        for (Tile t : layers) {

            if (t.getLayer() == layer) {
                return t;
            }
        }

        return null;
    }

    public boolean isBlocking() {

    Tile structure = getLayer(Layer.STRUCTURE);

    if (structure != null) {
        return structure.isBlocking();
    }

    Tile floor = getLayer(Layer.FLOOR);

    if (floor != null) {
        return floor.isBlocking();
    }

    return false;
}

    public boolean hasType(
            TileType type) {

        return layers.stream()
                .anyMatch(
                        t -> t.getType() == type
                );
    }

    public void removeType(
            TileType type) {

        layers.removeIf(
                t -> t.getType() == type
        );
    }
}