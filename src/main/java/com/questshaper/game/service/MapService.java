package com.questshaper.game.service;

import com.questshaper.game.model.GameMap;
import com.questshaper.game.model.Player;
import com.questshaper.game.model.tiles.Tile;
import com.questshaper.game.model.tiles.TileLayer;
import com.questshaper.game.model.tiles.TileStack;
import com.questshaper.game.util.MapLoader;
import com.questshaper.game.util.TileEncoder;

import java.util.Collection;

import org.springframework.stereotype.Service;

@Service
public class MapService {

    private final GameMap map;

    public MapService() {
        String[][] data = MapLoader.loadMap("map.txt");
        map = new GameMap(data.length, data[0].length);
        map.createMap(data);
    }

    public int getHeight() {
        return map.getHeight();
    }

    public int getWidth() {
        return map.getWidth();
    }

    public TileStack getStack(int y, int x) {
        return map.getStack(y, x);
    }

 public String[][] getWindow(int centerY, int centerX, int size) {

    String[][] window = new String[size][size];

    int half = size / 2;
    int width = getWidth();
    int height = getHeight();

    for (int wy = 0; wy < size; wy++) {

        int mapY = centerY - half + wy;

        // Y does NOT wrap
        if (mapY < 0 || mapY >= height) {
            for (int wx = 0; wx < size; wx++) {
                window[wy][wx] = " ";
            }
            continue;
        }

        for (int wx = 0; wx < size; wx++) {

            int mapX = centerX - half + wx;

            // X WRAPS
            while (mapX < 0) {
                mapX += width;
            }

            while (mapX >= width) {
                mapX -= width;
            }

            TileStack stack = getStack(mapY, mapX);

            window[wy][wx] =
                    TileEncoder.encode(stack);
        }
    }

    return window;
}

public Tile getItem(int y, int x) {

    TileStack stack = getStack(y, x);

    if (stack == null) return null;

    return stack.getByLayer(TileLayer.ITEM);
}

public void removeItem(int y, int x) {

    TileStack stack = getStack(y, x);

    if (stack != null) {
        stack.removeLayer(TileLayer.ITEM);
    }
}

public void placeItem(int y, int x, Tile item) {

    TileStack stack = getStack(y, x);

    if (stack != null) {
        stack.add(item);
    }
}

    public boolean isBlocked(int y, int x) {
        TileStack stack = map.getStackSafe(y, x);
        return stack.isBlockingStack();
    }
}