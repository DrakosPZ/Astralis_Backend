package com.Astralis.backend.persistence;

import com.Astralis.backend.model.UserGameState;
import com.Astralis.backend.model.User_GameState_PK;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;


@CrossOrigin(origins = "http://localhost:4200")
public interface UserGameStateRepo extends CrudRepository<UserGameState, User_GameState_PK> {
    @Override
    Optional<UserGameState> findById(User_GameState_PK user_gameState_pk);

    @Override
    void deleteById(User_GameState_PK user_gameState_pk);
}
