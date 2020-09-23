package com.Astralis.backend.model;

import com.Astralis.backend.dto.GameStateDTO;
import com.Astralis.backend.dto.UserDTO;
import com.Astralis.backend.dto.UserGameStateDTO;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.*;

@Getter
@Setter
@Entity
@Builder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GameState extends AbstractModel{
    private String name;
    private String description;
    private String image;

    @OneToMany(mappedBy = "user")
    private List<UserGameState> userGameStates = new ArrayList<>();

    //DTO Constructor
    public GameState(GameStateDTO gameStateDTO){
        this.identifier = Optional.ofNullable(gameStateDTO.getIdentifier())
                .orElse(UUID.randomUUID().toString());
        this.name = gameStateDTO.getName() == null ? "": gameStateDTO.getName();
        this.description = gameStateDTO.getDescription() == null ? "": gameStateDTO.getDescription();
        this.image = gameStateDTO.getImage() == null ? "": gameStateDTO.getImage();

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
