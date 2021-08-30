package com.Astralis.backend.multiplayerStack.web.model.specialized;

import com.Astralis.backend.gameLogic.model.Position;
import com.Astralis.backend.multiplayerStack.web.model.MessageSpecialized;
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
