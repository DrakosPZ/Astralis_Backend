package com.Astralis.backend.model;

import com.Astralis.backend.dto.LoginInformationDTO;
import lombok.*;

import javax.persistence.*;
import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LoginInformation extends AbstractModel{

    private String loginName;
    private String password;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToOne(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    @JoinColumn(name="id")
    private User user;

    //DTO Constructor
    public LoginInformation(LoginInformationDTO loginInformationDTO){
        this.identifier = Optional.ofNullable(loginInformationDTO.getIdentifier())
                                .orElse(UUID.randomUUID().toString());
        this.loginName = loginInformationDTO.getLoginName() == null ? "": loginInformationDTO.getLoginName();
        this.password = loginInformationDTO.getPassword() == null ? "": loginInformationDTO.getPassword();

        this.user = null;
    }







    //----------------------1:1 Relationship Methods----------------------
    public void setUser(User user){
        if(this.user != null) {
            if (this.user.equals(user)) {
                return;
            }
        }
        this.user = user;
        user.setLoginInformation(this);
    }







    //----------------------1:N Relationship Methods----------------------








    //----------------------N:1 Relationship Methods----------------------








    //----------------------N:M Relationship Methods----------------------
}
