package com.questshaper.game.service;

import com.questshaper.game.util.MapLoader;

import java.util.Arrays;

import org.springframework.stereotype.Service;

@Service
public class MapService {

    private final char[][] map;

    public MapService() {
        this.map = MapLoader.loadMap("map.txt");
    }

    public int getHeight() {
        return map.length;
    }

    public int getWidth() {
        return map[0].length;
    }

    private int wrapX(int x) {
        int width = getWidth();

        return ((x % width) + width) % width;
    }

    private int clampY(int y) {

        if (y < 0) {
            return 0;
        }

        if (y >= getHeight()) {
            return getHeight() - 1;
        }

        return y;
    }

    public int normalizeX(int x) {
        return wrapX(x);
    }

    public int normalizeY(int y) {
        return clampY(y);
    }

    public boolean isBlocked(int y, int x) {

        x = wrapX(x);
        y = clampY(y);

        char tile = map[y][x];

        return switch (tile) {
            case 'W', 'S', 'B', 'D' -> true;
            default -> false;
        };
    }

   public char[][] getWindow(int top, int left, int size) {

    char[][] window = new char[size][size];

    int width = getWidth();

    for (int y = 0; y < size; y++) {

        int mapY = top + y;

        for (int x = 0; x < size; x++) {

            int rawX = left + x;
            System.out.println("TOP=" + top + " LEFT=" + left);
            // ONLY wrap when reading actual map
            int wrappedX = ((rawX % width) + width) % width;

            // Vertical bounds are clamped
            if (mapY < 0 || mapY >= getHeight()) {
                window[y][x] = ' ';
                continue;
            }

            window[y][x] = map[mapY][wrappedX];
        }
    }
    for (char[] row : window) {
    System.out.println(Arrays.toString(row));
}
    return window;
}
}