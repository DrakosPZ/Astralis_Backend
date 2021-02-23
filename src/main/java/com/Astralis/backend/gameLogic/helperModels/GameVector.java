package com.Astralis.backend.gameLogic.helperModels;

import com.Astralis.backend.gameLogic.model.mPosition;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class GameVector {
    private mPosition vector;
    private double length;
    private mPosition unitVector;

    //TODO: Add documentation
    public GameVector(mPosition target, mPosition start){
        setVector(target, start);
    }

    //TODO: Add documentation
    public void setVector(mPosition target, mPosition start){
        vector = new mPosition(target.getX() - start.getX(), target.getY() - start.getY());
        length = Math.sqrt(Math.pow(vector.getX(),2) + Math.pow(vector.getY(),2));
        unitVector = new mPosition(vector.getX()/length, vector.getY()/length);
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
