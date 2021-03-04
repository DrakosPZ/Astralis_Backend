package com.Astralis.backend.gameDatabase.controller;

import com.Astralis.backend.accountManagement.dto.UserDTO;
import com.Astralis.backend.gameDatabase.GameMasterService;
import com.Astralis.backend.gameLogic.model.mCountry;
import com.Astralis.backend.gameLogic.model.mLogicGameState;
import com.Astralis.backend.gameLogic.model.mPosition;
import com.Astralis.backend.gameLogic.model.mShip;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@RequestMapping(path = "/gameMasterEndpoint")
public class GameMasterController{
    private final GameMasterService service;

    Optional<mLogicGameState> findByIdModel(long id) {
        return service.findModelById(id);
    }

    //----------------------Custom Route Methods----------------------
    @GetMapping(path = "/storedLogicGameState", params = "id")
    public ResponseEntity<mLogicGameState> getStoredLogicGameState(@RequestParam long id) {
        /*Optional<mLogicGameState> find = findByIdModel(id);
        if(find.isEmpty())
        {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }*/
        return ResponseEntity.of(
                service.loadGameStateFromDatabase(id)
        );
    }

    @PostMapping(path = "/storeChangedLogicGameState", consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<mLogicGameState> storeChangedLogicGameState(
            @RequestBody
            mLogicGameState body) {
        return ResponseEntity.of(
                service.storeGameStateToDatabase(body)
        );
    }

    /**
     * Delete route to delete an object based on the given id
     *
     * @param id of the to be deleted object
     * @return the deleted object, encased in a ResponseEntity.
     */
    @DeleteMapping(params = "id")
    public Optional<mLogicGameState> delete(@RequestParam long id)
    {
        Optional<mLogicGameState> optionaldto = service.findModelById(id);
        service.deleteModelById(id);
        return optionaldto;
    }

    @GetMapping(path = "/storeTestState")
    public ResponseEntity<mLogicGameState> getStoredTestState() {

        mShip ship = mShip.builder()
                .currentMPosition(new mPosition(10,10))
                .targetMPosition(new mPosition(10,10))
                .movementSpeed(10d)
                .build();

        mCountry country = mCountry.builder()
                .name("Test Country")
                .colour("blue")
                .mShip(ship)
                .build();

        ArrayList countries= new ArrayList();
        countries.add(country);

        mLogicGameState testState = mLogicGameState.builder()
                .year(10)
                .month(10)
                .day(10)
                .hour(10)
                .year(10)
                .countries(countries)
                .build();

        return ResponseEntity.of(service.storeGameStateToDatabase(testState));
    }

}
