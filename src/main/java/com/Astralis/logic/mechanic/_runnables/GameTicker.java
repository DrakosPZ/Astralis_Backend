package com.Astralis.logic.mechanic._runnables;

import com.Astralis.logic.helperModels.GameVector;
import com.Astralis.logic.mechanic.MovementManager;
import com.Astralis.logic.model.LogicGameState;
import org.springframework.beans.factory.annotation.Autowired;

public class GameTicker implements Runnable {
    LogicGameState activeState;
    @Autowired
    private MovementManager movementManager;

    // Todo: Add Commentary
    public GameTicker(LogicGameState activeState) {
        this.activeState = activeState;
        this.movementManager = new MovementManager(); //TODO: Implement with Dependency Injection
    }

    // Todo: Add Commentary
    public void run() {
        increaseTime();
        System.out.println("TimeStamp: " + activeState.getDay() + "." + activeState.getMonth() +"." + activeState.getYear() + " : " + activeState.getHour());
    }

    // Todo: Add Commentary, Ship/Troop Movements, Combat
    private void hourTick(){
        System.out.println("Hour Tick");
        activeState.getCountries().forEach(country -> {
            System.out.println("Country: " + country.getName());
            movementManager.moveShip(country.getShip());
        });
    }

    // Todo: Add Commentary
    private void dayTick(){
        System.out.println("Day Tick");

    }

    // Todo: Add Commentary
    private void monthTick(){
        System.out.println("Month Tick");

    }

    // Todo: Add Commentary
    private void yearTick(){
        System.out.println("Year Tick");

    }

    // Todo: Add Commentary and split ticks from time
    private void increaseTime(){
        int hour = activeState.getHour();
        int day = activeState.getDay();
        int month = activeState.getMonth();
        int year = activeState.getYear();
        boolean hourFlag = false;
        boolean dayFlag = false;
        boolean monthFlag = false;
        boolean yearFlag = false;

        hour++;
        hourFlag = true;
        if(hour > 23){
            hour = 0;
            day++;
            dayFlag = true;
        }
        if(day > 30){
            day = 1;
            month++;
            monthFlag = true;
        }
        if(month > 12){
            month = 1;
            year++;
            yearFlag = true;
        }

        activeState.setHour(hour);
        activeState.setDay(day);
        activeState.setMonth(month);
        activeState.setYear(year);

        if(hourFlag){
            hourTick();
        }
        if(dayFlag){
            dayTick();
        }
        if(monthFlag){
            monthTick();
        }
        if(yearFlag){
            yearTick();
        }
    }

}