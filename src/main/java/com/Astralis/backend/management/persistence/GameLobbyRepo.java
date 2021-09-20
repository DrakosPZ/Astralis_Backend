package com.Astralis.backend.management.persistence;

import com.Astralis.backend.management.model.GameLobby;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GameLobbyRepo extends AbstractRepo<GameLobby>, CrudRepository<GameLobby, Long> {

    //List<GameLobby> findByUserGameStatesUserIdentifier(String identifier);
    List<GameLobby> findByUserGameLobbiesUserIdentifier(String identifier);
    List<GameLobby> findByNameContains(String name);
    List<GameLobby> findByIdentifierContains(String identifier);
}
