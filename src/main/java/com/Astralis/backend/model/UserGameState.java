package com.Astralis.backend.model;

import com.Astralis.backend.dto.UserGameStateDTO;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Entity
@Builder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserGameState {

    @EmbeddedId
    private User_GameState_PK id;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @MapsId("user_id")
    @JoinColumn(name = "USER_ID")
    private User user;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @MapsId("gameState_id")
    @JoinColumn(name = "GAMESTATE_ID")
    private GameState gameState;

    //add Role later



    public UserGameState(UserGameStateDTO userGameStateDTO){
        //this.id =
        this.user = null;
        this.gameState = null;
    }



    //----------------------1:1 Relationship Methods----------------------







    //----------------------1:N Relationship Methods----------------------








    //----------------------N:1 Relationship Methods----------------------
    public void setUser(User user) {
        if (Objects.equals(this.user, user)) {
            return;
        }

        User oldUser = this.user;
        this.user = user;

        if (oldUser != null) {
            oldUser.removeUserGameState(this);
        }

        if (user != null) {
            user.addUserGameState(this);
        }
    }

    public void setGameState(GameState gameState) {
        if (Objects.equals(this.gameState, gameState)) {
            return;
        }

        GameState oldGameState = this.gameState;
        this.gameState = gameState;

        if (oldGameState != null) {
            oldGameState.removeUserGameState(this);
        }

        if (oldGameState != null) {
            oldGameState.addUserGameState(this);
        }
    }








    //----------------------N:M Relationship Methods----------------------
}
