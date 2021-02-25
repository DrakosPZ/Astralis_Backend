package com.Astralis.backend.gameDatabase.model;

import com.Astralis.backend.accountManagement.model.GameState;
import com.Astralis.backend.gameLogic.model.mLogicGameState;
import com.Astralis.backend.gameLogic.model.mCountry;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class LogicGameState extends AbstractGameModel {
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name="currentState")
    private GameState gameState;

    //#possibly put into own class ffs
    private int year;
    private int month;
    private int day;
    private int hour;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(
            cascade = {CascadeType.PERSIST},
            fetch = FetchType.LAZY,
            mappedBy = "logicGameState",
            orphanRemoval = true
    )
    private List<Country> countries = new ArrayList<>();
    //private GameMap map;


    public LogicGameState(mLogicGameState memory){
        super(memory.getId());

        this.gameState = null; //#Todo: Think of either putting this as an id, or DTO
        this.year = memory.getYear() < 0 ?  0 : memory.getYear();
        this.month = memory.getMonth() < 0 ?  0 : memory.getMonth();
        this.day = memory.getDay() < 0 ?  0 : memory.getDay();
        this.hour = memory.getHour() < 0 ?  0 : memory.getHour();

        (memory.getCountries() == null ? new ArrayList<Country>() : memory.getCountries())
                .stream()
                .map(x -> new Country((mCountry) x))
                .forEach(this::addCountry);
    }






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






    //----------------------1:N Relationship Methods----------------------
    public void addCountry(Country country) {
        if (countries.contains(country)) {
            return;
        }
        countries.add(country);
        country.setLogicGameState(this);
    }

    public void removeCountry(Country country) {
        if (!countries.contains(country)) {
            return;
        }
        country.setLogicGameState(null);
        countries.remove(country);
    }







    //----------------------N:1 Relationship Methods----------------------









    //----------------------N:M Relationship Methods----------------------
}
