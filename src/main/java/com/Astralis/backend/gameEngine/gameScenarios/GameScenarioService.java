package com.Astralis.backend.gameEngine.gameScenarios;

import com.Astralis.backend.gameEngine.gameLifeCycle.logicLoop.GameLoop;
import com.Astralis.backend.gameEngine.gameLogic.model.Country;
import com.Astralis.backend.gameEngine.gameLogic.model.LogicGameState;
import com.Astralis.backend.gameEngine.gameLogic.model.Position;
import com.Astralis.backend.gameEngine.gameLogic.model.Ship;
import com.Astralis.backend.management.model.GameState;
import com.Astralis.backend.management.model.GameStatus;

import java.util.ArrayList;
import java.util.List;

public class GameScenarioService {

    public void initializeNormalGame(){
        GameLoop gameLoop = new GameLoop();

        List<Country> countries = new ArrayList<>();
        countries.add(Country.builder()
                .name("Player1")
                .ship(Ship.builder()
                        .currentPosition(new Position(0, 0))
                        .targetPosition(new Position(100, 100))
                        .movementSpeed(100)
                        .build())
                .build());
        countries.add(Country.builder()
                .name("Player2")
                .ship(Ship.builder()
                        .currentPosition(new Position(0, 0))
                        .targetPosition(new Position(-100, -100))
                        .movementSpeed(10)
                        .build())
                .build());

        GameState gameState = new GameState();
        gameState.setIdentifier("GID0");

        LogicGameState LogicGameState = new LogicGameState(gameState, GameStatus.RUNNING, 4000, 1, 1, 0, countries);

        gameLoop.startLoop("GID0", LogicGameState);
    }

}
