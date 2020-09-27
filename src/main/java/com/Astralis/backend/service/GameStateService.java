package com.Astralis.backend.service;

import com.Astralis.backend.dto.GameStateDTO;
import com.Astralis.backend.model.GameState;
import com.Astralis.backend.model.User;
import com.Astralis.backend.model.UserGameState;
import com.Astralis.backend.model.User_GameState_PK;
import com.Astralis.backend.persistence.GameStateRepo;
import com.Astralis.backend.persistence.UserGameStateRepo;
import com.Astralis.backend.persistence.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class GameStateService
        extends AbstractService<GameStateDTO, GameState> {

    private final GameStateRepo gameStateRepo;
    private final UserRepo userRepo;
    private final UserGameStateRepo userGameStateRepo;

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









    //----------------------Relationship Handling Methods----------------------
    public Optional<GameStateDTO> addUserToGameState(String identifierGS, String identifierU) {
        GameState gameState = gameStateRepo.findByIdentifier(identifierGS)
                .orElseThrow(() -> new IllegalArgumentException("GameState ID has no according GameState."));
        User user = userRepo.findByIdentifier(identifierU)
                .orElseThrow(() -> new IllegalArgumentException("User ID has no according User."));

        //Custom N:M Connection Part
        UserGameState connection = new UserGameState(user, gameState);
        //gameState.addUser(user);

        return Optional.of(gameState)
                .map(m -> convertModelIntoDTO(m));
    }

    public Optional<GameStateDTO> removeUserFromGameState(String identifierGS, String identifierU) {
        GameState gameState = gameStateRepo.findByIdentifier(identifierGS)
                .orElseThrow(() -> new IllegalArgumentException("GameState ID has no according GameState."));
        User user = userRepo.findByIdentifier(identifierU)
                .orElseThrow(() -> new IllegalArgumentException("User ID has no according User."));

        //Custom N:M Connection Part
        UserGameState connection =
                new UserGameState(user, gameState);

        gameState.removeUserGameState(connection);
        return Optional
                .of(gameState)
                .map(this::convertModelIntoDTO);
    }
}
