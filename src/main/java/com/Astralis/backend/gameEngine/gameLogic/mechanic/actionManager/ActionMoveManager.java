package com.Astralis.backend.gameEngine.gameLogic.mechanic.actionManager;

import com.Astralis.backend.gameEngine.gameLogic.mechanic.utils.FindInGameState;
import com.Astralis.backend.gameEngine.gameLogic.model.LogicGameState;
import com.Astralis.backend.gameEngine.gameLogic.model.Ship;
import com.Astralis.backend.gameEngine.gameLifeCycle.multiplayerStack.model.MessageSpecialized;
import com.Astralis.backend.gameEngine.gameLifeCycle.multiplayerStack.model.specialized.MoveShip;

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
     * Method to handle the player's move ship action input.
     * <ol>
     *     <li>For that it extracts the to be moved Ship from the actions context,</li>
     *     <li>then it looks for the according object in the provided logicGameState</li>
     *     <li>finally sets the new targetPosition on the gameState Ship.</li>
     * </ol>
     *
     * @param messageSpecialized The player's move ship action.
     * @param logicGameState The gameState in which the ship is supposed to be moved
     */
    public void moveShip(MessageSpecialized messageSpecialized, LogicGameState logicGameState){
        MoveShip specialObject = (MoveShip) messageSpecialized.getSpecializedObject();
        Ship ship = FindInGameState.in(logicGameState, specialObject.getShipId());
        ship.setTargetPosition(specialObject.getNewGoal());
        System.out.println(messageSpecialized);
    }
}
