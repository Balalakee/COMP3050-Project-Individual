package com.questshaper.game.model;

import com.questshaper.game.model.tiles.*;
import com.questshaper.game.util.TileDecoder;

public class GameMap {

    private final TileStack[][] grid;

    public GameMap(int height, int width) {
        grid = new TileStack[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                TileStack stack = new TileStack();
                stack.add(new Tile(TileType.GRASS, TileLayer.FLOOR));
                grid[y][x] = stack;
            }
        }
    }

    public void createMap(String[][] mapData) {
        for (int y = 0; y < mapData.length; y++) {
            for (int x = 0; x < mapData[0].length; x++) {
                grid[y][x] = TileDecoder.decode(mapData[y][x]);
            }
        }
    }

    public int getHeight() {
        return grid.length;
    }

    public int getWidth() {
        return grid[0].length;
    }

    public TileStack getStack(int y, int x) {
        if (x < 0 || x >= getWidth() || y < 0 || y >= getHeight()) {
            return null;
        }
        return grid[y][x];
    }

    public TileStack getStackSafe(int y, int x) {

        if (y < 0 || y >= getHeight()) {
            TileStack empty = new TileStack();
            empty.add(new Tile(TileType.GRASS, TileLayer.FLOOR));
            return empty;
        }

        if (x < 0 || x >= getWidth()) {
            TileStack empty = new TileStack();
            empty.add(new Tile(TileType.GRASS, TileLayer.FLOOR));
            return empty;
        }

        return grid[y][x];
    }
}