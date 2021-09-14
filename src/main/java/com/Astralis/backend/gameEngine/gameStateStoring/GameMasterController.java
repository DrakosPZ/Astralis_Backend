package com.Astralis.backend.gameEngine.gameStateStoring;

import com.Astralis.backend.gameEngine.gameLogic.model.Country;
import com.Astralis.backend.gameEngine.gameLogic.model.LogicGameState;
import com.Astralis.backend.gameEngine.gameLogic.model.Position;
import com.Astralis.backend.gameEngine.gameLogic.model.Ship;
import com.Astralis.backend.gameEngine.gameStateStoring.dataHolders.LogicGameStateGameNameSet;
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

    /**
     * Get Route to load the stored GameState File.
     *
     * @param storageFolder Required Name of the Folder where the File is stored.
     * @return the latest GameState out of the give folder.
     */
    @GetMapping(path = "/loadLogicGameState", params = "storageFolder")
    public ResponseEntity<LogicGameState> getStoredLogicGameState(@RequestParam String storageFolder) {
        return ResponseEntity.of(
                service.loadGameStateFromDatabase("storage//gameState//" + storageFolder)
        );
    }

    /**
     * Get Route to store a given LogicGameState as a GameFile to a folder defined by a custoem storeFolderName.
     *
     * @param body A custom object containing the to be stored LogicGameState and the name for the folder where it is
     *             supposed to be stored.
     * @return Returns the stored LogicGameState.
     */
    @PostMapping(path = "/storeChangedLogicGameState", consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<LogicGameState> storeChangedLogicGameState(
            @RequestBody
                    LogicGameStateGameNameSet body) {
        return ResponseEntity.of(
                service.storeGameStateToDatabase(body.getLogicGameState(), body.getGameName())
        );
    }



    //Test Method

    /**
     * Test Route meant to test if the storing of GameStates to a Folder/GameFile works.
     * Creates a test GameState with a country and a ship and stores the to the "TestGame" folder.
     *
     * @return The stored TestGame Object.
     */
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

    /**
     * Test Route meant to test if the loading of GameStates from a Folder/GameFile works.
     * It Loads a LogicGameState previously stored with a different test method from the "TestGame" folder.
     *
     * @return The loaded TestGame Object out of the "TestGame" folder.
     */
    @GetMapping(path = "/loadTestState")
    public ResponseEntity<LogicGameState> loadTestState() {
        return ResponseEntity.of(service.loadGameStateFromDatabase("storage//gameState//TestGame"));
    }

}
