package com.Astralis.backend.gameEngine.gameLogic.mechanics;

import com.Astralis.backend.gameEngine.gameLogic.mechanics.classes.MovementManager;
import com.Astralis.backend.gameEngine.gameLogic.model.GameState;

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
    public void hourTick(GameState activeState){
        System.out.println("Hour Tick");
        movementManager.updateMovement(activeState);
    }

    /**
     * dayTick calls:
     *
     */
    public void dayTick(GameState activeState){
        System.out.println("Day Tick");
    }

    /**
     * MonthTick calls:
     *
     */
    public void monthTick(GameState activeState){
        System.out.println("Month Tick");
    }

    /**
     * YearTick calls:
     *
     */
    public void yearTick(GameState activeState){
        System.out.println("Year Tick");
    }

}
