package com.questshaper.game;

import com.questshaper.game.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {

    private GameService gameService;
    private final String SESSION = "test";

    @BeforeEach
    void setUp() {
        MapService mapService = new MapService();
        gameService = new GameService(mapService);

        gameService.createSession(SESSION);
    }

    @Test
    void testSessionCreated() {
        Map<String, Object> info = gameService.getInfo(SESSION, 5, 5);

        assertNotNull(info);
        assertEquals(5, info.get("x"));
        assertEquals(5, info.get("y"));
    }

    @Test
    void testMoveValid() {
        boolean moved = gameService.move(SESSION, 1, 0);

        assertTrue(moved);

        Map<String, Object> info = gameService.getInfo(SESSION, 0, 0);
        assertEquals(6, info.get("y"));
    }

    @Test
    void testMoveBlocked_OutOfBounds() {
        // Force out-of-bounds (always blocked)
        boolean moved = gameService.move(SESSION, -100, 0);

        assertFalse(moved);
    }

    @Test
    void testRemoveSession() {
        gameService.removeSession(SESSION);

        Map<String, Object> info = gameService.getInfo(SESSION, 5, 5);

        assertNull(info);
    }

    @Test
    void testUnknownSessionFails() {
        boolean moved = gameService.move("unknown", 1, 0);

        assertFalse(moved);
    }
}