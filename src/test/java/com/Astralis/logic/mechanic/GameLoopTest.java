package com.Astralis.logic.mechanic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameLoopTest {
    private final GameLoop gameLoop = new GameLoop();

    @Test
    void testGameLoop() {
        gameLoop.startLoop(null, null);
    }

}