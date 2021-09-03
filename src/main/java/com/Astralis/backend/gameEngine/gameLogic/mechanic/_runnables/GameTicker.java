package com.Astralis.backend.gameEngine.gameLogic.mechanic._runnables;

import com.Astralis.backend.management.model.GameStatus;
import com.Astralis.backend.gameEngine.gameLogic.mechanic.MovementManager;
import com.Astralis.backend.gameEngine.gameLogic.mechanic.actionManager.ActionEcoSystemManager;
import com.Astralis.backend.gameEngine.gameLogic.model.LogicGameState;
import com.Astralis.backend.gameEngine.gameLifeCycle.logicLoop.GameLoop;
import com.Astralis.backend.gameEngine.gameLifeCycle.multiplayerStack.model.MessageSpecialized;

public class GameTicker implements Runnable {
    private LogicGameState activeState;
    private MovementManager movementManager;
    private ActionEcoSystemManager actionEcoSystemManager;
    private GameLoop loop;

    /**
     * Instantiates a GameTicker Thread with the activeState as the basis for the game, and an Emitter from
     * the instantiater if already joining.
     *
     * @param activeState the LogicState used for the GameTick
     * @param loop the Game Loop the Ticker is stored in, used to forward events to the clients
     */
    public GameTicker(LogicGameState activeState, GameLoop loop) {
        this.activeState = activeState;
        this.loop = loop;
        this.movementManager = MovementManager.getMovementManager();
        this.actionEcoSystemManager = ActionEcoSystemManager.getActionEcoSystemManager();
    }

    /**
     * The threads running method:
     *      If the status flag is set to Running
     *      the time is increased, the new GameState is send out, and a timestamp is send to the console
     */
    public void run() {
        if(activeState.getGameStatus().equals(GameStatus.RUNNING)){
            increaseTime();
            sendOutState();
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
     *
     * sets the Status of the active Game State
     * @param gameStatus to be set Status
     */
    public void setStatus(GameStatus gameStatus){
        activeState.setGameStatus(gameStatus);
    }


    /**
     * calls all Emitters currently active in Game, and sends out the current GameState
     *
     * On Error it Removes all Emitters and sends a CleanUp Messages
     */
    private void sendOutState(){
        loop.updateStatus();
    }

    /**
     * //TODO: Add Commentary
     *
     * @param action
     */
    public void input(MessageSpecialized action){
        actionEcoSystemManager.recieveInput(action, activeState);
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