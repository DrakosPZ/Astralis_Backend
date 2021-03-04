package com.Astralis.backend.gameLogic.model;

import com.Astralis.backend.accountManagement.dto.UserDTO;
import com.Astralis.backend.accountManagement.dto.UserGameStateDTO;
import com.Astralis.backend.accountManagement.model.GameState;
import com.Astralis.backend.accountManagement.model.UserGameState;
import com.Astralis.backend.gameDatabase.model.Country;
import com.Astralis.backend.gameDatabase.model.LogicGameState;
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
public class mLogicGameState extends AbstractMemoryModel implements Serializable {
    private GameState gameState;

    //#possibly put into own class ffs
    private int year;
    private int month;
    private int day;
    private int hour;

    private List<mCountry> countries = new ArrayList<>();
    //private GameMap map;


    public mLogicGameState(LogicGameState model){
        super();
        //this.id = model.getId() == null ?  -1 : model.getId();
        this.id = model.getId();

        this.gameState = null; //#Todo: Think of either putting this as an id, or DTO
        this.year = model.getYear() < 0 ?  0 : model.getYear();
        this.month = model.getYear() < 0 ?  0 : model.getMonth();
        this.day = model.getYear() < 0 ?  0 : model.getDay();
        this.hour = model.getYear() < 0 ?  0 : model.getHour();

        (model.getCountries() == null ? new ArrayList<mCountry>() : model.getCountries())
                .stream().map(x -> new mCountry((Country) x))
                .collect(Collectors.toList());
    }
}
