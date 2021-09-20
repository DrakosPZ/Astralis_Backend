package com.Astralis.backend.gameEngine.gameLogic.mechanics;

import com.Astralis.backend.gameEngine.gameLogic.mechanics.classes.MovementManager;
import com.Astralis.backend.gameEngine.gameLogic.model.LogicGameState;

public class TickManager {

    private static TickManager refTickManager;
    private MovementManager movementManager;

    /**
     * TickManager instance getter, to only instantiate TickManager once.
     *      If TickManager isn't instantiated already it is automatically instantiated.
     *
     * @return TickManager instance
     */
    public static TickManager getTickManager(){
        if(refTickManager == null){
            refTickManager = new TickManager();
        }
        return refTickManager;
    }

    public TickManager(){
        this.movementManager = MovementManager.getMovementManager();
    }

    /**
     * HourTick calls:
     *      ShipMovement
     */
    public void hourTick(LogicGameState activeState){
        System.out.println("Hour Tick");
        movementManager.updateMovement(activeState);
    }

    /**
     * dayTick calls:
     *
     */
    public void dayTick(LogicGameState activeState){
        System.out.println("Day Tick");
    }

    /**
     * MonthTick calls:
     *
     */
    public void monthTick(LogicGameState activeState){
        System.out.println("Month Tick");
    }

    /**
     * YearTick calls:
     *
     */
    public void yearTick(LogicGameState activeState){
        System.out.println("Year Tick");
    }

}
