package com.Astralis.backend.gameLogic.model;

import com.Astralis.backend.gameDatabase.model.Ship;
import lombok.*;

import java.io.Serializable;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class mShip extends AbstractMemoryModel implements Serializable {

    private mPosition targetMPosition;
    private mPosition currentMPosition;

    private double movementSpeed;

    public mShip(Ship model){
        super();
        //this.id = model.getId() == null ?  -1 : model.getId();
        this.id = model.getId();

        this.targetMPosition = new mPosition(model.getTargetPosition());
        this.currentMPosition = new mPosition(model.getCurrentPosition());
        this.movementSpeed = model.getMovementSpeed() < 0 ?  0 : model.getMovementSpeed();
    }
}
