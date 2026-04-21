package com.questshaper.game.service;

import java.util.*;

public class GameService {

    private MapService mapService;

    private int playerX = 5;
    private int playerY = 5;

    public GameService(MapService mapService) {
        this.mapService = mapService;
    }

    public boolean move(int dy, int dx) {
        int newY = playerY + dy;
        int newX = playerX + dx;

        if (mapService.isBlocked(newY, newX)) {
            return false;
        }

        playerY = newY;
        playerX = newX;
        return true;
    }

    public Map<String, Object> getInfo(int y, int x) {
        int size = 11;
        int half = size / 2;

        List<List<Character>> window = new ArrayList<>();

        for (int row = y - half; row <= y + half; row++) {
            List<Character> line = new ArrayList<>();

            for (int col = x - half; col <= x + half; col++) {
                if (row < 0 || row >= mapService.getHeight()
                        || col < 0 || col >= mapService.getWidth()) {
                    line.add(' ');
                } else {
                    line.add(mapService.getMap()[row][col]);
                }
            }

            window.add(line);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("x", playerX);
        result.put("y", playerY);
        result.put("top", y - half);
        result.put("left", x - half);
        result.put("bottom", y + half);
        result.put("right", x + half);
        result.put("info", window);

        return result;
    }
}