package com.Astralis.backend.gameLogic.model;

import com.Astralis.backend.gameDatabase.model.Position;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class mPosition implements Serializable {
    private double x;
    private double y;

    public mPosition(Position model){
        super();
        this.x = model.getX();
        this.y = model.getY();
    }
}
