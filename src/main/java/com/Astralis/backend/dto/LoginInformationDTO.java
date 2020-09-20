package com.Astralis.backend.dto;

import com.Astralis.backend.model.LoginInformation;
import com.Astralis.backend.model.User;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginInformationDTO extends AbstractModelDto{

    private String loginName;
    private String password;
    //child Side
    private String user;

    //O Constructor
    public LoginInformationDTO(LoginInformation loginInformation){
        super(loginInformation.getIdentifier());
        this.loginName = loginInformation.getLoginName() == null ? "": loginInformation.getLoginName();
        this.password = loginInformation.getPassword() == null ? "": loginInformation.getPassword();
        this.user = loginInformation.getUser() == null ? "": loginInformation.getUser().getIdentifier();
    }

}
