package com.Astralis.backend.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class User_GameState_PK implements Serializable {
    @Column(name = "USER_ID")
    private Long user_id;

    @Column(name = "GAMESTATE_ID")
    private Long gameState_id;
}
