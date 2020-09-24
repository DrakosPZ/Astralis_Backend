package com.Astralis.backend.dto;

import com.Astralis.backend.model.UserGameState;
import com.Astralis.backend.model.User_GameState_PK;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserGameStateDTO{
    private User_GameState_PK id;

    private String identifier;
    private String user;
    private String gameState;

    //O Constructor
    public UserGameStateDTO(UserGameState userGameState){
        this.id = userGameState.getId();
        this.user = userGameState.getUser() == null ? "": userGameState.getUser().getIdentifier();
        this.gameState = userGameState.getGameState()  == null ? "": userGameState.getGameState().getIdentifier();
    }

}
