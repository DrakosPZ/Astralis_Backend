package com.Astralis.backend.gameDatabase.controller;

import com.Astralis.backend.accountManagement.DataHolders.GameUserIDSet;
import com.Astralis.backend.accountManagement.dto.GameStateDTO;
import com.Astralis.backend.gameDatabase.GameMasterService;
import com.Astralis.backend.gameLogic.model.mLogicGameState;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@RequestMapping(path = "/gameMasterEndpoint")
public class GameMasterController  extends AbstractController<mLogicGameState> {
    private final GameMasterService service;

    @Override
    List<mLogicGameState> findAllDTO() {
        return null;
    }

    @Override
    Optional<mLogicGameState> findByIdentifierDTO(String identifier) {
        return Optional.empty();
    }

    @Override
    Optional<mLogicGameState> saveDTO(Optional<mLogicGameState> optionaldto) {
        return Optional.empty();
    }

    @Override
    Optional<mLogicGameState> updateDTO(Optional<mLogicGameState> optionaldto) {
        return Optional.empty();
    }

    @Override
    Optional<mLogicGameState> deleteByIdentifierDTO(String identifier) {
        return Optional.empty();
    }









    //----------------------Custom Route Methods----------------------
    @GetMapping(path = "/storedLogicGameState", params = "identifier")
    public ResponseEntity<mLogicGameState> getStoredLogicGameState(@RequestParam String identifier) {
        return findByIdentifier(identifier);
    }

    @PostMapping(path = "/storeChangedLogicGameState", consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<CollectionModel<GameStateDTO>> storeChangedLogicGameState(
            @RequestBody
            GameUserIDSet holder) {
        return ResponseEntity.ok(
                new CollectionModel<>(
                        service.findAllJoinedGames(identifier)
                                .stream()
                                .map(this::addSelfLink)
                                .collect(Collectors.toList())
                )
        );
    }
}
