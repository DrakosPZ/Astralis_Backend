package com.Astralis.backend.management.controller;

import com.Astralis.backend.management.DataHolders.GameUserIDSet;
import com.Astralis.backend.management.DataHolders.GameUserRoleSet;
import com.Astralis.backend.management.dto.CustomeDetailDTOs.DetailGameLobbyDTO;
import com.Astralis.backend.management.dto.GameLobbyDTO;
import com.Astralis.backend.management.service.GameLobbyService;
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
@RequestMapping(path = "/gamelobby")
@CrossOrigin("http://localhost:4200")
public class GameLobbyController extends AbstractController<GameLobbyDTO> {
    private final GameLobbyService service;
    private final GameLoopManager gameLoopManager;

    /**
     * calls the find all method of the appropriate service.
     *
     * @return a List of DTO Objects.
     */
    @Override
    List<GameLobbyDTO> findAllDTO() {
        return service.findAll();
    }

    /**
     * calls the findbyIdentifier method of the appropriate service.
     *
     * @return a DTO object.
     */
    @Override
    Optional<GameLobbyDTO> findByIdentifierDTO(String identifier) {
        return service.findModelByIdentifier(identifier);
    }

    /**
     * calls the save method of the appropriate service.
     * Before that also checks if Username is already in use.
     *
     * @return the saved DTO object.
     */
    @Override
    Optional<GameLobbyDTO> saveDTO(Optional<GameLobbyDTO> optionaldto) {
        return service.save(optionaldto);
    }

    /**
     * calls the update method of the appropriate service.
     *
     * @return the updated DTO object.
     */
    @Override
    Optional<GameLobbyDTO> updateDTO(Optional<GameLobbyDTO> optionaldto) {
        return service.update(optionaldto);
    }

    /**
     * calls the deleteByIdentifier method of the appropriate service.
     *
     * @return the deleted DTO object.
     */
    @Override
    Optional<GameLobbyDTO> deleteByIdentifierDTO(String identifier) {
        Optional<GameLobbyDTO> optionaldto = service.findModelByIdentifier(identifier);
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
    public ResponseEntity<CollectionModel<GameLobbyDTO>> findAllJoinedGames(@RequestParam String identifier) {
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
     * Get route to return a Detail View of the searched for GameLobby Object.
     *
     * @param identifier of the searched GameLobby.
     * @return the detail GameLobbyDTO.
     */
    @GetMapping(path = "/detailsFor", params = "identifier")
    public ResponseEntity<DetailGameLobbyDTO> findGameLobbyAsDetail(@RequestParam String identifier) {
        Optional<GameLobbyDTO> find = findByIdentifierDTO(identifier);
        if(find.isEmpty())
        {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.of(
                service.findGameLobbyAsDetail(identifier)
        );
    }


    /**
     * Get route to return all Games containing a given identifier.
     *
     * @param identifier of the searched GameLobby.
     * @return a List of filtered GameLobbies.
     */
    @GetMapping(path = "/searchFor", params = "identifier")
    public ResponseEntity<CollectionModel<GameLobbyDTO>> findAllGamesContainingIdentifier(@RequestParam String identifier) {
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
     * @param name of the searched GameLobby.
     * @return a List of filtered GameLobbies.
     */
    @GetMapping(path = "/searchFor", params = "name")
    public ResponseEntity<CollectionModel<GameLobbyDTO>> findAllGamesContainingName(@RequestParam String name) {
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
     * @param holder holds the identifier of the GameLobby object, and the user object to be added.
     * @return the changed GameLobbyDTO.
     */
    @PostMapping(path = "/createNewGame", consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<GameLobbyDTO> createNewGame(
            @RequestBody GameUserIDSet holder) {
        GameLobbyDTO gameLobbyDTO = holder.getGameLobby();
        gameLobbyDTO = saveDTO(Optional.of(gameLobbyDTO))
                .orElseThrow(() -> new IllegalArgumentException("Error when creating new Game"));

        String identifierGS = gameLobbyDTO.getIdentifier();
        String identifierU = holder.getUserIdentifier();
        return ResponseEntity.ok(
                service.addUserToGameLobby(
                        identifierGS,
                        identifierU)
                        .map(this::addSelfLink)
                        .orElseThrow(IllegalArgumentException::new)
        );
    }

    /**
     * Post route to add a User to an existing Game.
     *
     * @param holder holds the identifier of the GameLobby object, and the user object to be added.
     * @return the changed GameLobbyDTO.
     */
    @PostMapping(path = "/addUser", consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<GameLobbyDTO> addUser(
            @RequestBody Map<String, String> holder) {
        String identifierGS = holder.keySet().stream()
                .map(key -> key)
                .collect(Collectors.toList()).get(0);
        String identifierU = holder.keySet().stream()
                .map(key -> holder.get(key))
                .collect(Collectors.toList()).get(0);
        return ResponseEntity.ok(
                service.addUserToGameLobby(
                        identifierGS,
                        identifierU)
                        .map(this::addSelfLink)
                        .orElseThrow(IllegalArgumentException::new)
        );
    }


    /**
     * Delete route to remove a User from a Game.
     *
     * @param holder holds the identifier of the GameLobby object, and the user object to be added.
     * @return the changed GameLobbyDTO, if it was deleted in the process, the identifier is empty.
     */
    @PostMapping(path = "/removeUser", consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<GameLobbyDTO> removeUser(
            @RequestBody Map<String, String> holder) {
        String identifierGS = holder.keySet().stream()
                .map(key -> key)
                .collect(Collectors.toList()).get(0);
        String identifierU = holder.keySet().stream()
                .map(key -> holder.get(key))
                .collect(Collectors.toList()).get(0);
        return ResponseEntity.ok(
                service.removeUserFromGameLobby(
                        identifierGS,
                        identifierU)
                        .map(this::addSelfLink)
                        .orElseThrow(IllegalArgumentException::new)
        );
    }


    /**
     * Put route to change the Role of a User in a given Game.
     *
     * @param holder holds a Map with a GameLobby, User Identifier Map and a String for the Role to be changed to.
     * @return the changedGameLobbyDTO.
     */
    @PutMapping(path = "/changeGameUserRole", consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<GameLobbyDTO> changeRoleFromUserInGameLobby(
            @RequestBody GameUserRoleSet holder) {
        String identifierGS = holder.getIdentifierGameLobby();
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
    public ResponseEntity<DetailGameLobbyDTO> startGame(@RequestParam String identifier) {
        service.startGame(identifier);

        return findGameLobbyAsDetail(identifier);
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
    public ResponseEntity<DetailGameLobbyDTO> stopGame(@RequestParam String identifier) {

        GameLoop gameLoop = gameLoopManager.findActiveGameLoop(identifier);

        if(gameLoop == null){
            throw new IllegalArgumentException("NO ACTIVE GAME FOUND WITH IDENTIFIER: " + identifier);
        }

        service.stopGame(gameLoop);
        return findGameLobbyAsDetail(identifier);
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
    public ResponseEntity<DetailGameLobbyDTO> storeGame(@RequestParam String identifier) {

        GameLoop gameLoop = gameLoopManager.findActiveGameLoop(identifier);

        if(gameLoop == null){
            throw new IllegalArgumentException("NO ACTIVE GAME FOUND WITH IDENTIFIER: " + identifier);
        }

        service.storeGame(gameLoop);

        return findGameLobbyAsDetail(identifier);
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
    public ResponseEntity<DetailGameLobbyDTO> pausingGame(@RequestParam String identifier) {

        GameLoop gameLoop = gameLoopManager.findActiveGameLoop(identifier);

        if(gameLoop == null){
            throw new IllegalArgumentException("NO ACTIVE GAME FOUND WITH IDENTIFIER: " + identifier);
        }

        service.pauseGame(gameLoop);
        return findGameLobbyAsDetail(identifier);
    }
}
