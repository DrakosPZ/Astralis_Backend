package com.Astralis.backend.gameEngine.gameStateStoring.dataHolders;

import com.Astralis.backend.gameEngine.gameLogic.model.LogicGameState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LogicGameStateGameNameSet {
    private LogicGameState logicGameState;
    private String gameName;
}
