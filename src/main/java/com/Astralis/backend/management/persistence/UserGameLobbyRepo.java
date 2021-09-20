package com.Astralis.backend.management.persistence;

import com.Astralis.backend.management.model.UserGameLobby;
import com.Astralis.backend.management.model.User_GameLobby_PK;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;


@CrossOrigin(origins = "http://localhost:4200")
public interface UserGameLobbyRepo extends CrudRepository<UserGameLobby, User_GameLobby_PK> {
    @Override
    Optional<UserGameLobby> findById(User_GameLobby_PK user_gameLobby_pk);

    @Override
    void deleteById(User_GameLobby_PK user_gameLobby_pk);
}
