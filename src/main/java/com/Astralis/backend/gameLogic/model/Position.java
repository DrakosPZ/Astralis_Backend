package com.Astralis.backend.gameLogic.model;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Position implements Serializable {
    private double x;
    private double y;
}
