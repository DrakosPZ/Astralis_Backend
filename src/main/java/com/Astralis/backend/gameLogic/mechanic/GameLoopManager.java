package com.Astralis.backend.gameLogic.mechanic;

import com.Astralis.backend.accountManagement.model.GameStatus;
import com.Astralis.backend.gameLogic.model.LogicGameState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;

@Component
@Getter
@NoArgsConstructor
public class GameLoopManager {
    private List<GameLoop> gameLoops = new ArrayList<>();
    private final long timeoutMillis = 0; // 0 = no Timeout

    //TODO: Add Documentation
    public void addGameLoop(String gameStateID, LogicGameState LogicGameState, SseEmitter emitter){
        GameLoop gameLoop = new GameLoop();
        gameLoops.add(gameLoop);
        gameLoop.startLoop(gameStateID, LogicGameState, emitter);
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
