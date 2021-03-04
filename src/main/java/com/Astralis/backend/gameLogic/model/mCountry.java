package com.Astralis.backend.gameLogic.model;

import com.Astralis.backend.gameDatabase.model.Country;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class mCountry extends AbstractMemoryModel implements Serializable {
    private String name;
    //private User leadingUser;
    private mShip mShip;
    private String colour;

    public mCountry(Country model){
        super();
        //this.id = model.getId() == null ?  -1 : model.getId();
        this.id = model.getId();

        this.name = model.getName() == null ?  "" : model.getName();
        this.mShip = new mShip(model.getShip());
        this.colour = model.getColour() == null ?  "" : model.getColour();
    }
}
