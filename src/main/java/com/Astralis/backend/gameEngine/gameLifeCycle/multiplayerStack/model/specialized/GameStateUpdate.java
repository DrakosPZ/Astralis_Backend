package com.Astralis.backend.gameEngine.gameLifeCycle.multiplayerStack.model.specialized;

import com.Astralis.backend.gameEngine.gameLogic.model.LogicGameState;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode()
public class GameStateUpdate implements SpecializedMessage{
    private LogicGameState logicGameState;
}
