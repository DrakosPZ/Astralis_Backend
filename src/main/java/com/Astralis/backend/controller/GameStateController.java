package com.Astralis.backend.controller;

import com.Astralis.backend.dto.GameStateDTO;
import com.Astralis.backend.service.GameStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
public class GameStateController extends AbstractController<GameStateDTO>{
    private final GameStateService service;

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
     * Post route to add a User to an existing Game
     *
     * @param holder holds the identifier of the gamestate object, and the user object to be added
     * @return
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
     * Delete route to remove a User from a Game
     *
     * @param holder holds the identifier of the gamestate object, and the user object to be added
     * @return
     */
    @DeleteMapping(path = "/removeUser", consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<GameStateDTO> removeFromTeam(
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
    //Add to Game (With roles if joining player is Master_Admin, set game  Role to Master-Admin, if admin set to Admin)
    //Remove from Game - if no players left, delete game
    //create new Game (with user to be set Master Role)

    //Edit Delete Behaviour to emtpy list
    //Route to change Role of a User to a wanted one (Cannot change MasterAdmin Role)
}
