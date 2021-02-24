package com.Astralis.backend.gameDatabase.model;

import com.Astralis.backend.gameLogic.model.mPosition;
import lombok.*;
import com.Astralis.backend.gameLogic.model.mShip;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Ship extends AbstractGameModel {
    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "x", column = @Column(name = "target_x")),
            @AttributeOverride( name = "y", column = @Column(name = "target_y"))
    })
    private Position targetPosition;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "x", column = @Column(name = "current_x")),
            @AttributeOverride( name = "y", column = @Column(name = "current_y"))
    })
    private Position currentPosition;
    private double movementSpeed;

    //ReverseMapping
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name="ship")
    private Country belongsTo;

    public Ship(mShip memory){
        super(memory.getId());

        this.targetPosition = new Position(memory.getTargetMPosition());
        this.currentPosition = new Position(memory.getCurrentMPosition());
        this.movementSpeed = memory.getMovementSpeed() < 0 ?  0 : memory.getMovementSpeed();
    }






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
