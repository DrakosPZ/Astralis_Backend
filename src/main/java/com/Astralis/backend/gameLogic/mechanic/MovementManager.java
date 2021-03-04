package com.Astralis.backend.gameLogic.mechanic;

import com.Astralis.backend.gameLogic.helperModels.GameVector;
import com.Astralis.backend.gameLogic.model.Position;
import com.Astralis.backend.gameLogic.model.Ship;

public class MovementManager {

    //TODO: Add documentation
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
