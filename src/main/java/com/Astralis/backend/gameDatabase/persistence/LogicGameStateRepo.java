package com.Astralis.backend.gameDatabase.persistence;

import com.Astralis.backend.gameDatabase.model.LogicGameState;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogicGameStateRepo extends AbstractRepo<LogicGameState>, CrudRepository<LogicGameState, Long> {
}
