package com.Astralis.backend.gameLogic.model;

import lombok.*;

import java.io.Serializable;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Ship extends AbstractMemoryModel implements Serializable {

    private Position targetPosition;
    private Position currentPosition;

    private double movementSpeed;

}
