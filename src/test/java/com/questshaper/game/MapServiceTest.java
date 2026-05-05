package com.questshaper.game;

import com.questshaper.game.service.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MapServiceTest {

    private final MapService mapService = new MapService();

    @Test
    void testOutOfBoundsBlocked() {
        assertTrue(mapService.isBlocked(-1, 0));
        assertTrue(mapService.isBlocked(0, -1));
        assertTrue(mapService.isBlocked(100, 100));
    }

    @Test
    void testWalkableTile() {
        // Top-left is always grass in your map
        assertFalse(mapService.isBlocked(0, 0));
    }

    @Test
    void testWindowSize() {
        char[][] window = mapService.getWindow(5, 5, 11);

        assertEquals(11, window.length);
        assertEquals(11, window[0].length);
    }
}