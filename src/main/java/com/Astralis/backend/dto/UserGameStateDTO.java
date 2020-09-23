package com.Astralis.backend.dto;

import com.Astralis.backend.model.GameState;
import com.Astralis.backend.model.User;
import com.Astralis.backend.model.UserGameState;
import com.Astralis.backend.model.User_GameState_PK;
import lombok.*;

import javax.persistence.EmbeddedId;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserGameStateDTO{
    private User_GameState_PK id;

    private String user;
    private String gameState;

    //O Constructor
    public UserGameStateDTO(UserGameState userGameState){
        //this.id =
        this.user = userGameState.getUser() == null ? "": userGameState.getUser().getIdentifier();
        this.gameState = userGameState.getGameState()  == null ? "": userGameState.getGameState().getIdentifier();
    }

}
