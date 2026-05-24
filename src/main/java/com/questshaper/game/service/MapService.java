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

    // ---------------- FIXED WINDOW ----------------

    public String[][] getWindow(
        int centerY,
        int centerX,
        int size
) {

    String[][] window =
            new String[size][size];

    int half = size / 2;

    int width = getWidth();

    for (int dy = 0;
         dy < size;
         dy++) {

        for (int dx = 0;
             dx < size;
             dx++) {

            int y =
                    centerY - half + dy;

            int x =
                    centerX - half + dx;

            // clamp Y

            if (y < 0 ||
                y >= getHeight()) {

                window[dy][dx] =
                        " ";
                continue;
            }

            // WRAP X

            x =
                ((x % width)
                        + width)
                        % width;

            window[dy][dx] =
                    TileEncoder.encode(
                            getStack(y, x)
                    );
        }
    }

    return window;
}

    public boolean isBlocked(int y, int x) {
        TileStack stack = map.getStackSafe(y, x);
        return stack.isBlockingStack();
    }
}