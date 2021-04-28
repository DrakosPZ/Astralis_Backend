package com.Astralis.backend.gameLogic.mechanic.utils;

import com.Astralis.backend.gameLogic.model.Country;
import com.Astralis.backend.gameLogic.model.LogicGameState;
import com.Astralis.backend.gameLogic.model.Ship;

public class FindInGameState {

    public static Ship in(LogicGameState logicGameState,long id){
        for (Country country: logicGameState.getCountries()) {
            if(country.getShip().getId() == id){
                return country.getShip();
            }
        }
        throw new IllegalArgumentException("Ship not found in GameState");
    }
}
