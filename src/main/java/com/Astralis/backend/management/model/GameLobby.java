package com.Astralis.backend.management.model;

import com.Astralis.backend.management.dto.GameLobbyDTO;
import com.Astralis.backend.management.dto.UserDTO;
import com.Astralis.backend.management.dto.UserGameLobbyDTO;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class GameLobby extends AbstractModel {
    private String name;
    private String description;
    private GameStatus status;
    private String image;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(cascade = {CascadeType.PERSIST},
            fetch = FetchType.LAZY,
            mappedBy = "gameLobby",
            orphanRemoval = true)
    private List<UserGameLobby> userGameLobbies = new ArrayList<>();

    //game Logic
    //private RuleSet rules;
    private String gameStorageFolder;

    //DTO Constructor
    public GameLobby(GameLobbyDTO gameLobbyDTO){
        this.identifier = Optional.ofNullable(gameLobbyDTO.getIdentifier())
                .orElse(UUID.randomUUID().toString());
        this.name = gameLobbyDTO.getName() == null ? "": gameLobbyDTO.getName();
        this.description = gameLobbyDTO.getDescription() == null ? "": gameLobbyDTO.getDescription();
        this.image = gameLobbyDTO.getImage() == null ? "": gameLobbyDTO.getImage();
        this.status = GameStatus.valueOf(gameLobbyDTO.getStatus());

        (gameLobbyDTO.getUserGameLobbies() == null ? new ArrayList<UserDTO>() : gameLobbyDTO.getUserGameLobbies())
                .stream()
                .map(x -> new UserGameLobby((UserGameLobbyDTO) x))
                .forEach(this::addUserGameLobby);
    }







    //----------------------1:1 Relationship Methods----------------------






    //----------------------1:N Relationship Methods----------------------
    public void addUserGameLobby(UserGameLobby userGameLobby) {
        if (userGameLobbies.contains(userGameLobby)) {
            return;
        }
        userGameLobbies.add(userGameLobby);
        userGameLobby.setGameLobby(this);
    }

    public void removeUserGameLobby(UserGameLobby userGameLobby) {
        if (!userGameLobbies.contains(userGameLobby)) {
            return;
        }
        userGameLobby.setGameLobby(null);
        userGameLobbies.remove(userGameLobby);
    }







    //----------------------N:1 Relationship Methods----------------------









    //----------------------N:M Relationship Methods----------------------
}
