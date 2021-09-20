package com.Astralis.backend.management.dto;

import com.Astralis.backend.management.model.UserGameLobby;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserGameLobbyDTO {
    //private User_GameState_PK id;

    private String user;
    private String gameLobby;
    private String gameRole;

    //O Constructor
    public UserGameLobbyDTO(UserGameLobby userGameLobby){
        //this.id = userGameState.getId();
        this.user = userGameLobby.getUser() == null ? "": userGameLobby.getUser().getIdentifier();
        this.gameRole = userGameLobby.getGameRole().toString();
        this.gameLobby = userGameLobby.getGameLobby()  == null ? "": userGameLobby.getGameLobby().getIdentifier();
    }

}
