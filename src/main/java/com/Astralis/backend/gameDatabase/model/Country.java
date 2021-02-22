package com.Astralis.backend.gameDatabase.model;

import com.Astralis.backend.accountManagement.model.AbstractModel;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Country extends AbstractGameModel {
    private String name;
    //private User leadingUser;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "belongsTo", orphanRemoval = true)
    private Ship ship;
    private String colour;

    //ReverseMapping
    @ManyToOne(
            cascade = {CascadeType.PERSIST},
            fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id")
    private LogicGameState logicGameState;






    //----------------------1:1 Relationship Methods----------------------
    public void setShip(Ship ship){
        if(this.ship != null) {
            if (this.ship.equals(ship)) {
                return;
            }
        }
        this.ship = ship;
        ship.setCurrentOwner(this);
    }






    //----------------------1:N Relationship Methods----------------------







    //----------------------N:1 Relationship Methods----------------------
    public void setLogicGameState(LogicGameState logicGameState) {
        if (Objects.equals(this.logicGameState, logicGameState)) {
            return;
        }

        LogicGameState oldLogicGameState = this.logicGameState;
        this.logicGameState = logicGameState;

        if (oldLogicGameState != null) {
            oldLogicGameState.removeCountry(this);
        }

        if (logicGameState != null) {
            logicGameState.addCountry(this);
        }
    }









    //----------------------N:M Relationship Methods----------------------
}
