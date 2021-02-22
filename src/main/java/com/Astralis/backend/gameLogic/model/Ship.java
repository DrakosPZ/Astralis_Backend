package com.Astralis.backend.gameLogic.model;

import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Ship extends AbstractMemoryModel {

    private Position targetPosition;
    private Position currentPosition;

    private double movementSpeed;

    //ReverseMapping
    private Country belongsTo;







    //----------------------1:1 Relationship Methods----------------------
    public void setCurrentOwner(Country country){
        if(this.belongsTo != null) {
            if (this.belongsTo.equals(country)) {
                return;
            }
        }
        this.belongsTo = country;
        country.setShip(this);
    }






    //----------------------1:N Relationship Methods----------------------







    //----------------------N:1 Relationship Methods----------------------









    //----------------------N:M Relationship Methods----------------------
}
