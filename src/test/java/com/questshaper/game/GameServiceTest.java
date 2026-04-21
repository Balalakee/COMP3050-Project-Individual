package com.questshaper.game;

import com.questshaper.game.service.GameService;
import com.questshaper.game.service.MapService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {

    private GameService gameService;

    @BeforeEach
    void setup() {
        gameService = new GameService(new MapService());
    }

    @Test
    void testMoveDoesNotCrash() {
        boolean result = gameService.move(0, 1);
        assertNotNull(result);
    }

    @Test
    void testBlockedMoveReturnsFalse() {
        boolean result = gameService.move(-10, 0);
        assertFalse(result);
    }

    @Test
    void testGetInfoReturnsMap() {
        var info = gameService.getInfo(5, 5);

        assertNotNull(info);
        assertTrue(info.containsKey("info"));
        assertTrue(info.containsKey("x"));
        assertTrue(info.containsKey("y"));
    }

    @Test
    void testInfoWindowSizeIs11() {
        var info = gameService.getInfo(5, 5);

        var window = (java.util.List<?>) info.get("info");

        assertEquals(11, window.size());

        for (var row : window) {
            assertEquals(11, ((java.util.List<?>) row).size());
        }
    }
}