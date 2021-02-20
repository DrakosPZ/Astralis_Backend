package com.Astralis.backend.accountManagement.model;

import com.Astralis.backend.accountManagement.dto.UserGameStateDTO;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserGameState{

    @EmbeddedId
    private User_GameState_PK id;

    @ManyToOne(
            cascade = {CascadeType.PERSIST},
            fetch = FetchType.LAZY)
    @MapsId("user_id")
    private User user;

    @ManyToOne(
            cascade = {CascadeType.PERSIST},
            fetch = FetchType.LAZY)
    @MapsId("gameState_id")
    private GameState gameState;

    private GameRole gameRole;

    public UserGameState(User u, GameState gs, GameRole gameRole) {
        // create primary key
        this.id = new User_GameState_PK(u.getId(), gs.getId());
        this.gameRole = gameRole;

        // initialize attributes
        setUser(u);
        setGameState(gs);

    }


    public UserGameState(UserGameStateDTO userGameStateDTO){
        this.user = null;
        this.gameState = null;
        this.gameRole = GameRole.PLAYER;
    }

    /**
     * Method used to clean all related Connections, required before deleting it.
     */
    public void cleanConnection(){
        setUser(null);
        setGameState(null);
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

        if (gameState != null) {
            gameState.addUserGameState(this);
        }
    }








    //----------------------N:M Relationship Methods----------------------
}
