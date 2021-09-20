package com.Astralis.backend.management.dto;

import com.Astralis.backend.management.model.User;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO extends AbstractModelDto {

    private String nickName;
    private String role;
    private LoginInformationDTO loginInformation;

    private List<UserGameLobbyDTO> userGameLobby = new ArrayList<>();

    //O Constructor
    public UserDTO(User user){
        super(user.getIdentifier());
        this.nickName = user.getNickName() == null ? "": user.getNickName();
        this.role = user.getRole().toString();
        this.loginInformation = user.getLoginInformation() == null ? new LoginInformationDTO(): new LoginInformationDTO(user.getLoginInformation());
        //this.loginInformation = null;

        this.userGameLobby = user.getUserGameLobbies() == null ? new ArrayList<>() : user.getUserGameLobbies()
                .stream().map(element -> new UserGameLobbyDTO(element))
                .collect(Collectors.toList());
    }

}
