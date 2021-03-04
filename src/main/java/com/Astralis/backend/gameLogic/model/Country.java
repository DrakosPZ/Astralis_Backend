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
public class Country extends AbstractMemoryModel implements Serializable {
    private String name;
    //private User leadingUser;
    private Ship ship;
    private String colour;

}
