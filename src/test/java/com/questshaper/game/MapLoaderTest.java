package com.questshaper.game;

import com.questshaper.game.util.MapLoader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MapLoaderTest {

    @Test
    void testMapLoads() {
        char[][] map = MapLoader.loadMap("map.txt");

        assertNotNull(map);
        assertTrue(map.length > 0);
        assertTrue(map[0].length > 0);
    }

    @Test
    void testMapIsRectangular() {
        char[][] map = MapLoader.loadMap("map.txt");

        int width = map[0].length;

        for (char[] row : map) {
            assertEquals(width, row.length);
        }
    }
}