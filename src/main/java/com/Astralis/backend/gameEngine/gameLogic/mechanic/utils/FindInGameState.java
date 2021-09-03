package com.Astralis.backend.gameEngine.gameLogic.mechanic.utils;

import com.Astralis.backend.gameEngine.gameLogic.model.Country;
import com.Astralis.backend.gameEngine.gameLogic.model.LogicGameState;
import com.Astralis.backend.gameEngine.gameLogic.model.Ship;

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
