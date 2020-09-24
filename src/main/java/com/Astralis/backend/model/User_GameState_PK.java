package com.Astralis.backend.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User_GameState_PK implements Serializable {

    @Column(name = "USER_ID")
    private Long user_id;

    @Column(name = "GAMESTATE_ID")
    private Long gameState_id;

    public User_GameState_PK(long user_id, long gameState_id){
        this.user_id = user_id;
        this.gameState_id = gameState_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        User_GameState_PK that = (User_GameState_PK) o;
        return Objects.equals(user_id, that.user_id) &&
                Objects.equals(gameState_id, that.gameState_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user_id, gameState_id);
    }
}
