package com.Astralis.backend.gameEngine.gameStateManagement.dataHolders;

import com.Astralis.backend.gameEngine.gameLogic.model.GameState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GameStateGameNameSet {
    private GameState gameState;
    private String gameName;
}
