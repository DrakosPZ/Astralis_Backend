package com.Astralis.backend.gameLogic.helperModels;

import com.Astralis.backend.gameLogic.model.Position;
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

    //TODO: Add documentation
    public GameVector(Position target, Position start){
        setVector(target, start);
    }

    //TODO: Add documentation
    public void setVector(Position target, Position start){
        vector = new Position(target.getX() - start.getX(), target.getY() - start.getY());
        length = Math.sqrt(Math.pow(vector.getX(),2) + Math.pow(vector.getY(),2));
        unitVector = new Position(vector.getX()/length, vector.getY()/length);
    }

    //TODO: Add documentation
    public boolean isPositiveX(){
        if(unitVector.getX() >= 0){
            return true;
        }
        return false;
    }

    //TODO: Add documentation
    public boolean isPositiveY(){
        if(unitVector.getY() >= 0){
            return true;
        }
        return false;
    }
}
