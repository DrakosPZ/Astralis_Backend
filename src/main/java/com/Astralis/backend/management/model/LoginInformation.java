package com.Astralis.backend.management.model;

import com.Astralis.backend.management.dto.LoginInformationDTO;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
public class LoginInformation extends AbstractModel {

    @NotNull
    @Column(unique=true)
    private String loginName;
    private String password;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToOne(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    @JoinColumn(name="loginInformation")
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
