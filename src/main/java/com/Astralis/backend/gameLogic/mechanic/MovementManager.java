package com.Astralis.backend.gameLogic.mechanic;

import com.Astralis.backend.gameLogic.helperModels.GameVector;
import com.Astralis.backend.gameLogic.model.mPosition;
import com.Astralis.backend.gameLogic.model.mShip;

public class MovementManager {

    //TODO: Add documentation
    public void moveShip(mShip mShip){
        System.out.println("Move Ship: ");
        mPosition currentPos = mShip.getCurrentMPosition();
        mPosition targetPos = mShip.getTargetMPosition();
        double movementSpeed = mShip.getMovementSpeed();

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
            mShip.setCurrentMPosition(currentPos);

            System.out.println("New Pos: " + currentPos.toString());
        }
    }
}
