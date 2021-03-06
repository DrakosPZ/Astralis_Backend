package com.Astralis.backend.accountManagement.dto;

import com.Astralis.backend.accountManagement.model.GameState;
import com.Astralis.backend.gameLogic.model.LogicGameState;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameStateDTO extends AbstractModelDto {
    private String name;
    private String description;
    private String image;
    private String status;

    private List<UserGameStateDTO> userGameStates = new ArrayList<>();

    //O Constructor
    public GameStateDTO(GameState gameState){
        super(gameState.getIdentifier());
        this.name = gameState.getName() == null ? "": gameState.getName();
        this.description = gameState.getDescription() == null ? "": gameState.getDescription();
        this.image = gameState.getImage() == null ? "": gameState.getImage();
        this.status = gameState.getStatus().toString();

        this.userGameStates = gameState.getUserGameStates() == null ? new ArrayList<>() : gameState.getUserGameStates()
                .stream().map(element -> new UserGameStateDTO(element))
                .collect(Collectors.toList());
    }

}
