package com.questshaper.game.service;

import com.questshaper.game.util.MapLoader;

public class MapService {

    private char[][] map;

    public MapService() {
        this.map = MapLoader.loadMap("map.txt");
    }

    public char[][] getMap() {
        return map;
    }

    public boolean isBlocked(int y, int x) {
        if (y < 0 || y >= map.length) return true;
        if (x < 0 || x >= map[0].length) return true;

        char tile = map[y][x];

        return tile == 'W' || tile == 'S' || tile == 'B' || tile == 'D';
    }

    public int getHeight() {
        return map.length;
    }

    public int getWidth() {
        return map[0].length;
    }
}