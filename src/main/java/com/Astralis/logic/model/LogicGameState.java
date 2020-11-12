package com.Astralis.logic.model;

import com.Astralis.backend.model.AbstractModel;
import com.Astralis.backend.model.GameState;
import com.Astralis.backend.model.User;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class LogicGameState extends AbstractModel {
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name="currentState")
    private GameState gameState;

    //#possibly put into own class ffs
    private int year;
    private int month;
    private int day;
    private int hour;

    private List<Country> countries;
    //private GameMap map;







    //----------------------1:1 Relationship Methods----------------------
    public void setGameState(GameState gameState){
        if(this.gameState != null) {
            if (this.gameState.equals(gameState)) {
                return;
            }
        }
        this.gameState = gameState;
        gameState.setCurrentState(this);
    }
}
