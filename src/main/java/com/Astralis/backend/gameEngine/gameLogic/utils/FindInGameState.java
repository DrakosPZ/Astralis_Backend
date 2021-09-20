package com.Astralis.backend.gameEngine.gameLogic.utils;

import com.Astralis.backend.gameEngine.gameLogic.model.Country;
import com.Astralis.backend.gameEngine.gameLogic.model.GameState;
import com.Astralis.backend.gameEngine.gameLogic.model.Ship;

public class FindInGameState {

    public static Ship in(GameState gameState, long id){
        for (Country country: gameState.getCountries()) {
            if(country.getShip().getId() == id){
                return country.getShip();
            }
        }
        throw new IllegalArgumentException("Ship not found in GameState");
    }
}
