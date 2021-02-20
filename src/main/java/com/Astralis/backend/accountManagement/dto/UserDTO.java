package com.Astralis.backend.accountManagement.dto;

import com.Astralis.backend.accountManagement.model.User;
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

    private List<UserGameStateDTO> userGameStates = new ArrayList<>();

    //O Constructor
    public UserDTO(User user){
        super(user.getIdentifier());
        this.nickName = user.getNickName() == null ? "": user.getNickName();
        this.role = user.getRole().toString();
        this.loginInformation = user.getLoginInformation() == null ? new LoginInformationDTO(): new LoginInformationDTO(user.getLoginInformation());
        //this.loginInformation = null;

        this.userGameStates = user.getUserGameStates() == null ? new ArrayList<>() : user.getUserGameStates()
                .stream().map(element -> new UserGameStateDTO(element))
                .collect(Collectors.toList());
    }

}
