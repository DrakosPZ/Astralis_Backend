package com.Astralis.backend.accountManagement.model;

import com.Astralis.backend.accountManagement.dto.GameStateDTO;
import com.Astralis.backend.accountManagement.dto.UserDTO;
import com.Astralis.backend.accountManagement.dto.UserGameStateDTO;
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
public class GameState extends AbstractModel {
    private String name;
    private String description;
    private GameStatus status;
    private String image;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(cascade = {CascadeType.PERSIST},
            fetch = FetchType.LAZY,
            mappedBy = "gameState",
            orphanRemoval = true)
    private List<UserGameState> userGameStates = new ArrayList<>();

    //game Logic
    //private RuleSet rules;
    private String gameStorageFolder;

    //DTO Constructor
    public GameState(GameStateDTO gameStateDTO){
        this.identifier = Optional.ofNullable(gameStateDTO.getIdentifier())
                .orElse(UUID.randomUUID().toString());
        this.name = gameStateDTO.getName() == null ? "": gameStateDTO.getName();
        this.description = gameStateDTO.getDescription() == null ? "": gameStateDTO.getDescription();
        this.image = gameStateDTO.getImage() == null ? "": gameStateDTO.getImage();
        this.status = GameStatus.valueOf(gameStateDTO.getStatus());

        (gameStateDTO.getUserGameStates() == null ? new ArrayList<UserDTO>() : gameStateDTO.getUserGameStates())
                .stream()
                .map(x -> new UserGameState((UserGameStateDTO) x))
                .forEach(this::addUserGameState);
    }







    //----------------------1:1 Relationship Methods----------------------






    //----------------------1:N Relationship Methods----------------------
    public void addUserGameState(UserGameState userGameState) {
        if (userGameStates.contains(userGameState)) {
            return;
        }
        userGameStates.add(userGameState);
        userGameState.setGameState(this);
    }

    public void removeUserGameState(UserGameState userGameState) {
        if (!userGameStates.contains(userGameState)) {
            return;
        }
        userGameState.setGameState(null);
        userGameStates.remove(userGameState);
    }







    //----------------------N:1 Relationship Methods----------------------









    //----------------------N:M Relationship Methods----------------------
}
