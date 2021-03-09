package com.Astralis.backend.accountManagement.service;

import com.Astralis.backend.accountManagement.model.*;
import com.Astralis.backend.accountManagement.persistence.GameStateRepo;
import com.Astralis.backend.accountManagement.persistence.UserGameStateRepo;
import com.Astralis.backend.accountManagement.persistence.UserRepo;
import com.Astralis.backend.accountManagement.dto.CustomeDetailDTOs.DetailGameStateDTO;
import com.Astralis.backend.accountManagement.dto.CustomeDetailDTOs.DetailUserGameStateDTO;
import com.Astralis.backend.accountManagement.dto.GameStateDTO;
import com.Astralis.backend.accountManagement.dto.UserDTO;
import com.Astralis.backend.gameLogic.mechanic.GameLoop;
import com.Astralis.backend.gameLogic.mechanic.GameLoopManager;
import com.Astralis.backend.gameLogic.model.LogicGameState;
import com.Astralis.backend.gameStateStoring.GameMasterService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class GameStateService
        extends AbstractService<GameStateDTO, GameState> {

    private final GameStateRepo gameStateRepo;
    private final UserRepo userRepo;
    private final UserGameStateRepo userGameStateRepo;
    private final GameMasterService gameMasterService;
    private final GameLoopManager gameLoopManager;

    /**
     * Convert a given Model into a respective DTO
     *
     * @param model to be transformed
     * @return transformed DTO
     */
    @Override
    GameStateDTO convertModelIntoDTO(GameState model) {
        return new GameStateDTO(model);
    }

    /**
     * Convert a given DTO into a respective Model
     *
     * @param dto to be transformed
     * @return transformed model
     */
    @Override
    GameState convertDTOIntoModel(GameStateDTO dto) {
        GameState model = new GameState(dto);
        return model;
    }

    /**
     * Step 1. update simple fields
     * Compares the old to the new User model.
     * If a field has changed, the old model's field is updated to the new version.
     * The old model is then returned with the updated fields.
     *
     * @param old GameState model
     * @param model new GameState model
     * @return the old GameState model with the updated fields.
     */
    @Override
    GameState compareUpdate(GameState old, GameState model) {
        if(!old.getName().equals(model.getName())){
            old.setName(model.getName());
        }
        if(!old.getDescription().equals(model.getDescription())){
            old.setDescription(model.getDescription());
        }
        if(!old.getImage().equals(model.getImage())){
            old.setImage(model.getImage());
        }
        return old;
    }

    /**
     * Step 2. update relationship fields
     * Transforms the DTO into a new Model.
     * Compares every single relationship list of the old and new model.
     * If an element of the old list is not contained by the new list,
     *      the remove relationship method is called,
     *      as well as it's also removed from the new list.
     * If the element is present in the new list, it is removed from the new list.
     *
     * Finally all remaining elements of the new list are added according relationship method.
     *
     * As every got model at this point is a hibernate instance the changes are edited on the database.
     * So to commit the changes the model is called from the database.
     * The returned model is handed forward.
     *
     * @param old GameState model.
     * @param dto new GameState dto.
     * @return the updated model from the database.
     */
    @Override
    GameState storeListChanges(GameState old, GameStateDTO dto) {
        //for those that are fully loaded by the converter
        GameState model = convertDTOIntoModel(dto);

        // not included users --- added by remove and add route

        return findByIdentifier(model.getIdentifier()).get();
    }

    /**
     * Sets for every created GameState ...
     *             Not yet Used
     *
     * @param model the to be instantiated model.
     * @return the fully set-up model.
     */
    @Override
    GameState setStandardData(GameState model) {
        //set Standard Data

        return model;
    }

    /**
     * Calls the find all method of the personRepo.
     *
     * @return a List of all stored Persons in the table.
     */
    @Override
    List<GameState> findall() {
        return gameStateRepo.findAll();
    }

    /**
     * Calls the save method of the personRepo.
     * Also encodes the Password, as the newly stored Person Model doesn't do it automatically.
     *
     * @param model the to be stored model person.
     * @return the stored model.
     */
    @Override
    GameState saveRep(GameState model) {
        //clean Relations --
        return gameStateRepo.save(model);
    }

    /**
     * For Updates only the fields are stored, relations are handled independently,
     * as such relation-fields are cleaned and the save method is called.
     *
     * @param model to be updated model person.
     * @return the updated model.
     */
    @Override
    GameState updateRep(GameState model) {
        //clean Relations

        return gameStateRepo.save(model);
    }

    /**
     * Calls the find by Identifier method of the respective repo
     *
     * @param identifier the looked for model's identifier
     * @return the looked for model.
     */
    @Override
    Optional<GameState> findByIdentifier(String identifier) {
        return gameStateRepo.findByIdentifier(identifier);
    }

    /**
     * Calls the delete by identifier method of the respective repo
     *
     * @param identifier the to be deleted model's identifier
     */
    @Override
    void deleteByIdentifier(String identifier) {
        gameStateRepo.deleteByIdentifier(identifier);
    }

    /**
     * no changes made before deletion, yet!
     * @param identifier
     */
    @Override
    void preDeleteCleanUp(String identifier) {
    }

    //-----------------------------Custom Methods------------------------------

    /**
     * Retrieves the connection of the given GameState and User combination,
     * and changes the role of the user in that connection to the given role.
     *
     * @param identifierGS GameState Identifier
     * @param identifierU User Identifier
     * @param role To be changed to role
     * @return the adjusted GameState
     */
    public Optional<GameStateDTO> changeRoleOfGamePlayer(String identifierGS, String identifierU, String role) {
        GameState gameState = gameStateRepo.findByIdentifier(identifierGS)
                .orElseThrow(() -> new IllegalArgumentException("GameState ID has no according GameState."));
        User user = userRepo.findByIdentifier(identifierU)
                .orElseThrow(() -> new IllegalArgumentException("User ID has no according User."));
        GameRole gameRole = GameRole.valueOf(role);

        //Get Connection
        User_GameState_PK connectionID =
                new User_GameState_PK(user.getId(), gameState.getId());
        UserGameState connection = userGameStateRepo.findById(connectionID)
                .orElseThrow(() -> new IllegalArgumentException("Connection ID has no according UserGameState Connection."));

        checkForGameRoleChange(connection, gameRole);

        //#! possibly change to UserGameState with it's own service further down the line
        return Optional.of(gameState)
                .map(m -> convertModelIntoDTO(m));
    }

    /**
     * Calls the find all Joined Games of the personRepo.
     *
     * @param identifier of the given user.
     * @return a List of all Games that the given User has joined.
     */
    public List<GameStateDTO> findAllJoinedGames(String identifier) {
        return gameStateRepo.findByUserGameStatesUserIdentifier(identifier)
                .stream()
                .map(item -> convertModelIntoDTO(item))
                .collect(Collectors.toList());
    }

    /**
     * Calls the find all Games containing the given name of the personRepo.
     *
     * @param name of the searched GameStates.
     * @return a List of all Games that contain the given Name
     */
    public List<GameStateDTO> findAllGamesContainName(String name) {
        return gameStateRepo.findByNameContains(name)
                .stream()
                .map(item -> convertModelIntoDTO(item))
                .collect(Collectors.toList());
    }

    /**
     * Calls the find all Games containing the given identifier of the personRepo.
     *
     * @param identifier of the given Game's identifier.
     * @return a List of all Games that contain the given identifier
     */
    public List<GameStateDTO> findAllGamesContainIdentifier(String identifier) {
        return gameStateRepo.findByIdentifierContains(identifier)
                .stream()
                .map(item -> convertModelIntoDTO(item))
                .collect(Collectors.toList());
    }

    /**
     * Calls the standard find by Identifier, and then maps it to a DetailGameStateDTO.
     * Continues to resolve all relationship through custome Detail RelationshipDTOs.
     *
     * @param identifier of the searched gameState
     * @return the optional of detail GameStateDTO
     */
    public Optional<DetailGameStateDTO> findGameStateAsDetail(String identifier){
        GameState found = gameStateRepo.findByIdentifier(identifier).get();

        DetailGameStateDTO detailGameStateDTO = new DetailGameStateDTO();
        detailGameStateDTO.setIdentifier(found.getIdentifier());
        detailGameStateDTO.setName(found.getName());
        detailGameStateDTO.setDescription(found.getDescription());
        detailGameStateDTO.setImage(found.getImage());
        detailGameStateDTO.setStatus(found.getStatus().toString());
        List<DetailUserGameStateDTO> connectedList = new ArrayList<>();
        found.getUserGameStates().forEach(connection -> {
            connectedList.add(
                    new DetailUserGameStateDTO(
                            new UserDTO(connection.getUser()),
                            connection.getGameRole().toString(),
                            detailGameStateDTO.getIdentifier()
                    )
            );
        });
        detailGameStateDTO.setUserGameStates(connectedList);

        return Optional.of(detailGameStateDTO);
    }

    /**
     * Change Status from Paused to Running, if not already initialized it first initialize Logic Game State
     *
     * @param identifier
     */
    public void startGame(String identifier){
        GameState gameState = findByIdentifier(identifier)
                .orElseThrow(() -> new IllegalArgumentException("No GameState Present"));
        LogicGameState logicGameState;
        //#TODO: Maybe implement this check rather over the status flag, as link may beremoved later on
        if(gameState.getGameStorageFolder() == null){
            gameMasterService.initializeGameState(gameState);
        }

        logicGameState = gameMasterService.loadGameStateFromDatabase(gameState.getGameStorageFolder())
                .orElseThrow(() -> new IllegalArgumentException("No GameState found."));

        gameLoopManager.addGameLoop(identifier, logicGameState, null);
    }

    //#TODO: Add Documentary
    //STOPS THE GAME
    public void stopGame(GameLoop gameLoop){
        GameState gameState = findByIdentifier(gameLoop.getID())
                .orElseThrow(() -> new IllegalArgumentException("No Connected GameStateFound"));

        gameLoopManager.removeGameLoop(gameLoop);
        gameMasterService.storeGameStateToDatabase(gameLoop.getLogicGameState(), gameState.getName());
    }

    //#TODO: Add Documentary
    //SAVING
    public void storeGame(GameLoop gameLoop){
        GameState gameState = findByIdentifier(gameLoop.getID())
                .orElseThrow(() -> new IllegalArgumentException("No Connected GameStateFound"));

        gameMasterService.storeGameStateToDatabase(gameLoop.getLogicGameState(), gameState.getName());
    }

    //#TODO: Add Documentary
    //Sets State to STORING for saving process
    public void lockGameState(GameLoop gameLoop){
        gameLoopManager.lockGameLoop(gameLoop);
        GameState gameState = findByIdentifier(gameLoop.getID())
                .orElseThrow(() -> new IllegalArgumentException("No GameState with ID Found"));
        gameState.setStatus(GameStatus.STORING);
    }

    //#TODO: Add Documentary
    //Sets State to RUNNING after finishing saving Process
    public void openGameState(GameLoop gameLoop){
        GameState gameState = findByIdentifier(gameLoop.getID())
                .orElseThrow(() -> new IllegalArgumentException("No GameState with ID Found"));
        if(gameState.getStatus().equals(GameStatus.STORING)){
            gameLoopManager.openGameLoop(gameLoop);
            gameState.setStatus(GameStatus.RUNNING);
        }
    }






    //----------------------N:M Connection Change Checks-----------------------
    /**
     * Checks if there are no players left in a game, if there are none, the game is deleted,
     * signalled by the returned DTO identifier being empty.
     *
     * @param gameState the to be checked GameState
     * @return the DTO of the checked GameState
     */
    private GameStateDTO checkForLastPlayerDeleted(GameState gameState){
        GameStateDTO returnedGameState =new GameStateDTO(gameState);

        if(gameState.getUserGameStates().size() <= 0){
            deleteByIdentifier(gameState.getIdentifier());
            returnedGameState.setIdentifier("");
        }

        return returnedGameState;
    }

    /**
     * Checks the given Gamestate if the user who joins a game is a Master Admin or the first person to join.
     * The first person is declared Game_Admin.
     * A Master_Admin is declared Master_Admin.
     * If none of the two applies the User will get the Player role
     *
     * @param gameState The to be added Gamestate.
     * @param user The newly added user.
     * @return the picked GameRole.
     */
    private GameRole checkForGameRole(GameState gameState, User user){
        if(user.getRole() == UserRole.MASTER_ADMIN){
            return GameRole.MASTER_ADMIN;
        } else if(gameState.getUserGameStates().size() <= 0){
            return GameRole.GAME_ADMIN;
        } else {
            return GameRole.PLAYER;
        }
    }

    /**
     * Checks the to be changed to gameRole if it is a Master Admin or Game Admin.
     * These roles can not be changed to with the normal change, therefore they just break.
     * Then checks the connection if the user is supposed to be changed from a Game Admin or Master Admin.
     * Same applies therefore breaks.
     * If none of the above is the case the method goses through with changing the role.
     *
     * @param connection to be edited connection.
     * @param gameRole to be changed to GameRole.
     */
    private void checkForGameRoleChange(UserGameState connection, GameRole gameRole){
        switch (gameRole){
            case MASTER_ADMIN:
                return;
            case GAME_ADMIN:
                return;
        }
        switch (connection.getGameRole()){
            case MASTER_ADMIN:
                return;
            case GAME_ADMIN:
                return;
        }
        connection.setGameRole(gameRole);
    }









    //----------------------Relationship Handling Methods----------------------
    public Optional<GameStateDTO> addUserToGameState(String identifierGS, String identifierU) {
        GameState gameState = gameStateRepo.findByIdentifier(identifierGS)
                .orElseThrow(() -> new IllegalArgumentException("GameState ID has no according GameState."));
        User user = userRepo.findByIdentifier(identifierU)
                .orElseThrow(() -> new IllegalArgumentException("User ID has no according User."));
        GameRole setGameRole = checkForGameRole(gameState, user);

        //Custom N:M Connection Part
        UserGameState connection = new UserGameState(user, gameState, setGameRole);

        return Optional.of(gameState)
                .map(m -> convertModelIntoDTO(m));
    }

    public Optional<GameStateDTO> removeUserFromGameState(String identifierGS, String identifierU) {
        GameState gameState = gameStateRepo.findByIdentifier(identifierGS)
                .orElseThrow(() -> new IllegalArgumentException("GameState ID has no according GameState."));
        User user = userRepo.findByIdentifier(identifierU)
                .orElseThrow(() -> new IllegalArgumentException("User ID has no according User."));

        //Custom N:M Connection Part
        User_GameState_PK connectionID =
                new User_GameState_PK(user.getId(), gameState.getId());
        UserGameState connection = userGameStateRepo.findById(connectionID)
                .orElseThrow(() -> new IllegalArgumentException("Connection ID has no according UserGameState Connection."));
        connection.cleanConnection();
        userGameStateRepo.deleteById(connectionID);

        //Extra behaviour
        GameStateDTO returnedGameState = checkForLastPlayerDeleted(gameState);

        return Optional.of(returnedGameState);
    }
}
