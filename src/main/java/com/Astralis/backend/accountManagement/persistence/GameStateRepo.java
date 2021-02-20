package com.Astralis.backend.accountManagement.persistence;

import com.Astralis.backend.accountManagement.model.GameState;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GameStateRepo extends AbstractRepo<GameState>, CrudRepository<GameState, Long> {

    List<GameState> findByUserGameStatesUserIdentifier(String identifier);
    List<GameState> findByNameContains(String name);
    List<GameState> findByIdentifierContains(String identifier);
}
