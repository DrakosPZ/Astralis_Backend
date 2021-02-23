package com.Astralis.backend.gameLogic.model;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class mCountry extends AbstractMemoryModel {
    private String name;
    //private User leadingUser;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "belongsTo", orphanRemoval = true)
    private mShip mShip;
    private String colour;

    //ReverseMapping
    @ManyToOne(
            cascade = {CascadeType.PERSIST},
            fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id")
    private mLogicGameState mLogicGameState;






    //----------------------1:1 Relationship Methods----------------------
    public void setMShip(mShip mShip){
        if(this.mShip != null) {
            if (this.mShip.equals(mShip)) {
                return;
            }
        }
        this.mShip = mShip;
        mShip.setCurrentOwner(this);
    }






    //----------------------1:N Relationship Methods----------------------







    //----------------------N:1 Relationship Methods----------------------
    public void setMLogicGameState(mLogicGameState mLogicGameState) {
        if (Objects.equals(this.mLogicGameState, mLogicGameState)) {
            return;
        }

        mLogicGameState oldMLogicGameState = this.mLogicGameState;
        this.mLogicGameState = mLogicGameState;

        if (oldMLogicGameState != null) {
            oldMLogicGameState.removeCountry(this);
        }

        if (mLogicGameState != null) {
            mLogicGameState.addCountry(this);
        }
    }









    //----------------------N:M Relationship Methods----------------------
}
