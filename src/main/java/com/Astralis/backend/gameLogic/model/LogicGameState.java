package com.Astralis.backend.gameLogic.model;

import com.Astralis.backend.accountManagement.model.GameState;
import com.Astralis.backend.accountManagement.model.GameStatus;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class LogicGameState extends AbstractMemoryModel implements Serializable {
    private GameState gameState;
    private GameStatus gameStatus;

    //#possibly put into own class ffs
    private int year;
    private int month;
    private int day;
    private int hour;

    private List<Country> countries = new ArrayList<>();
    //private GameMap map;
}
