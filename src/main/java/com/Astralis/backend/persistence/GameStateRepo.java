package com.Astralis.backend.persistence;

import com.Astralis.backend.model.GameState;
import com.Astralis.backend.model.LoginInformation;
import org.springframework.data.repository.CrudRepository;

public interface GameStateRepo extends AbstractRepo<GameState>, CrudRepository<GameState, Long> {
}
