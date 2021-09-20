package com.Astralis.backend.gameEngine.gameStateManagement;

import com.Astralis.backend.gameEngine.gameLogic.model.Country;
import com.Astralis.backend.gameEngine.gameLogic.model.GameState;
import com.Astralis.backend.gameEngine.gameLogic.model.Position;
import com.Astralis.backend.gameEngine.gameLogic.model.Ship;
import com.Astralis.backend.gameEngine.gameStateManagement.dataHolders.GameStateGameNameSet;
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
    @GetMapping(path = "/loadGameState", params = "storageFolder")
    public ResponseEntity<GameState> getStoredGameState(@RequestParam String storageFolder) {
        return ResponseEntity.of(
                service.loadGameStateFromDatabase("storage//gameState//" + storageFolder)
        );
    }

    /**
     * Get Route to store a given GameState as a GameFile to a folder defined by a custoem storeFolderName.
     *
     * @param body A custom object containing the to be stored GameState and the name for the folder where it is
     *             supposed to be stored.
     * @return Returns the stored GameState.
     */
    @PostMapping(path = "/storeChangedGameState", consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<GameState> storeChangedGameState(
            @RequestBody
                    GameStateGameNameSet body) {
        return ResponseEntity.of(
                service.storeGameStateToDatabase(body.getGameState(), body.getGameName())
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
    public ResponseEntity<GameState> getStoredTestState() {

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

        GameState testState = GameState.builder()
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
     * It Loads a GameState previously stored with a different test method from the "TestGame" folder.
     *
     * @return The loaded TestGame Object out of the "TestGame" folder.
     */
    @GetMapping(path = "/loadTestState")
    public ResponseEntity<GameState> loadTestState() {
        return ResponseEntity.of(service.loadGameStateFromDatabase("storage//gameState//TestGame"));
    }

}
