package com.Astralis.backend.gameEngine.gameScenarios;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * A Controller to supply routes, meant to test specific systems in the game.
 * Everything is supposed to be automatically setup and initialized and accessible of a specific frontend screen only
 * accessible to admins.
 *
 * AT THIS POINT IT ONLY CREATES A BACKEND TICKER TO BE INSPECTED BY THE CONSOLE
 * ADD FUNCTIONALITY THAT IT ACTUALLY IS BEING SHOWN AS A RUNNING SCNEARIO IN THE ADMIN SCREEN.
 */
@RestController
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@RequestMapping(path = "/gameScenariosEndpoint")
public class GameScenarioController {
    private final GameScenarioService service;

    @GetMapping(path = "/storeTestState")
    public void getStoredTestState() {
        service.initializeNormalGame();
    }
}
