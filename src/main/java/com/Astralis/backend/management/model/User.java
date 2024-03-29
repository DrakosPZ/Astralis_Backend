package com.Astralis.backend.management.model;

import com.Astralis.backend.management.dto.GameLobbyDTO;
import com.Astralis.backend.management.dto.UserDTO;
import com.Astralis.backend.management.dto.UserGameLobbyDTO;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class User extends AbstractModel {

    private String nickName;
    private UserRole role;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true)
    private LoginInformation loginInformation;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(
            cascade = {CascadeType.PERSIST},
            fetch = FetchType.LAZY,
            mappedBy = "user",
            orphanRemoval = true
    )
    private List<UserGameLobby> userGameLobbies = new ArrayList<>();

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

        (userDTO.getUserGameLobby() == null ? new ArrayList<GameLobbyDTO>() : userDTO.getUserGameLobby())
                .stream()
                .map(x -> new UserGameLobby((UserGameLobbyDTO) x))
                .forEach(this::addUserGameLobby);
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
    public void addUserGameLobby(UserGameLobby userGameLobby) {
        if (userGameLobbies.contains(userGameLobby)) {
            return;
        }
        userGameLobbies.add(userGameLobby);
        userGameLobby.setUser(this);
    }

    public void removeUserGameLobby(UserGameLobby userGameLobby) {
        if (!userGameLobbies.contains(userGameLobby)) {
            return;
        }
        userGameLobby.setUser(null);
        userGameLobbies.remove(userGameLobby);
    }







    //----------------------N:1 Relationship Methods----------------------









    //----------------------N:M Relationship Methods----------------------
}
