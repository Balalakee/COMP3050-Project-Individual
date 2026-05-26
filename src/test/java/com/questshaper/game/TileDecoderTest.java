package com.questshaper.game;

import com.questshaper.game.util.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TileDecoderTest {

    @Test
    void decodeGrass() {

        var stack =
                TileDecoder.decode("g");

        assertEquals(
                1,
                stack.getLayers().size()
        );
    }

    @Test
    void decodeDoor() {

        var stack =
                TileDecoder.decode("fD");

        assertEquals(
                2,
                stack.getLayers().size()
        );
    }

    @Test
    void decodeItem() {

        var stack =
                TileDecoder.decode("gk");

        assertEquals(
                2,
                stack.getLayers().size()
        );
    }

    @Test
    void decodePlayer() {

        var stack =
                TileDecoder.decode("g1");

        assertEquals(
                2,
                stack.getLayers().size()
        );
    }
}