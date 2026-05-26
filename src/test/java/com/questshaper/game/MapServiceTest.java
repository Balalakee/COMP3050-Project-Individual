package com.questshaper.game;

import com.questshaper.game.model.tiles.TileLayer;
import com.questshaper.game.model.tiles.TileType;
import com.questshaper.game.service.MapService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MapServiceTest {

    private final MapService mapService =
            new MapService();

    @Test
    void mapLoadsSuccessfully() {
        assertTrue(mapService.getHeight() > 0);
        assertTrue(mapService.getWidth() > 0);
    }

    @Test
    void mapHasCorrectDimensions() {
        assertEquals(20, mapService.getHeight());
        assertEquals(20, mapService.getWidth());
    }

    @Test
    void blockedWallReturnsTrue() {
        assertTrue(mapService.isBlocked(1,0));
    }

    @Test
    void grassReturnsNotBlocked() {
        assertFalse(mapService.isBlocked(0,0));
    }

    @Test
    void bridgeOnWaterNotBlocked() {
        assertFalse(mapService.isBlocked(6,9));
    }

    @Test
    void getStackReturnsNotNull() {
        assertNotNull(
                mapService.getStack(0,0)
        );
    }

    @Test
    void invalidYReturnsNullStack() {
        assertNull(
                mapService.getStack(-1,0)
        );
    }

    @Test
    void invalidXReturnsNullStack() {
        assertNull(
                mapService.getStack(0,-1)
        );
    }

    @Test
    void windowCorrectSize() {
        String[][] window =
                mapService.getWindow(5,5,11);

        assertEquals(11, window.length);
        assertEquals(11, window[0].length);
    }

    @Test
    void doorExistsAsStructureLayer() {

        var stack =
                mapService.getStack(9,15);

        boolean found =
                stack.getLayers()
                        .stream()
                        .anyMatch(t ->
                                t.getLayer()
                                        == TileLayer.STRUCTURE);

        assertTrue(found);
    }
}