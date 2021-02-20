package com.Astralis.backend.accountManagement.dto;

import com.Astralis.backend.accountManagement.model.UserGameState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
