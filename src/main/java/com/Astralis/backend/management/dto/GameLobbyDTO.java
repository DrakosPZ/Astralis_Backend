package com.Astralis.backend.management.dto;

import com.Astralis.backend.management.model.GameLobby;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameLobbyDTO extends AbstractModelDto {
    private String name;
    private String description;
    private String image;
    private String status;

    private List<UserGameLobbyDTO> userGameLobbies = new ArrayList<>();

    //O Constructor
    public GameLobbyDTO(GameLobby gameLobby){
        super(gameLobby.getIdentifier());
        this.name = gameLobby.getName() == null ? "": gameLobby.getName();
        this.description = gameLobby.getDescription() == null ? "": gameLobby.getDescription();
        this.image = gameLobby.getImage() == null ? "": gameLobby.getImage();
        this.status = gameLobby.getStatus().toString();

        this.userGameLobbies = gameLobby.getUserGameLobbies() == null ? new ArrayList<>() : gameLobby.getUserGameLobbies()
                .stream().map(element -> new UserGameLobbyDTO(element))
                .collect(Collectors.toList());
    }

}
