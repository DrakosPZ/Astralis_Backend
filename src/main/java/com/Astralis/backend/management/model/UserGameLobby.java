package com.Astralis.backend.management.model;

import com.Astralis.backend.management.dto.UserGameLobbyDTO;
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
public class UserGameLobby {

    @EmbeddedId
    private User_GameLobby_PK id;

    @ManyToOne(
            cascade = {CascadeType.PERSIST},
            fetch = FetchType.LAZY)
    @MapsId("user_id")
    private User user;

    @ManyToOne(
            cascade = {CascadeType.PERSIST},
            fetch = FetchType.LAZY)
    @MapsId("gameLobby_id")
    private GameLobby gameLobby;

    private GameRole gameRole;

    public UserGameLobby(User u, GameLobby gl, GameRole gameRole) {
        // create primary key
        this.id = new User_GameLobby_PK(u.getId(), gl.getId());
        this.gameRole = gameRole;

        // initialize attributes
        setUser(u);
        setGameLobby(gl);

    }


    public UserGameLobby(UserGameLobbyDTO userGameLobbyDTO){
        this.user = null;
        this.gameLobby = null;
        this.gameRole = GameRole.PLAYER;
    }

    /**
     * Method used to clean all related Connections, required before deleting it.
     */
    public void cleanConnection(){
        setUser(null);
        setGameLobby(null);
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
            oldUser.removeUserGameLobby(this);
        }

        if (user != null) {
            user.addUserGameLobby(this);
        }
    }

    public void setGameLobby(GameLobby gameLobby) {
        if (Objects.equals(this.gameLobby, gameLobby)) {
            return;
        }

        GameLobby oldGameLobby = this.gameLobby;
        this.gameLobby = gameLobby;

        if (oldGameLobby != null) {
            oldGameLobby.removeUserGameLobby(this);
        }

        if (gameLobby != null) {
            gameLobby.addUserGameLobby(this);
        }
    }








    //----------------------N:M Relationship Methods----------------------
}
