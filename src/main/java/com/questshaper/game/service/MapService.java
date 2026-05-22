package com.questshaper.game.service;
import com.questshaper.game.model.GameMap;
import com.questshaper.game.model.tiles.*;
import org.springframework.stereotype.Service;

@Service
public class MapService {

    private final GameMap map;

    public MapService() {

        // replace map.txt entirely
        this.map = new GameMap(20, 20);

        // optionally initialize here
        map.getStack(5, 5).add(new Tile(TileType.GRASS, TileLayer.FLOOR));
    }

    public TileStack getStack(int y, int x) {
        return map.getStack(y, x);
    }

    public boolean isBlocked(int y, int x) {
        TileStack stack = getStack(y, x);
        return stack != null && stack.isBlocked();
    }

    public int getHeight() {
        return map.getHeight();
    }

    public int getWidth() {
        return map.getWidth();
    }
}