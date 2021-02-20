package com.Astralis.backend.gameLogic.model;

import com.Astralis.backend.accountManagement.model.AbstractModel;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Ship extends AbstractModel {
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
