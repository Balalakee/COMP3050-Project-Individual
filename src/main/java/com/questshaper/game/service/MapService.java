package com.questshaper.game.service;

import com.questshaper.game.model.GameMap;
import com.questshaper.game.model.tiles.TileStack;
import com.questshaper.game.util.MapLoader;
import com.questshaper.game.util.TileEncoder;

import org.springframework.stereotype.Service;

@Service
public class MapService {

    private final GameMap map;

    public MapService() {
        String[][] mapData = MapLoader.loadMap("map.txt");
        GameMap updateMap = new GameMap(mapData.length, mapData[0].length);
        updateMap.createMap(mapData);
        this.map = updateMap;
    }

    public String[][] getArrayFromMapData() {
        int height = getHeight();
        int width = getWidth();
        String[][] encoded = new String[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                encoded[y][x] = TileEncoder.encode(getStack(y, x));
            }
        }

        return encoded;
    }

    public String[][] getWindow(int centerY, int centerX, int size) {

    String[][] window = new String[size][size];

    int half = size / 2;

    for (int dy = 0; dy < size; dy++) {

        for (int dx = 0; dx < size; dx++) {

            int y = centerY - half + dy;
            int x = centerX - half + dx;

            // X Wraps
            if (x < 0) {
                x += getWidth();
            }

            if (x >= getWidth()) {
                x -= getWidth();
            }

            // Y Does not wrap
            if (y < 0 || y >= getHeight()) {
                window[dy][dx] = " ";
                continue;
            }

            window[dy][dx] = TileEncoder.encode(getStack(y, x));
        }
    }

    return window;
}

    public int getHeight() {
        return map.getHeight();
    }

    public int getWidth() {
        return map.getWidth();
    }

    public boolean isBlocked(int y, int x) {
        TileStack stack = getStack(y, x);
        return stack != null && stack.isBlockingStack();
    }

   public TileStack getStack(int y, int x) {
        return map.getStack(y, x);
    }
}