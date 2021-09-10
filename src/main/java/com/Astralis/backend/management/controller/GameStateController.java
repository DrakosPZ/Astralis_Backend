package com.Astralis.backend.management.controller;

import com.Astralis.backend.management.DataHolders.GameUserIDSet;
import com.Astralis.backend.management.DataHolders.GameUserRoleSet;
import com.Astralis.backend.management.dto.CustomeDetailDTOs.DetailGameStateDTO;
import com.Astralis.backend.management.dto.GameStateDTO;
import com.Astralis.backend.management.service.GameStateService;
import com.Astralis.backend.gameEngine.gameLifeCycle.logicLoop.GameLoop;
import com.Astralis.backend.gameEngine.gameLifeCycle.logicLoop.GameLoopManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@RequestMapping(path = "/gamestate")
@CrossOrigin("http://localhost:4200")
public class GameStateController extends AbstractController<GameStateDTO> {
    private final GameStateService service;
    private final GameLoopManager gameLoopManager;

    /**
     * calls the find all method of the appropriate service.
     *
     * @return a List of DTO Objects.
     */
    @Override
    List<GameStateDTO> findAllDTO() {
        return service.findAll();
    }

    /**
     * calls the findbyIdentifier method of the appropriate service.
     *
     * @return a DTO object.
     */
    @Override
    Optional<GameStateDTO> findByIdentifierDTO(String identifier) {
        return service.findModelByIdentifier(identifier);
    }

    /**
     * calls the save method of the appropriate service.
     * Before that also checks if Username is already in use.
     *
     * @return the saved DTO object.
     */
    @Override
    Optional<GameStateDTO> saveDTO(Optional<GameStateDTO> optionaldto) {
        return service.save(optionaldto);
    }

    /**
     * calls the update method of the appropriate service.
     *
     * @return the updated DTO object.
     */
    @Override
    Optional<GameStateDTO> updateDTO(Optional<GameStateDTO> optionaldto) {
        return service.update(optionaldto);
    }

    /**
     * calls the deleteByIdentifier method of the appropriate service.
     *
     * @return the deleted DTO object.
     */
    @Override
    Optional<GameStateDTO> deleteByIdentifierDTO(String identifier) {
        Optional<GameStateDTO> optionaldto = service.findModelByIdentifier(identifier);
        service.deleteModelByIdentifier(identifier);
        return optionaldto;
    }









    //----------------------Custom Route Methods----------------------
    /**
     * Get route to return all Games a given User has joined.
     *
     * @param identifier the id of the to be checked User.
     * @return a List of Joined Games.
     */
    @GetMapping(path = "/joinedGame", params = "identifier")
    public ResponseEntity<CollectionModel<GameStateDTO>> findAllJoinedGames(@RequestParam String identifier) {
        return ResponseEntity.ok(
                new CollectionModel<>(
                        service.findAllJoinedGames(identifier)
                                .stream()
                                .map(this::addSelfLink)
                                .collect(Collectors.toList())
                )
        );
    }

    /**
     * Get route to return a Detail View of the searched for GameState Object.
     *
     * @param identifier of the searched gameState.
     * @return the detail GameStateDTO.
     */
    @GetMapping(path = "/detailsFor", params = "identifier")
    public ResponseEntity<DetailGameStateDTO> findGameStateAsDetail(@RequestParam String identifier) {
        Optional<GameStateDTO> find = findByIdentifierDTO(identifier);
        if(find.isEmpty())
        {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.of(
                service.findGameStateAsDetail(identifier)
        );
    }


    /**
     * Get route to return all Games containing a given identifier.
     *
     * @param identifier of the searched gameState.
     * @return a List of filtered gameStates.
     */
    @GetMapping(path = "/searchFor", params = "identifier")
    public ResponseEntity<CollectionModel<GameStateDTO>> findAllGamesContainingIdentifier(@RequestParam String identifier) {
        return ResponseEntity.ok(
                new CollectionModel<>(
                        service.findAllGamesContainIdentifier(identifier)
                                .stream()
                                .map(this::addSelfLink)
                                .collect(Collectors.toList())
                )
        );
    }

    /**
     * Get route to return all Games containing a given name.
     *
     * @param name of the searched gameState.
     * @return a List of filtered gameStates.
     */
    @GetMapping(path = "/searchFor", params = "name")
    public ResponseEntity<CollectionModel<GameStateDTO>> findAllGamesContainingName(@RequestParam String name) {
        return ResponseEntity.ok(
                new CollectionModel<>(
                        service.findAllGamesContainName(name)
                                .stream()
                                .map(this::addSelfLink)
                                .collect(Collectors.toList())
                )
        );
    }

    /**
     * Post route to create a new game and add the User to it.
     *
     * @param holder holds the identifier of the gamestate object, and the user object to be added.
     * @return the changed GameStateDTO.
     */
    @PostMapping(path = "/createNewGame", consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<GameStateDTO> createNewGame(
            @RequestBody GameUserIDSet holder) {
        GameStateDTO gameStateDTO = holder.getGameState();
        gameStateDTO = saveDTO(Optional.of(gameStateDTO))
                .orElseThrow(() -> new IllegalArgumentException("Error when creating new Game"));

        String identifierGS = gameStateDTO.getIdentifier();
        String identifierU = holder.getUserIdentifier();
        return ResponseEntity.ok(
                service.addUserToGameState(
                        identifierGS,
                        identifierU)
                        .map(this::addSelfLink)
                        .orElseThrow(IllegalArgumentException::new)
        );
    }

    /**
     * Post route to add a User to an existing Game.
     *
     * @param holder holds the identifier of the gamestate object, and the user object to be added.
     * @return the changed GameStateDTO.
     */
    @PostMapping(path = "/addUser", consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<GameStateDTO> addUser(
            @RequestBody Map<String, String> holder) {
        String identifierGS = holder.keySet().stream()
                .map(key -> key)
                .collect(Collectors.toList()).get(0);
        String identifierU = holder.keySet().stream()
                .map(key -> holder.get(key))
                .collect(Collectors.toList()).get(0);
        return ResponseEntity.ok(
                service.addUserToGameState(
                        identifierGS,
                        identifierU)
                        .map(this::addSelfLink)
                        .orElseThrow(IllegalArgumentException::new)
        );
    }


    /**
     * Delete route to remove a User from a Game.
     *
     * @param holder holds the identifier of the gamestate object, and the user object to be added.
     * @return the changed GameStateDTO, if it was deleted in the process, the identifier is empty.
     */
    @PostMapping(path = "/removeUser", consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<GameStateDTO> removeUser(
            @RequestBody Map<String, String> holder) {
        String identifierGS = holder.keySet().stream()
                .map(key -> key)
                .collect(Collectors.toList()).get(0);
        String identifierU = holder.keySet().stream()
                .map(key -> holder.get(key))
                .collect(Collectors.toList()).get(0);
        return ResponseEntity.ok(
                service.removeUserFromGameState(
                        identifierGS,
                        identifierU)
                        .map(this::addSelfLink)
                        .orElseThrow(IllegalArgumentException::new)
        );
    }


    /**
     * Put route to change the Role of a User in a given Game.
     *
     * @param holder holds a Map with a GameState, User Identifier Map and a String for the Role to be changed to.
     * @return the changed GameStateDTO.
     */
    @PutMapping(path = "/changeGameUserRole", consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<GameStateDTO> changeRoleFromUserInGameState(
            @RequestBody GameUserRoleSet holder) {
        String identifierGS = holder.getIdentifierGameState();
        String identifierU = holder.getIdentifierUser();
        String role = holder.getRole();
        return ResponseEntity.ok(
                service.changeRoleOfGamePlayer(
                        identifierGS,
                        identifierU,
                        role )
                        .map(this::addSelfLink)
                        .orElseThrow(IllegalArgumentException::new)
        );
    }





    /**
     * Get Route to start the given Game.
     * If the Game hasn't been initialized yet it is first initialized and then started.
     * Starting means the game is interactable for players, i.e: Joining, stopping it, storing it, etc. .
     * All further checks and behaviour is done by the according services.
     *
     * @param identifier the given Game State Identifier which should be started.
     * @return updated Detail Game with new Status.
     */
    @GetMapping(path = "/startGame", params = "identifier")
    public ResponseEntity<DetailGameStateDTO> startGame(@RequestParam String identifier) {
        service.startGame(identifier);

        return findGameStateAsDetail(identifier);
    }

    /**
     * Get Route to stop the given Game. Game is stored before closing.
     * Closing means players cannot join it while it is stopped.
     * All further checks and behaviour is done by the according services.
     *
     * @param identifier the given Game State Identifier which should be stopped.
     * @return updated Detail Game with new Status.
     */
    @GetMapping(path = "/stopGame", params = "identifier")
    public ResponseEntity<DetailGameStateDTO> stopGame(@RequestParam String identifier) {

        GameLoop gameLoop = gameLoopManager.findActiveGameLoop(identifier);

        if(gameLoop == null){
            throw new IllegalArgumentException("NO ACTIVE GAME FOUND WITH IDENTIFIER: " + identifier);
        }

        service.stopGame(gameLoop);
        return findGameStateAsDetail(identifier);
    }

    /**
     * Get Route to store the given Game.
     * Game is paused/locked before storing process starts and is opened again once done.
     * All further checks and behaviour is done by the according services.
     *
     * @param identifier the given Game State Identifier which should be stored.
     * @return updated Detail Game with new Status.
     */
    @GetMapping(path = "/storeGame", params = "identifier")
    public ResponseEntity<DetailGameStateDTO> storeGame(@RequestParam String identifier) {

        GameLoop gameLoop = gameLoopManager.findActiveGameLoop(identifier);

        if(gameLoop == null){
            throw new IllegalArgumentException("NO ACTIVE GAME FOUND WITH IDENTIFIER: " + identifier);
        }

        service.storeGame(gameLoop);

        return findGameStateAsDetail(identifier);
    }

    /**
     * Get Route to pause/unpause the given Game.
     * The given game is checked if it is already paused, and if so it is opened again.
     * Pausing means it is still interactable for players, but the game doesn't progress.
     * All further checks and behaviour is done by the according services.
     *
     * @param identifier the given Game State Identifier which should be paused/unpaused.
     * @return updated Detail Game with new Status.
     */
    @GetMapping(path = "/pauseGame", params = "identifier")
    public ResponseEntity<DetailGameStateDTO> pausingGame(@RequestParam String identifier) {

        GameLoop gameLoop = gameLoopManager.findActiveGameLoop(identifier);

        if(gameLoop == null){
            throw new IllegalArgumentException("NO ACTIVE GAME FOUND WITH IDENTIFIER: " + identifier);
        }

        service.pauseGame(gameLoop);
        return findGameStateAsDetail(identifier);
    }
}
