package com.questshaper.game.model;

public class GameMap {
    private char[][] grid;

    public GameMap(char[][] grid) {
        this.grid = grid;
    }

    public char getTile(int x, int y) {
        return grid[y][x];
    }

    public boolean isBlocked(int y, int x) {
        char tile = grid[y][x];
        return tile == 'W' || tile == 'S' || tile == 'B' || tile == 'D';
    }
}
