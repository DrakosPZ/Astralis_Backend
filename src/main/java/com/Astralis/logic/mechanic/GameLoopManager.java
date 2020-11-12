package com.Astralis.logic.mechanic;

import com.Astralis.backend.service.GameStateService;
import com.Astralis.logic.model.Country;
import com.Astralis.logic.model.LogicGameState;
import com.Astralis.logic.model.Position;
import com.Astralis.logic.model.Ship;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;

@Service
@Getter
@AllArgsConstructor
public class GameLoopManager {
    private List<GameLoop> gameLoops = new ArrayList<>();
    private final long timeoutMillis = 0; // 0 = no Timeout

    public void addGameLoop(String gameStateID, LogicGameState logicGameState, SseEmitter emitter){
        GameLoop gameLoop = new GameLoop();
        gameLoops.add(gameLoop);
        gameLoop.startLoop(gameStateID, logicGameState, emitter);
    }

    public void removeGameLoop(GameLoop gameLoop){
        gameLoop.endLoop();
        gameLoops.remove(gameLoop);
    }

    public GameLoop findActiveGameLoop(String identifier){
        return gameLoops.stream()
                .filter(loop -> identifier.equals(loop.getID()))
                .findAny()
                .orElse(null);
    }
}
