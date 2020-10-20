package com.Astralis.logic.mechanic._runnables;

import com.Astralis.logic.model.LogicGameState;

public class GameTicker implements Runnable {
    LogicGameState activeState;

    // Todo: Add Commentary
    public GameTicker(LogicGameState activeState) {
        this.activeState = activeState;
    }

    // Todo: Add Commentary
    public void run() {
        hourTick();
        dayTick();
        monthTick();
        yearTick();
        increaseTime();
        System.out.println("TimeStamp: " + activeState.getDay() + "." + activeState.getMonth() +"." + activeState.getYear() + " : " + activeState.getHour());
    }

    // Todo: Add Commentary
    private void hourTick(){

    }

    // Todo: Add Commentary
    private void dayTick(){

    }

    // Todo: Add Commentary
    private void monthTick(){

    }

    // Todo: Add Commentary
    private void yearTick(){

    }

    // Todo: Add Commentary
    private void increaseTime(){
        int hour = activeState.getHour();
        int day = activeState.getDay();
        int month = activeState.getMonth();
        int year = activeState.getYear();

        hour++;
        if(hour > 23){
            hour = 0;
            day++;
        }
        if(day > 30){
            day = 1;
            month++;
        }
        if(month > 12){
            month = 1;
            year++;
        }

        activeState.setHour(hour);
        activeState.setDay(day);
        activeState.setMonth(month);
        activeState.setYear(year);
    }

}