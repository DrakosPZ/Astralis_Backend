package com.Astralis.backend.gameEngine.gameLifeCycle.logicLoop;

import com.Astralis.backend.gameEngine.gameLogic.model.LogicGameState;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Getter
@NoArgsConstructor
public class GameLoopManager {
    private List<GameLoop> gameLoops = new ArrayList<>();
    private final long timeoutMillis = 0; // 0 = no Timeout

    //TODO: Add Documentation
    public void addGameLoop(String gameStateID, LogicGameState LogicGameState){
        GameLoop gameLoop = new GameLoop();
        gameLoops.add(gameLoop);
        gameLoop.startLoop(gameStateID, LogicGameState);
    }

    //TODO: Add Documentation
    public void removeGameLoop(GameLoop gameLoop){
        gameLoop.endLoop();
        gameLoops.remove(gameLoop);
    }

    //TODO: Add Documentation
    public GameLoop findActiveGameLoop(String identifier) {
        return gameLoops.stream()
                .filter(loop -> identifier.equals(loop.getID()))
                .findAny()
                .orElseThrow(() -> {
                    throw new IllegalArgumentException("GameLoopManager: no Active Game Loop found with given ID");
                });
    }

}
