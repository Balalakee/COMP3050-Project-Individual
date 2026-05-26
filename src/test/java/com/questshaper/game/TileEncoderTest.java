package com.questshaper.game;

import com.questshaper.game.util.*;
import com.questshaper.game.model.tiles.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TileEncoderTest {

    @Test
    void grassEncodesCorrectly() {

        TileStack stack =
                new TileStack();

        stack.add(
                new Tile(
                        TileType.GRASS,
                        TileLayer.FLOOR
                )
        );

        assertEquals(
                "g",
                TileEncoder.encode(stack)
        );
    }

    @Test
    void doorEncodesCorrectly() {

        TileStack stack =
                new TileStack();

        stack.add(
                new Tile(
                        TileType.FLAGSTONES,
                        TileLayer.FLOOR
                )
        );

        stack.add(
                new Tile(
                        TileType.DOOR,
                        TileLayer.STRUCTURE
                )
        );

        assertEquals(
                "fD",
                TileEncoder.encode(stack)
        );
    }

    @Test
    void itemEncodesCorrectly() {

        TileStack stack =
                new TileStack();

        stack.add(
                new Tile(
                        TileType.GRASS,
                        TileLayer.FLOOR
                )
        );

        stack.add(
                new Tile(
                        TileType.KEY,
                        TileLayer.ITEM
                )
        );

        assertEquals(
                "gk",
                TileEncoder.encode(stack)
        );
    }

    @Test
    void characterEncodesLast() {

        TileStack stack =
                new TileStack();

        stack.add(
                new Tile(
                        TileType.GRASS,
                        TileLayer.FLOOR
                )
        );

        stack.add(
                new Tile(
                        TileType.PLAYER_1,
                        TileLayer.CHARACTER
                )
        );

        assertEquals(
                "g1",
                TileEncoder.encode(stack)
        );
    }
}