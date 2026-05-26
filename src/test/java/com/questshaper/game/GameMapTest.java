package com.questshaper.game;

import com.questshaper.game.model.*;
import com.questshaper.game.model.tiles.TileStack;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameMapTest {

    @Test
    void dimensionsCorrect() {

        GameMap map =
                new GameMap(20,20);

        assertEquals(20,map.getHeight());
        assertEquals(20,map.getWidth());
    }

    @Test
    void getStackWorks() {

        GameMap map =
                new GameMap(5,5);

        TileStack stack = map.getStack(1,1);

        assertNotNull(stack);
        assertSame(stack, map.getStack(1,1));
    }

    @Test
    void outOfBoundsReturnsNull() {

        GameMap map =
                new GameMap(5,5);

        assertNull(
                map.getStack(-1,0)
        );
    }
}