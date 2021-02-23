package com.Astralis.backend.gameLogic.model;

import com.Astralis.backend.gameDatabase.model.Ship;
import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class mShip extends AbstractMemoryModel {

    private mPosition targetMPosition;
    private mPosition currentMPosition;

    private double movementSpeed;

    public mShip(Ship model){
        super();
        this.id = model.getId() < 0 ?  0 : model.getId();

        this.targetMPosition = new mPosition(model.getTargetPosition());
        this.currentMPosition = new mPosition(model.getCurrentPosition());
        this.movementSpeed = model.getMovementSpeed() < 0 ?  0 : model.getMovementSpeed();
    }
}
