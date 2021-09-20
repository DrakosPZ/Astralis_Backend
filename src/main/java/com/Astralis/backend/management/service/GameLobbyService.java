package com.Astralis.backend.management.service;

import com.Astralis.backend.management.model.*;
import com.Astralis.backend.management.persistence.GameLobbyRepo;
import com.Astralis.backend.management.persistence.UserGameLobbyRepo;
import com.Astralis.backend.management.persistence.UserRepo;
import com.Astralis.backend.management.dto.CustomeDetailDTOs.DetailGameLobbyDTO;
import com.Astralis.backend.management.dto.CustomeDetailDTOs.DetailUserLobbyDTO;
import com.Astralis.backend.management.dto.GameLobbyDTO;
import com.Astralis.backend.management.dto.UserDTO;
import com.Astralis.backend.gameEngine.gameLifeCycle.logicLoop.GameLoop;
import com.Astralis.backend.gameEngine.gameLifeCycle.logicLoop.GameLoopManager;
import com.Astralis.backend.gameEngine.gameLogic.model.GameState;
import com.Astralis.backend.gameEngine.gameStateManagement.GameMasterService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class GameLobbyService
        extends AbstractService<GameLobbyDTO, GameLobby> {

    private final GameLobbyRepo gameLobbyRepo;
    private final UserRepo userRepo;
    private final UserGameLobbyRepo userGameLobbyRepo;
    private final GameMasterService gameMasterService;
    private final GameLoopManager gameLoopManager;

    /**
     * Convert a given Model into a respective DTO.
     *
     * @param model to be transformed.
     * @return transformed DTO.
     */
    @Override
    GameLobbyDTO convertModelIntoDTO(GameLobby model) {
        return new GameLobbyDTO(model);
    }

    /**
     * Convert a given DTO into a respective Model.
     *
     * @param dto to be transformed.
     * @return transformed model.
     */
    @Override
    GameLobby convertDTOIntoModel(GameLobbyDTO dto) {
        GameLobby model = new GameLobby(dto);
        return model;
    }

    /**
     * Step 1. update simple fields:
     * Compares the old to the new User model.
     * If a field has changed, the old model's field is updated to the new version.
     * The old model is then returned with the updated fields.
     *
     * @param old GameLobby model.
     * @param model new GameLobby model.
     * @return the old GameLobby model with the updated fields.
     */
    @Override
    GameLobby compareUpdate(GameLobby old, GameLobby model) {
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
     * Step 2. update relationship fields:
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
     * @param old GameLobby model.
     * @param dto new GameLobby dto.
     * @return the updated model from the database.
     */
    @Override
    GameLobby storeListChanges(GameLobby old, GameLobbyDTO dto) {
        //for those that are fully loaded by the converter
        GameLobby model = convertDTOIntoModel(dto);

        // not included users --- added by remove and add route

        return findByIdentifier(model.getIdentifier()).get();
    }

    /**
     * Sets for every created GameLobby ...
     *             Not yet Used
     *
     * @param model the to be instantiated model.
     * @return the fully set-up model.
     */
    @Override
    GameLobby setStandardData(GameLobby model) {
        //set Standard Data

        return model;
    }

    /**
     * Calls the find all method of the personRepo.
     *
     * @return a List of all stored Persons in the table.
     */
    @Override
    List<GameLobby> findall() {
        return gameLobbyRepo.findAll();
    }

    /**
     * Calls the save method of the personRepo.
     * Also encodes the Password, as the newly stored Person Model doesn't do it automatically.
     *
     * @param model the to be stored model person.
     * @return the stored model.
     */
    @Override
    GameLobby saveRep(GameLobby model) {
        //clean Relations --
        return gameLobbyRepo.save(model);
    }

    /**
     * For Updates only the fields are stored, relations are handled independently,
     * as such relation-fields are cleaned and the save method is called.
     *
     * @param model to be updated model person.
     * @return the updated model.
     */
    @Override
    GameLobby updateRep(GameLobby model) {
        //clean Relations

        return gameLobbyRepo.save(model);
    }

    /**
     * Calls the find by Identifier method of the respective repo.
     *
     * @param identifier the looked for model's identifier.
     * @return the looked for model.
     */
    @Override
    Optional<GameLobby> findByIdentifier(String identifier) {
        return gameLobbyRepo.findByIdentifier(identifier);
    }

    /**
     * Calls the delete by identifier method of the respective repo.
     *
     * @param identifier the to be deleted model's identifier.
     */
    @Override
    void deleteByIdentifier(String identifier) {
        gameLobbyRepo.deleteByIdentifier(identifier);
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
     * Retrieves the connection of the given GameLobby and User combination,
     * and changes the role of the user in that connection to the given role.
     *
     * @param identifierGS GameLobby Identifier.
     * @param identifierU User Identifier.
     * @param role To be changed to role.
     * @return the adjusted GameLobby.
     */
    public Optional<GameLobbyDTO> changeRoleOfGamePlayer(String identifierGS, String identifierU, String role) {
        GameLobby gameLobby = gameLobbyRepo.findByIdentifier(identifierGS)
                .orElseThrow(() -> new IllegalArgumentException("GameLobby ID has no according GameLobby."));
        User user = userRepo.findByIdentifier(identifierU)
                .orElseThrow(() -> new IllegalArgumentException("User ID has no according User."));
        GameRole gameRole = GameRole.valueOf(role);

        //Get Connection
        User_GameLobby_PK connectionID =
                new User_GameLobby_PK(user.getId(), gameLobby.getId());
        UserGameLobby connection = userGameLobbyRepo.findById(connectionID)
                .orElseThrow(() -> new IllegalArgumentException("Connection ID has no according UserGameLobby Connection."));

        checkForGameRoleChange(connection, gameRole);

        //#! possibly change to UserGameLobby with it's own service further down the line
        return Optional.of(gameLobby)
                .map(m -> convertModelIntoDTO(m));
    }

    //TODO: this could throw an exception
    /**
     * Calls the find all Joined Games of the personRepo.
     *
     * @param identifier of the given user.
     * @return a List of all Games that the given User has joined.
     */
    public List<GameLobbyDTO> findAllJoinedGames(String identifier) {
        return gameLobbyRepo.findByUserGameLobbiesUserIdentifier(identifier)
                .stream()
                .map(item -> convertModelIntoDTO(item))
                .collect(Collectors.toList());
    }

    /**
     * Calls the find all Games containing the given name of the personRepo.
     *
     * @param name of the searched GameLobbies.
     * @return a List of all Games that contain the given Name.
     */
    public List<GameLobbyDTO> findAllGamesContainName(String name) {
        return gameLobbyRepo.findByNameContains(name)
                .stream()
                .map(item -> convertModelIntoDTO(item))
                .collect(Collectors.toList());
    }

    /**
     * Calls the find all Games containing the given identifier of the personRepo.
     *
     * @param identifier of the given Game's identifier.
     * @return a List of all Games that contain the given identifier.
     */
    public List<GameLobbyDTO> findAllGamesContainIdentifier(String identifier) {
        return gameLobbyRepo.findByIdentifierContains(identifier)
                .stream()
                .map(item -> convertModelIntoDTO(item))
                .collect(Collectors.toList());
    }

    /**
     * Calls the standard find by Identifier, and then maps it to a DetailGameLobbyDTO.
     * Continues to resolve all relationship through custom Detail RelationshipDTOs.
     *
     * @param identifier of the searched GameLobby.
     * @return the optional of detail GameLobbyDTO.
     */
    public Optional<DetailGameLobbyDTO> findGameLobbyAsDetail(String identifier){
        GameLobby found = gameLobbyRepo.findByIdentifier(identifier).get();

        DetailGameLobbyDTO detailGameLobbyDTO = new DetailGameLobbyDTO();
        detailGameLobbyDTO.setIdentifier(found.getIdentifier());
        detailGameLobbyDTO.setName(found.getName());
        detailGameLobbyDTO.setDescription(found.getDescription());
        detailGameLobbyDTO.setImage(found.getImage());
        detailGameLobbyDTO.setStatus(found.getStatus().toString());
        List<DetailUserLobbyDTO> connectedList = new ArrayList<>();
        found.getUserGameLobbies().forEach(connection -> {
            connectedList.add(
                    new DetailUserLobbyDTO(
                            new UserDTO(connection.getUser()),
                            connection.getGameRole().toString(),
                            detailGameLobbyDTO.getIdentifier()
                    )
            );
        });
        detailGameLobbyDTO.setUserGameLobbies(connectedList);

        return Optional.of(detailGameLobbyDTO);
    }

    /**
     * Change Status from Paused to Running, if not already initialized it first initialize Logic Game State.
     * Also adds the gameLoop to the gameLoopManager and initializes it.
     *
     * @param identifier the Identifier of the to be started GameLobby.
     */
    public void startGame(String identifier){
        GameLobby gameLobby = findByIdentifier(identifier)
                .orElseThrow(() -> new IllegalArgumentException("No GameLobby Present"));
        GameState gameState;
        //#TODO: Maybe implement this check rather over the status flag, as link may be removed later on
        if(gameLobby.getStatus() == GameStatus.UNINITIALIZED){
            gameMasterService.setUpNewGameState(gameLobby);
        }

        gameState = gameMasterService.loadGameStateFromDatabase(gameLobby.getGameStorageFolder())
                .orElseThrow(() -> new IllegalArgumentException("No GameState found."));

        gameLoopManager.addGameLoop(identifier, gameState);

        GameLoop gameLoop = gameLoopManager.findActiveGameLoop(identifier);
        manageStatus(gameLoop, GameStatus.RUNNING);
    }

    /**
     * Stops the given GameLoop. Stopping makes the gameLoop uninteractable for the players.
     * First the gameLoop's status is changed to closed, stopping the gameTicker.
     * <ol>
     * <li> The gameLoop's status is changed to closed, stopping the gameTicker.
     * <li> The gameLoop is removed from the gameLoopManager.
     * <li> The gameState is stored.
     * </ol>
     *
     * @param gameLoop The gameLoop which is supposed to be stored.
     */
    public void stopGame(GameLoop gameLoop){
        GameLobby gameLobby = findByIdentifier(gameLoop.getID())
                .orElseThrow(() -> new IllegalArgumentException("No Connected GameLobbyFound"));

        manageStatus(gameLoop, GameStatus.CLOSED);
        gameLoopManager.removeGameLoop(gameLoop);
        gameMasterService.storeGameStateToDatabase(gameLoop.getGameState(), gameLobby.getName());
    }

    /**
     * Method to store the given GameLoop.
     * Game is locked during storing process and unlocked once done.
     *
     * @param gameLoop The Gameloop supposed to be stored.
     */
    public void storeGame(GameLoop gameLoop){
        GameLobby gameLobby = findByIdentifier(gameLoop.getID())
                .orElseThrow(() -> new IllegalArgumentException("No Connected GameLobby Found"));
        GameStatus previoues = gameLobby.getStatus();

        manageStatus(gameLoop, GameStatus.STORING);
        gameMasterService.storeGameStateToDatabase(gameLoop.getGameState(), gameLobby.getName());
        manageStatus(gameLoop, previoues);
    }

    /**
     * Method to stop the given gameLoop.
     * If it is already stopped it is changed to running.
     *
     * @param gameLoop The gameLoop supposed to be paused/unpaused.
     */
    public void pauseGame(GameLoop gameLoop){
        GameLobby gameLobby = findByIdentifier(gameLoop.getID())
                .orElseThrow(() -> new IllegalArgumentException("No Connected GameLobby Found"));

        if(gameLobby.getStatus() == GameStatus.PAUSED){
            manageStatus(gameLoop, GameStatus.RUNNING);
        } else {
            manageStatus(gameLoop, GameStatus.PAUSED);
        }
    }

    /**
     * Method to change the given gameLoop's status to Storing.
     *
     * @param gameLoop The gameLoop whose status is supposed to be changed.
     */
    public void lockGameState(GameLoop gameLoop){
        manageStatus(gameLoop, GameStatus.STORING);
    }

    /**
     * Method to change the given gameLoop's status to Running.
     *
     * @param gameLoop The gameLoop whose status is supposed to be changed.
     */
    public void openGameState(GameLoop gameLoop){
        manageStatus(gameLoop, GameStatus.RUNNING);
    }

    /**
     * Method to change Status of the gameLoop object and the status of the according GameLobby Object at the sametime.
     * The GameLobby reference is got by searching for it with the related ID in the gameLoop.
     *
     * @param gameLoop The gameLoop whose status is supposed to be changed.
     * @param gameStatus The status that it is supposed to be changed to.
     */
    private void manageStatus(GameLoop gameLoop, GameStatus gameStatus){
        GameLobby gameLobby = findByIdentifier(gameLoop.getID())
                .orElseThrow(() -> new IllegalArgumentException("No Connected GameLobby Found"));
        gameLobby.setStatus(gameStatus);
        gameLoop.forwardStatus(gameStatus);
    }






    //----------------------N:M Connection Change Checks-----------------------
    /**
     * Checks if there are no players left in a game, if there are none, the game is deleted,
     * signalled by the returned DTO identifier being empty.
     *
     * @param gameLobby the to be checked GameLobby
     * @return the DTO of the checked GameLobby
     */
    private GameLobbyDTO checkForLastPlayerDeleted(GameLobby gameLobby){
        GameLobbyDTO returnedGameLobby =new GameLobbyDTO(gameLobby);

        if(gameLobby.getUserGameLobbies().size() <= 0){
            GameLoop gameLoop = null;
            try{
                gameLoop = gameLoopManager.findActiveGameLoop(gameLobby.getIdentifier());
            } catch (Exception e){
                System.out.println("Game isn't running.");
            }
            //if not already stopped, stop running gameloop
            if(gameLoop != null){
                stopGame(gameLoopManager.findActiveGameLoop(gameLobby.getIdentifier()));
            }
            deleteByIdentifier(gameLobby.getIdentifier());
            returnedGameLobby.setIdentifier("");
        }

        return returnedGameLobby;
    }

    /**
     * Checks the given GameLobby if the user who joins a game is a Master Admin or the first person to join.
     * The first person is declared Game_Admin.
     * A Master_Admin is declared Master_Admin.
     * If none of the two applies the User will get the Player role
     *
     * @param gameLobby The to be added GameLobby.
     * @param user The newly added user.
     * @return the picked GameRole.
     */
    private GameRole checkForGameRole(GameLobby gameLobby, User user){
        if(user.getRole() == UserRole.MASTER_ADMIN){
            return GameRole.MASTER_ADMIN;
        } else if(gameLobby.getUserGameLobbies().size() <= 0){
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
    private void checkForGameRoleChange(UserGameLobby connection, GameRole gameRole){
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
    public Optional<GameLobbyDTO> addUserToGameLobby(String identifierGS, String identifierU) {
        GameLobby gameLobby = gameLobbyRepo.findByIdentifier(identifierGS)
                .orElseThrow(() -> new IllegalArgumentException("GameLobby ID has no according GameLobby."));
        User user = userRepo.findByIdentifier(identifierU)
                .orElseThrow(() -> new IllegalArgumentException("User ID has no according User."));
        GameRole setGameRole = checkForGameRole(gameLobby, user);

        //Custom N:M Connection Part
        UserGameLobby connection = new UserGameLobby(user, gameLobby, setGameRole);

        return Optional.of(gameLobby)
                .map(m -> convertModelIntoDTO(m));
    }

    public Optional<GameLobbyDTO> removeUserFromGameLobby(String identifierGS, String identifierU) {
        GameLobby gameLobby = gameLobbyRepo.findByIdentifier(identifierGS)
                .orElseThrow(() -> new IllegalArgumentException("GameLobby ID has no according GameLobby."));
        User user = userRepo.findByIdentifier(identifierU)
                .orElseThrow(() -> new IllegalArgumentException("User ID has no according User."));
        GameLoop gameLoop = null;
        try{
            gameLoop = gameLoopManager.findActiveGameLoop(gameLobby.getIdentifier());
        }catch (Exception e){
            System.out.println("Game currently not running.");
        }

        //Custom N:M Connection Part
        User_GameLobby_PK connectionID =
                new User_GameLobby_PK(user.getId(), gameLobby.getId());
        UserGameLobby connection = userGameLobbyRepo.findById(connectionID)
                .orElseThrow(() -> new IllegalArgumentException("Connection ID has no according UserGameLobby Connection."));
        connection.cleanConnection();
        userGameLobbyRepo.deleteById(connectionID);

        //Extra behaviour
        if(gameLoop != null){
            gameLoopManager.disconnectPlayerFrom(gameLoop, identifierU);
        }
        GameLobbyDTO returnedGameLobby = checkForLastPlayerDeleted(gameLobby);

        return Optional.of(returnedGameLobby);
    }
}
