package com.Astralis.backend.gameStateStoring;

import com.Astralis.backend.gameLogic.model.Country;
import com.Astralis.backend.gameLogic.model.LogicGameState;
import com.Astralis.backend.gameLogic.model.Position;
import com.Astralis.backend.gameLogic.model.Ship;
import com.Astralis.backend.gameStateStoring.dataHolders.LogicGameStateGameNameSet;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@RequestMapping(path = "/gameMasterEndpoint")
public class GameMasterController{
    private final GameMasterService service;

    //----------------------Custom Route Methods----------------------
    @GetMapping(path = "/storedLogicGameState", params = "storageFolder")
    public ResponseEntity<LogicGameState> getStoredLogicGameState(@RequestParam String storageFolder) {
        return ResponseEntity.of(
                service.loadGameStateFromDatabase("storage//gameState//" + storageFolder)
        );
    }

    @PostMapping(path = "/storeChangedLogicGameState", consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<LogicGameState> storeChangedLogicGameState(
            @RequestBody
                    LogicGameStateGameNameSet body) {
        return ResponseEntity.of(
                service.storeGameStateToDatabase(body.getLogicGameState(), body.getGameName())
        );
    }

    @GetMapping(path = "/storeTestState")
    public ResponseEntity<LogicGameState> getStoredTestState() {

        Ship ship = Ship.builder()
                .currentPosition(new Position(10,10))
                .targetPosition(new Position(10,10))
                .movementSpeed(10d)
                .build();

        Country country = Country.builder()
                .name("Test Country")
                .colour("blue")
                .ship(ship)
                .build();

        ArrayList countries= new ArrayList();
        countries.add(country);

        LogicGameState testState = LogicGameState.builder()
                .year(10)
                .month(10)
                .day(10)
                .hour(10)
                .year(10)
                .countries(countries)
                .build();

        return ResponseEntity.of(service.storeGameStateToDatabase(testState,"TestGame"));
    }

    @GetMapping(path = "/loadTestState")
    public ResponseEntity<LogicGameState> loadTestState() {
        return ResponseEntity.of(service.loadGameStateFromDatabase("storage//gameState//TestGame"));
    }

}
