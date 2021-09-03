package com.Astralis.backend.gameEngine.gameLifeCycle.multiplayerStack.model.specialized;

import com.Astralis.backend.gameEngine.gameLogic.model.Position;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode()
public class MoveShip implements SpecializedMessage {
    private Long shipId;
    private Position newGoal;

    public String toString(){
        return "Ship " + getShipId() + "to new Position(" + getNewGoal().getX() + "/" + getNewGoal().getY() + ")";
    }
}
