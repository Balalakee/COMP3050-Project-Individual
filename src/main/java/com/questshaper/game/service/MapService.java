package com.questshaper.game.service;

import com.questshaper.game.model.tiles.*;
import com.questshaper.game.util.TileEncoder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import com.questshaper.game.util.MapLoader;

public class MapService {

    private final TileStack[][] map;
    private final int height;
    private final int width;

    public MapService() {
    String[][] mapData =
            MapLoader.loadDefaultMap();

    this.height = mapData.length;
    this.width = mapData[0].length;
    this.map = new TileStack[height][width];

    for (int y = 0; y < height; y++) {
        if (mapData[y].length != width) {
            throw new RuntimeException("Map rows must all have the same width.");
        }

        for (int x = 0; x < width; x++) {
            this.map[y][x] = decode(mapData[y][x]);
        }
    }
}

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int wrapX(int x) {
        int wrapped = x % width;
        if (wrapped < 0) wrapped += width;
        return wrapped;
    }

    public boolean isYInBounds(int y) {
        return y >= 0 && y < height;
    }

    public TileStack getStack(int y, int x) {
        if (!isYInBounds(y)) return null;
        return map[y][wrapX(x)];
    }

    public String getEncodedTileForView(int y, int x) {
        if (!isYInBounds(y)) return " ";
        return TileEncoder.encode(getStack(y, x));
    }

    public boolean isBlocked(int y, int x) {
        TileStack stack = getStack(y, x);
        return stack == null || stack.isBlocking();
    }

    public String[][] getWindow(int centerY, int centerX, int size) {
        String[][] window = new String[size][size];
        int half = size / 2;

        for (int wy = 0; wy < size; wy++) {
            int mapY = centerY - half + wy;

            for (int wx = 0; wx < size; wx++) {
                int mapX = centerX - half + wx;
                window[wy][wx] = getEncodedTileForView(mapY, mapX);
            }
        }

        return window;
    }

    public Tile getItem(int y, int x) {
        TileStack stack = getStack(y, x);
        if (stack == null) return null;
        return stack.getLayer(Layer.ITEM);
    }

    public void removeItem(int y, int x) {
        TileStack stack = getStack(y, x);
        if (stack != null) {
            stack.removeLayer(Layer.ITEM);
        }
    }

    public boolean placeItem(int y, int x, TileType itemType) {
        TileStack stack = getStack(y, x);
        if (stack == null) return false;

        if (stack.getLayer(Layer.ITEM) != null) {
            return false;
        }

        stack.add(new Tile(itemType));
        return true;
    }

    public boolean toggleDoor(int y, int x) {
        TileStack stack = getStack(y, x);
        if (stack == null) return false;

        Tile structure = stack.getLayer(Layer.STRUCTURE);
        if (structure == null) return false;

        if (structure.getType() == TileType.CLOSED_DOOR) {
            stack.add(new Tile(TileType.OPEN_DOOR));
            return true;
        }

        if (structure.getType() == TileType.OPEN_DOOR) {
            stack.add(new Tile(TileType.CLOSED_DOOR));
            return true;
        }

        return false;
    }

    private TileStack decode(String code) {
        TileStack stack = new TileStack();

        for (char c : code.toCharArray()) {
            TileType type = TileType.fromCode(c);
            if (type != null) {
                stack.add(new Tile(type));
            }
        }

        if (stack.getLayer(Layer.FLOOR) == null) {
            stack.add(new Tile(TileType.GRASS));
        }

        return stack;
    }
}