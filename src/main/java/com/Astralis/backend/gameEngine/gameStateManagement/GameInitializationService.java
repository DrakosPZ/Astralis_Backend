package com.Astralis.backend.gameEngine.gameStateManagement;

import com.Astralis.backend.gameEngine.gameLogic.model.Country;
import com.Astralis.backend.gameEngine.gameLogic.model.LogicGameState;
import com.Astralis.backend.gameEngine.gameLogic.model.Position;
import com.Astralis.backend.gameEngine.gameLogic.model.Ship;
import com.Astralis.backend.management.model.GameLobby;
import com.Astralis.backend.management.model.GameStatus;
import com.Astralis.backend.management.model.UserGameLobby;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class GameInitializationService {

    /**
     * A Method to initialize and set all the start Data of a logicGameState
     * based of the given GameState.
     * Initializes CURRENTLY TEST Data. CHANGE LATER TO ACTUAL START DATA.
     *
     * @param gameLobby The database's gameState reference used to link the logicGameState to the lobby.
     */
    public LogicGameState initialize(GameLobby gameLobby){
        //Test Data - replace with proper galaxy initialization
        String player1ID = "";
        String player2ID = "";
        List<UserGameLobby> list = gameLobby.getUserGameLobbies();
        for (UserGameLobby userGameLobby : list) {
            if(userGameLobby.getUser().getNickName().equals("DrakoD")){
                player1ID = userGameLobby.getUser().getIdentifier();
            }
            if(userGameLobby.getUser().getNickName().equals("KuroK")){
                player2ID = userGameLobby.getUser().getIdentifier();
            }
        }



        List<Country> countries = new ArrayList<>();
        countries.add(Country.builder()
                .id(1L)
                .name("Player1")
                .ship(Ship.builder()
                        .id(2L)
                        .currentPosition(new Position(0, 0))
                        .targetPosition(new Position(100, 100))
                        .movementSpeed(100)
                        .build())
                .owner(player1ID)
                .build());
        countries.add(Country.builder()
                .id(3L)
                .name("Player2")
                .ship(Ship.builder()
                        .id(4L)
                        .currentPosition(new Position(0, 0))
                        .targetPosition(new Position(-100, -100))
                        .movementSpeed(10)
                        .build())
                .owner(player2ID)
                .build());
        LogicGameState logicGameState =
                new LogicGameState(null, GameStatus.RUNNING,4000, 1, 1, 0, countries);

        return logicGameState;
    }

}
