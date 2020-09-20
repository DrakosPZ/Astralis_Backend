package com.Astralis.backend.dto;

import com.Astralis.backend.model.LoginInformation;
import com.Astralis.backend.model.User;
import com.Astralis.backend.model.UserRole;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO extends AbstractModelDto{

    private String nickName;
    private String role;
    private LoginInformationDTO loginInformation;

    //O Constructor
    public UserDTO(User user){
        super(user.getIdentifier());
        this.nickName = user.getNickName() == null ? "": user.getNickName();
        this.role = user.getRole().toString();
        this.loginInformation = user.getLoginInformation() == null ? new LoginInformationDTO(): new LoginInformationDTO(user.getLoginInformation());
    }

}
