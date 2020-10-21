package com.Astralis.logic.mechanic._runnables;

import com.Astralis.logic.model.LogicGameState;
import com.Astralis.logic.model.Position;

public class GameTicker implements Runnable {
    LogicGameState activeState;

    // Todo: Add Commentary
    public GameTicker(LogicGameState activeState) {
        this.activeState = activeState;
    }

    // Todo: Add Commentary
    public void run() {
        increaseTime();
        System.out.println("TimeStamp: " + activeState.getDay() + "." + activeState.getMonth() +"." + activeState.getYear() + " : " + activeState.getHour());
    }

    // Todo: Add Commentary, Ship/Troop Movements, Combat
    private void hourTick(){
        activeState.getCountries().forEach(country -> {

            Position currentPos = country.getShip().getCurrentPosition();
            Position targetPos = country.getShip().getTargetPosition();
            if(currentPos.getX() != targetPos.getX() || currentPos.getY() != targetPos.getY()){
                //TODO: Put into own Vector class
                Position vector = new Position(targetPos.getX() - currentPos.getX(), targetPos.getY() - currentPos.getY());
                double length = Math.sqrt(Math.pow(vector.getX(),2) + Math.pow(vector.getY(),2));
                Position unitVector = new Position(vector.getX()/length, vector.getY()/length);

                currentPos.setX(currentPos.getX() + unitVector.getX() * country.getShip().getMovementSpeed());
                currentPos.setY(currentPos.getY() + unitVector.getY() * country.getShip().getMovementSpeed());
                country.getShip().setCurrentPosition(currentPos);
            }
        });
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