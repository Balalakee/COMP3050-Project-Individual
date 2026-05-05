package com.questshaper.game.service;

import com.questshaper.game.util.MapLoader;
import org.springframework.stereotype.Service;

@Service
public class MapService {

    private final char[][] map;

    public MapService() {
        this.map = MapLoader.loadMap("map.txt");
    }

    public boolean isBlocked(int y, int x) {
        if (y < 0 || y >= map.length || x < 0 || x >= map[0].length) {
            return true;
        }

        char tile = map[y][x];
        return (tile == 'W' || tile == 'S' || tile == 'B' || tile == 'D');
    }

    public char[][] getWindow(int top, int left, int size) {
        char[][] window = new char[size][size];

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                int mapY = top + y;
                int mapX = left + x;

                if (mapY < 0 || mapY >= map.length || mapX < 0 || mapX >= map[0].length) {
                    window[y][x] = ' ';
                } else {
                    window[y][x] = map[mapY][mapX];
                }
            }
        }

        return window;
    }
}