package com.Astralis.backend.gameLogic.model;

import com.Astralis.backend.accountManagement.model.GameState;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class mLogicGameState extends AbstractMemoryModel {
    private GameState gameState;

    //#possibly put into own class ffs
    private int year;
    private int month;
    private int day;
    private int hour;

    private List<mCountry> countries;
    //private GameMap map;







    //----------------------1:1 Relationship Methods----------------------
    public void setGameState(GameState gameState){
        if(this.gameState != null) {
            if (this.gameState.equals(gameState)) {
                return;
            }
        }
        this.gameState = gameState;
        gameState.setCurrentState(this);
    }






    //----------------------1:N Relationship Methods----------------------
    public void addCountry(mCountry mCountry) {
        if (countries.contains(mCountry)) {
            return;
        }
        countries.add(mCountry);
        mCountry.setMLogicGameState(this);
    }

    public void removeCountry(mCountry mCountry) {
        if (!countries.contains(mCountry)) {
            return;
        }
        mCountry.setMLogicGameState(null);
        countries.remove(mCountry);
    }







    //----------------------N:1 Relationship Methods----------------------









    //----------------------N:M Relationship Methods----------------------
}
