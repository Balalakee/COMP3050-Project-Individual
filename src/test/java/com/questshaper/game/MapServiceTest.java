package com.questshaper.game;

import com.questshaper.game.service.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MapServiceTest {

    private final MapService mapService = new MapService();

    @Test
    void testWalkableTile() {
        // Top-left is always grass in your map
        assertFalse(mapService.isBlocked(0, 0));
    }
}