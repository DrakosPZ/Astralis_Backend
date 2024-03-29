package com.Astralis.backend.gameEngine.gameLogic._runnables;

import com.Astralis.backend.gameEngine.gameLogic.mechanics.TickManager;
import com.Astralis.backend.management.model.GameStatus;
import com.Astralis.backend.gameEngine.gameLogic.actions.ActionSystemManager;
import com.Astralis.backend.gameEngine.gameLogic.model.GameState;
import com.Astralis.backend.gameEngine.gameLifeCycle.logicLoop.GameLoop;
import com.Astralis.backend.gameEngine.gameLifeCycle.multiplayerStack.model.MessageSpecialized;

public class GameTicker implements Runnable {
    private GameState activeState;
    private TickManager tickManager;
    private ActionSystemManager actionSystemManager;
    private GameLoop loop;

    /**
     * Instantiates a GameTicker Thread with the activeState as the basis for the game, and an Emitter from
     * the instantiater if already joining.
     *
     * @param activeState the LogicState used for the GameTick
     * @param loop the Game Loop the Ticker is stored in, used to forward events to the clients
     */
    public GameTicker(GameState activeState, GameLoop loop) {
        this.activeState = activeState;
        this.loop = loop;
        this.tickManager = TickManager.getTickManager();
        this.actionSystemManager = ActionSystemManager.getActionSystemManager();
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
     * @return the used gameState
     */
    public GameState getActiveState(){
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
     * Method to forward the player's action to the actionEcoSystemManager.
     *
     * @param action The player's action stored in a Message Object.
     */
    public void input(MessageSpecialized action){
        actionSystemManager.receiveInput(action, activeState);
    }


    //Game Logic Methods

    /**
     * increases hour by one and raises hourFlag,
     * <ul>
     *     <li>if the hour counter reaches 24, it is reset to 0 and dayFlag is raised,</li>
     *     <li>if the day counter reaches 31, it is reset to 1 and monthFlag is raised,</li>
     *     <li>if the month counter reaches 13, it is reset to 1 and yearFlag is raised</li>
     * </ul>
     *
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
            tickManager.hourTick(activeState);
        }
        if(dayFlag){
            tickManager.dayTick(activeState);
        }
        if(monthFlag){
            tickManager.monthTick(activeState);
        }
        if(yearFlag){
            tickManager.yearTick(activeState);
        }
    }

}