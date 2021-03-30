package com.Astralis.backend.gameLogic.mechanic._runnables;

import com.Astralis.backend.accountManagement.model.GameStatus;
import com.Astralis.backend.gameLogic.mechanic.MovementManager;
import com.Astralis.backend.gameLogic.model.LogicGameState;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;

public class GameTicker implements Runnable {
    private LogicGameState activeState;
    private MovementManager movementManager;
    private List<SseEmitter> emitters = new ArrayList<>();

    /**
     * Instantiates a GameTicker Thread with the activeState as the basis for the game, and an Emitter from
     * the instantiater if already joining.
     *
     * @param activeState the LogicState used for the GameTick
     * @param emitter instantiater receiver if they want to join right after instantiating
     */
    public GameTicker(LogicGameState activeState, SseEmitter emitter) {
        this.activeState = activeState;
        if(emitter != null){
            //if it's null, the instance is just started without someone joining in yet
            addEmitter(emitter);
        }
        this.movementManager = MovementManager.getMovementManager();
    }

    /**
     * The threads running method:
     *      If the status flag is set to Running
     *      the time is increased, the new GameState is send out, and a timestamp is send to the console
     */
    public void run() {
        if(activeState.getGameStatus().equals(GameStatus.RUNNING)){
            increaseTime();
            sendOutEvents();
            System.out.println("TimeStamp: " + activeState.getDay() + "." + activeState.getMonth() +"." + activeState.getYear() + " : " + activeState.getHour());
        }
    }


    //Getter/Setter and Managment Methods
    /**
     * Gets the current LogicsGameState
     *
     * @return the used LogicGameState
     */
    public LogicGameState getActiveState(){
        return activeState;
    }

    /**
     * Set GameStatus flag to STORING
     */
    public void stopGame(){
        activeState.setGameStatus(GameStatus.STORING);
    }

    /**
     * Set GameStatus flag to RUNNING
     */
    public void continueGame(){
        activeState.setGameStatus(GameStatus.RUNNING);
    }

    /**
     *
     * sets the Status of the active Game State
     * @param gameStatus to be set Status
     */
    public void setStatus(GameStatus gameStatus){
        activeState.setGameStatus(gameStatus);
    }



    //Emitter Handlers
    /**
     * add Emitter to receiver List
     *
     * @param emitter to be added SSEEmitter
     */
    public void addEmitter(SseEmitter emitter){
        emitters.add(emitter);
    }

    /**
     * remove Emitter fro receiver List
     *
     * @param emitter to be removed SSEEmitter
     */
    public void removeEmitter(SseEmitter emitter){
        emitters.remove(emitter);
    }

    /**
     * Removes all Emitters from the Game and sends out the given messages
     *
     * @param message the to be send out message
     */
    public void cleanUpEmitters(String message){
        for (int index = 0; index < emitters.size(); index++) {
            SseEmitter emitter = emitters.get(index);
            try {
                emitter.send(message);
            } catch (Exception ex) {
                throw new IllegalArgumentException("Game Ticker: Error when cleanup Message - " + ex.fillInStackTrace());
            }
            emitter.complete();
            removeEmitter(emitter);
            index--;
        }
        System.out.println("Size after Cleanup: "+emitters.size());
    }

    /**
     * calls all Emitters currently active in Game, and sends out the current GameState
     *
     * On Error it Removes all Emitters and sends a CleanUp Messages
     */
    private void sendOutEvents(){
        try {
            System.out.println("--------------EMITTERS PRESENT: " + emitters.size());
            for (SseEmitter emitter: emitters) {
                    emitter.send(activeState);
            }
        } catch (Exception ex) {
            cleanUpEmitters("Error occurred, Closing Sessions");
            throw new IllegalArgumentException("GameTickError");
        }
    }


    //Game Logic Methods

    /**
     * HourTick calls:
     *      ShipMovement
     */
    private void hourTick(){
        System.out.println("Hour Tick");
        activeState.getCountries().forEach(country -> {
            System.out.println("Country: " + country.getName());
            movementManager.moveShip(country.getShip());
        });
    }

    /**
     * dayTick calls:
     *
     */
    private void dayTick(){
        System.out.println("Day Tick");

    }

    /**
     * MonthTick calls:
     *
     */
    private void monthTick(){
        System.out.println("Month Tick");

    }

    /**
     * YearTick calls:
     *
     */
    private void yearTick(){
        System.out.println("Year Tick");

    }

    /**
     * increases hour by one and raises hourFlag,
     *      if the hour counter reaches 24, it is reset to 0 and dayFlag is raised,
     *      if the day counter reaches 31, it is reset to 1 and monthFlag is raised,
     *      if the month counter reaches 13, it is reset to 1 and yearFlag is raised
     * time values are set and for every flag raised the according gameTick Method is called.
     */
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