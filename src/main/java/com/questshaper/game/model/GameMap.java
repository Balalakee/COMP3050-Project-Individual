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
                stack.add(new Tile(TileType.GRASS, TileLayer.FLOOR)); // default to grass floor
                grid[y][x] = stack;
            }
        }
    }

    public void createMap(String[][] mapData) {
        for (int y = 0; y < mapData.length; y++) {
            for (int x = 0; x < mapData[0].length; x++) {
                String cell = mapData[y][x];
                TileStack stack = TileDecoder.decode(cell);
                if (stack.getLayers() == null || stack.getLayers().isEmpty()) {
                    TileStack fallback = new TileStack();
                    fallback.add(new Tile(TileType.GRASS, TileLayer.FLOOR));
                    grid[y][x] = fallback;
                }
                else {
                    grid[y][x] = stack;
                }
                
            }
        }
    }

    public String sendMapData() {
        return "";
    }

    public int getHeight() {
        return grid.length;
    }

    public int getWidth() {
        return grid[0].length;
    }

    public TileStack getStack(int y, int x) {
        if (y < 0 || y >= getHeight()) return null;
        if (x < 0 || x >= getWidth()) return null;
        if (grid[y][x] == null) {
        TileStack fallback = new TileStack();
        fallback.add(new Tile(TileType.GRASS, TileLayer.FLOOR));
        grid[y][x] = fallback;
    }
        return grid[y][x];
    }

    public void setStack(int y, int x, TileStack stack) {
        if (y < 0 || y >= getHeight()) return;
        if (x < 0 || x >= getWidth()) return;
        grid[y][x] = stack;
    }
}
