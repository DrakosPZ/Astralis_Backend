package com.Astralis.backend.gameLogic.model;

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

    //ReverseMapping
    private mCountry belongsTo;







    //----------------------1:1 Relationship Methods----------------------
    public void setCurrentOwner(mCountry mCountry){
        if(this.belongsTo != null) {
            if (this.belongsTo.equals(mCountry)) {
                return;
            }
        }
        this.belongsTo = mCountry;
        mCountry.setMShip(this);
    }






    //----------------------1:N Relationship Methods----------------------







    //----------------------N:1 Relationship Methods----------------------









    //----------------------N:M Relationship Methods----------------------
}
