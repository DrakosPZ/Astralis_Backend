package com.Astralis.backend.gameDatabase.model;

import lombok.*;
import com.Astralis.backend.gameLogic.model.mPosition;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Position implements Serializable {
    private double x;
    private double y;

    public Position(mPosition memory){
        super();
        this.x = memory.getX();
        this.y = memory.getY();
    }
}
