package com.Astralis.backend.gameEngine.gameLogic.model;

import com.Astralis.backend.management.model.GameLobby;
import com.Astralis.backend.management.model.GameStatus;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class LogicGameState extends AbstractMemoryModel implements Serializable {
    private GameLobby gameLobby;
    private GameStatus gameStatus;

    //#possibly put into own class ffs
    private int year;
    private int month;
    private int day;
    private int hour;

    private List<Country> countries = new ArrayList<>();
    //private GameMap map;
}
