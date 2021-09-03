package com.Astralis.backend.management.persistence;

import com.Astralis.backend.management.model.GameState;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GameStateRepo extends AbstractRepo<GameState>, CrudRepository<GameState, Long> {

    List<GameState> findByUserGameStatesUserIdentifier(String identifier);
    List<GameState> findByNameContains(String name);
    List<GameState> findByIdentifierContains(String identifier);
}
