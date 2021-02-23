package com.Astralis.backend.gameLogic.mechanic._runnables;

import com.Astralis.backend.gameLogic.mechanic.MovementManager;
import com.Astralis.backend.gameLogic.model.mLogicGameState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;

public class GameTicker implements Runnable {
    private mLogicGameState activeState;
    @Autowired
    private MovementManager movementManager;
    private List<SseEmitter> emitters = new ArrayList<>();

    // Todo: Add Commentary
    public GameTicker(mLogicGameState activeState, SseEmitter emitter) {
        this.activeState = activeState;
        addEmitter(emitter);
        this.movementManager = new MovementManager(); //TODO: Implement with Dependency Injection
    }

    // Todo: Add Commentary
    public void run() {
        increaseTime();
        sendOutEvents();
        System.out.println("TimeStamp: " + activeState.getDay() + "." + activeState.getMonth() +"." + activeState.getYear() + " : " + activeState.getHour());
    }

    // Todo: Add Commentary
    public mLogicGameState getActiveState(){
        return activeState;
    }

    // Todo: Add Commentary
    public void addEmitter(SseEmitter emitter){
        emitters.add(emitter);
    }

    // Todo: Add Commentary
    public void removeEmitter(SseEmitter emitter){
        emitters.remove(emitter);
    }

    // Todo: Add Commentary
    public void cleanUpEmitters(String message){
        for (int index = 0; index < emitters.size(); index++) {
            SseEmitter emitter = emitters.get(index);
            try {
                emitter.send(message);
            } catch (Exception ex) {
                throw new IllegalArgumentException("Game Ticker: Error when cleanup Message");
            }
            emitter.complete();
            removeEmitter(emitter);
            index--;
        }
        System.out.println("Size after Cleanup: "+emitters.size());
    }

    // Todo: Add Commentary
    private void sendOutEvents(){
        try {
            for (SseEmitter emitter: emitters) {
                    emitter.send(activeState);
            }
        } catch (Exception ex) {
            cleanUpEmitters("Error occurred, Closing Sessions");
            throw new IllegalArgumentException("GameTickError");
        }
    }

    // Todo: Add Commentary, Ship/Troop Movements, Combat
    private void hourTick(){
        System.out.println("Hour Tick");
        activeState.getCountries().forEach(country -> {
            System.out.println("Country: " + country.getName());
            movementManager.moveShip(country.getMShip());
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