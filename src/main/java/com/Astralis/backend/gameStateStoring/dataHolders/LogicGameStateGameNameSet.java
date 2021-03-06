package com.Astralis.backend.gameStateStoring.dataHolders;

import com.Astralis.backend.gameLogic.model.LogicGameState;
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
