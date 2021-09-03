package com.Astralis.backend.gameEngine.gameLogic.helperModels;

import com.Astralis.backend.gameEngine.gameLogic.model.Position;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class GameVector {
    private Position vector;
    private double length;
    private Position unitVector;

    /**
     * Constructor to instantiate a GameVector Object
     *
     * @param target Position Object that marks the ending point of the vector
     * @param start Position Object that marks the starting point of the vector
     */
    public GameVector(Position target, Position start){
        setVector(target, start);
    }

    /**
     * Sets all variables of the vector:
     *      sets the Vector Object between start and target Point
     *      calculates the length of the set vector
     *      calculates the unit vector for the set vector
     *
     * @param target Position Object that marks the ending point of the vector
     * @param start Position Object that marks the starting point of the vector
     */
    public void setVector(Position target, Position start){
        vector = new Position(target.getX() - start.getX(), target.getY() - start.getY());
        length = Math.sqrt(Math.pow(vector.getX(),2) + Math.pow(vector.getY(),2));
        unitVector = new Position(vector.getX()/length, vector.getY()/length);
    }

    /**
     * Checks if X-Axis orientation of the (unit) vector is positive
     *
     * @return true if positive, false if negative
     */
    public boolean isPositiveX(){
        if(unitVector.getX() >= 0){
            return true;
        }
        return false;
    }

    /**
     * Checks if Y-Axis orientation of the (unit) vector is positive
     *
     * @return true if positive, false if negative
     */
    public boolean isPositiveY(){
        if(unitVector.getY() >= 0){
            return true;
        }
        return false;
    }
}
