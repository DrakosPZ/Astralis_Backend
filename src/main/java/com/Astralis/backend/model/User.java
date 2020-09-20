package com.Astralis.backend.model;

import com.Astralis.backend.dto.LoginInformationDTO;
import com.Astralis.backend.dto.UserDTO;
import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
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
public class User extends AbstractModel{

    private String nickName;
    private UserRole role;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToOne(cascade = CascadeType.ALL,
                fetch = FetchType.LAZY,
                mappedBy = "user",
                orphanRemoval = true)
    private LoginInformation loginInformation;

    //DTO Constructor
    public User(UserDTO userDTO){
        this.identifier = Optional.ofNullable(userDTO.getIdentifier())
                .orElse(UUID.randomUUID().toString());
        this.nickName = userDTO.getNickName() == null ? "": userDTO.getNickName();
        this.role = UserRole.valueOf(userDTO.getRole());

        this.loginInformation = null;
        if(userDTO.getLoginInformation() != null) {
            setLoginInformation(new LoginInformation(userDTO.getLoginInformation()));
        } else {
            setLoginInformation(new LoginInformation());
        }
    }








    //----------------------1:1 Relationship Methods----------------------
    public void setLoginInformation(LoginInformation loginInformation){
        if(this.loginInformation != null) {
            if (this.loginInformation.equals(loginInformation)) {
                return;
            }
        }
        this.loginInformation = loginInformation;
        loginInformation.setUser(this);
    }







    //----------------------1:N Relationship Methods----------------------








    //----------------------N:1 Relationship Methods----------------------








    //----------------------N:M Relationship Methods----------------------
}
