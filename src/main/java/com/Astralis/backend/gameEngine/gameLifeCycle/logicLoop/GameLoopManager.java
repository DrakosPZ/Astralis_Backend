package com.Astralis.backend.gameEngine.gameLifeCycle.logicLoop;

import com.Astralis.backend.gameEngine.gameLogic.model.LogicGameState;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to hold and manage all the active GameLoops.
 */
@Component
@Getter
@NoArgsConstructor
public class GameLoopManager {
    private List<GameLoop> gameLoops = new ArrayList<>();
    private final long timeoutMillis = 0; // 0 = no Timeout

    /**
     * Method to add a gameLoop to the active list.
     * For this it initializes a new GameLoop forwarding the needed information to the according method.
     *
     * @param gameStateID The ID of the started gameState.
     * @param LogicGameState The gameState holding running game information.
     */
    public void addGameLoop(String gameStateID, LogicGameState LogicGameState){
        GameLoop gameLoop = new GameLoop();
        gameLoops.add(gameLoop);
        gameLoop.startLoop(gameStateID, LogicGameState);
    }

    /**
     * Method removing the given gameLoop from the active Loop list.
     * Before doing so, it stops the gameLoop.
     *
     * @param gameLoop The to be removed gameLoop.
     */
    public void removeGameLoop(GameLoop gameLoop){
        gameLoop.endLoop();
        gameLoops.remove(gameLoop);
    }

    /**
     * Method to return the activeGameloop based on the given ID.
     *
     * @param identifier The ID of the looked for gameLoop.
     * @return The searched for gameLoop.
     */
    public GameLoop findActiveGameLoop(String identifier) {
        return gameLoops.stream()
                .filter(loop -> identifier.equals(loop.getID()))
                .findAny()
                .orElseThrow(() -> {
                    throw new IllegalArgumentException("GameLoopManager: no Active Game Loop found with given ID");
                });
    }

}
