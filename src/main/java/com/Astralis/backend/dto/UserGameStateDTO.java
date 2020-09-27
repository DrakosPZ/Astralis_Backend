package com.Astralis.backend.dto;

import com.Astralis.backend.model.UserGameState;
import com.Astralis.backend.model.User_GameState_PK;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserGameStateDTO{
    //private User_GameState_PK id;

    private String user;
    private String gameState;
    private String gameRole;

    //O Constructor
    public UserGameStateDTO(UserGameState userGameState){
        //this.id = userGameState.getId();
        this.user = userGameState.getUser() == null ? "": userGameState.getUser().getIdentifier();
        this.gameRole = userGameState.getGameRole().toString();
        this.gameState = userGameState.getGameState()  == null ? "": userGameState.getGameState().getIdentifier();
    }

}
