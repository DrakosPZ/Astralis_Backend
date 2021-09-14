package com.Astralis.backend.gameLogic.mechanic;

import com.Astralis.backend.gameEngine.gameLifeCycle.logicLoop.GameLoop;
import org.junit.jupiter.api.Test;

class GameLoopTest {
    private final GameLoop gameLoop = new GameLoop();

    @Test
    void testGameLoop() {
        gameLoop.startLoop(null, null);
    }

}