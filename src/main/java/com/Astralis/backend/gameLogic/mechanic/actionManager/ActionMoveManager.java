package com.Astralis.backend.gameLogic.mechanic.actionManager;

import com.Astralis.backend.gameLogic.mechanic.utils.FindInGameState;
import com.Astralis.backend.gameLogic.model.LogicGameState;
import com.Astralis.backend.gameLogic.model.Ship;
import com.Astralis.backend.multiplayerStack.web.model.MessageSpecialized;
import com.Astralis.backend.multiplayerStack.web.model.specialized.MoveShip;

public class ActionMoveManager {

    private static ActionMoveManager refActionMoveManager;

    /**
     * ActionMoveManager instance getter, to only instantiate ActionMoveManager once.
     *      If ActionMoveManager isn't instantiated already it is automatically instantiated.
     *
     * @return ActionMoveManager instance
     */
    public static ActionMoveManager getActionMoveManager(){
        if(refActionMoveManager == null){
            refActionMoveManager = new ActionMoveManager();
        }
        return refActionMoveManager;
    }

    /**
     * TODO: Add Commentary
     *
     * @param messageSpecialized
     * @param logicGameState
     */
    public void moveShip(MessageSpecialized messageSpecialized, LogicGameState logicGameState){
        MoveShip specialObject = (MoveShip) messageSpecialized.getSpecializedObject();
        Ship ship = FindInGameState.in(logicGameState, specialObject.getShipId());
        ship.setTargetPosition(specialObject.getNewGoal());
        System.out.println(messageSpecialized);
    }
}
