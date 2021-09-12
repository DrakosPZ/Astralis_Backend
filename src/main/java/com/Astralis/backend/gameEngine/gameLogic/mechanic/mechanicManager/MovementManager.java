package com.Astralis.backend.gameEngine.gameLogic.mechanic.mechanicManager;

import com.Astralis.backend.gameEngine.gameLogic.helperModels.GameVector;
import com.Astralis.backend.gameEngine.gameLogic.model.Position;
import com.Astralis.backend.gameEngine.gameLogic.model.Ship;

public class MovementManager {

    private static MovementManager refMovementManager;

    /**
     * MovementManager instance getter, to only instantiate MovementManager once.
     *      If MovementManager isn't instantiated already it is automatically instantiated.
     *
     * @return MovementManager instance
     */
    public static MovementManager getMovementManager(){
        if(refMovementManager == null){
            refMovementManager = new MovementManager();
        }
        return refMovementManager;
    }

    /**
     * Checks if ship's target and current position are the same,
     * if they aren't load target and current Pos into Game Vector Object,
     * and move ship by the following formula:
     *      currentPos + Unit Vector * movementSpeed
     * Afterwards check if target Pos. has been overshot, by
     *      checking if orientation (x/y) is positive or negative,
     *      checking if current pos is smaller (negative) or larger (positive) than target.
     * Lastly set currentPost to actual Ship pos.
     *
     * @param Ship the to be moved Ship
     */
    public void moveShip(Ship Ship){
        System.out.println("Move Ship: ");
        Position currentPos = Ship.getCurrentPosition();
        Position targetPos = Ship.getTargetPosition();
        double movementSpeed = Ship.getMovementSpeed();

        if(currentPos.getX() != targetPos.getX() || currentPos.getY() != targetPos.getY()){
            System.out.println("Old Pos: " + currentPos.toString());
            System.out.println("Target Pos: " + targetPos.toString());

            GameVector vector = new GameVector(targetPos, currentPos);

            currentPos.setX(currentPos.getX() + vector.getUnitVector().getX() * movementSpeed);
            currentPos.setY(currentPos.getY() + vector.getUnitVector().getY() * movementSpeed);

            // positiveX Movement
            if(vector.isPositiveX()){
                if(currentPos.getX() > targetPos.getX()){
                    currentPos.setX(targetPos.getX());
                }
            }else {
                if(currentPos.getX() < targetPos.getX()){
                    currentPos.setX(targetPos.getX());
                }
            }
            // positiveY Movement
            if(vector.isPositiveY()){
                if(currentPos.getY() > targetPos.getY()){
                    currentPos.setY(targetPos.getY());
                }
            }else {
                if(currentPos.getY() < targetPos.getY()){
                    currentPos.setY(targetPos.getY());
                }
            }
            Ship.setCurrentPosition(currentPos);

            System.out.println("New Pos: " + currentPos.toString());
        }
    }
}
