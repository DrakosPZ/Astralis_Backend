package com.Astralis.backend.persistence;

import com.Astralis.backend.model.GameState;
import com.Astralis.backend.model.LoginInformation;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface GameStateRepo extends AbstractRepo<GameState>, CrudRepository<GameState, Long> {

    List<GameState> findByUserGameStatesUserIdentifier(String identifier);
    List<GameState> findByNameContains(String name);
    List<GameState> findByIdentifierContains(String identifier);
}
