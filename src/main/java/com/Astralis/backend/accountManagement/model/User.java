package com.Astralis.backend.accountManagement.model;

import com.Astralis.backend.accountManagement.dto.GameStateDTO;
import com.Astralis.backend.accountManagement.dto.UserDTO;
import com.Astralis.backend.accountManagement.dto.UserGameStateDTO;
import com.Astralis.backend.accountManagement.model.AbstractModel;
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
    private List<UserGameState> userGameStates = new ArrayList<>();

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

        (userDTO.getUserGameStates() == null ? new ArrayList<GameStateDTO>() : userDTO.getUserGameStates())
                .stream()
                .map(x -> new UserGameState((UserGameStateDTO) x))
                .forEach(this::addUserGameState);
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
    public void addUserGameState(UserGameState userGameState) {
        if (userGameStates.contains(userGameState)) {
            return;
        }
        userGameStates.add(userGameState);
        userGameState.setUser(this);
    }

    public void removeUserGameState(UserGameState userGameState) {
        if (!userGameStates.contains(userGameState)) {
            return;
        }
        userGameState.setUser(null);
        userGameStates.remove(userGameState);
    }







    //----------------------N:1 Relationship Methods----------------------









    //----------------------N:M Relationship Methods----------------------
}
