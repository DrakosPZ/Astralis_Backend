package com.Astralis.backend.management.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class User_GameLobby_PK implements Serializable {

    @Column(name = "USER_ID")
    private Long user_id;

    @Column(name = "GAMELOBBY_ID")
    private Long gameLobby_id;
}
