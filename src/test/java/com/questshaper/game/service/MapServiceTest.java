// src/test/java/com/questshaper/game/service/MapServiceTest.java

package com.questshaper.game.service;

import com.questshaper.game.model.tiles.TileType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MapServiceTest {

    private MapService mapService;

    @BeforeEach
    void setup() {
        mapService = new MapService();
    }

    @Test
    void mapLoadsSuccessfully() {
        assertTrue(mapService.getWidth() > 0);
        assertTrue(mapService.getHeight() > 0);
    }

    @Test
    void mapHasExpectedDimensions() {
        assertEquals(20, mapService.getWidth());
        assertEquals(20, mapService.getHeight());
    }

    @Test
    void grassDoesNotBlockMovement() {
        assertFalse(mapService.isBlocked(0, 0));
    }

    @Test
    void waterBlocksMovement() {
        assertTrue(mapService.isBlocked(0, 10));
    }

    @Test
    void closedDoorBlocksMovement() {
        assertTrue(mapService.isBlocked(9, 15));
    }

    @Test
    void bridgeOverWaterIsWalkable() {
        assertFalse(mapService.isBlocked(6, 9));
    }

    @Test
    void horizontalWrappingWorks() {
        assertEquals(19, mapService.wrapX(-1));
        assertEquals(0, mapService.wrapX(20));
        assertEquals(1, mapService.wrapX(21));
    }

    @Test
    void yOutOfBoundsIsNotInBounds() {
        assertFalse(mapService.isYInBounds(-1));
        assertFalse(mapService.isYInBounds(20));
    }

    @Test
    void getEncodedTileForViewReturnsBlankForInvalidY() {
        assertEquals(" ", mapService.getEncodedTileForView(-1, 0));
        assertEquals(" ", mapService.getEncodedTileForView(20, 0));
    }

    @Test
    void getWindowReturnsCorrectSize() {
        String[][] window = mapService.getWindow(5, 5, 11);

        assertEquals(11, window.length);
        assertEquals(11, window[0].length);
    }

    @Test
    void windowWrapsAtLeftEdge() {
        String[][] window = mapService.getWindow(7, 0, 11);

        assertEquals(11, window.length);
        assertEquals(11, window[0].length);
        assertNotEquals(" ", window[5][0]);
    }

    @Test
    void itemCanBeReadFromKnownTile() {
        assertNotNull(mapService.getItem(0, 14));
    }

    @Test
    void removeItemRemovesKnownItem() {
        assertNotNull(mapService.getItem(0, 14));

        mapService.removeItem(0, 14);

        assertNull(mapService.getItem(0, 14));
    }

    @Test
    void placeItemPlacesItemIfTileHasNoItem() {
        mapService.removeItem(0, 0);

        assertTrue(mapService.placeItem(0, 0, TileType.KEY));
        assertNotNull(mapService.getItem(0, 0));
    }

    @Test
    void placeItemFailsIfTileAlreadyHasItem() {
        assertNotNull(mapService.getItem(0, 14));

        assertFalse(mapService.placeItem(0, 14, TileType.AXE));
    }

    @Test
    void toggleDoorChangesDoorState() {
        String before = mapService.getEncodedTileForView(4, 3);

        assertTrue(mapService.toggleDoor(4, 3));

        String after = mapService.getEncodedTileForView(4, 3);

        assertNotEquals(before, after);
    }

    @Test
    void toggleDoorReturnsFalseWhenNoDoorExists() {
        assertFalse(mapService.toggleDoor(0, 0));
    }
}