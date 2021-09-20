package com.Astralis.backend.gameEngine.gameLogic.actions;

import com.Astralis.backend.gameEngine.gameLogic.actions.classes.ActionMoveManager;
import com.Astralis.backend.gameEngine.gameLogic.model.GameState;
import com.Astralis.backend.gameEngine.gameLifeCycle.multiplayerStack.model.MessageSpecialized;

public class ActionSystemManager {

    private static ActionSystemManager refActionSystemManager;

    private ActionMoveManager actionMoveManager;

    /**
     * ActionSystemManager instance getter, to only instantiate ActionSystemManager once.
     *      If ActionSystemManager isn't instantiated already it is automatically instantiated.
     *
     * @return ActionSystemManager instance
     */
    public static ActionSystemManager getActionSystemManager(){
        if(refActionSystemManager == null){
            refActionSystemManager = new ActionSystemManager();
        }
        return refActionSystemManager;
    }

    public ActionSystemManager(){
        actionMoveManager = ActionMoveManager.getActionMoveManager();
    }

    /**
     * Method to forward player's action to the appropriate ActionManager.
     * This is done based on the actions category as followed:
     * <ul>
     *     <li> MOVE -> actionMoveManager </li>
     * </ul>
     *
     * @param action the action of the player.
     * @param gameState the gameState the action happens in.
     */
    public void receiveInput(MessageSpecialized action, GameState gameState){
        switch (action.getAction()){
            case MOVE -> actionMoveManager.moveShip(action, gameState);
        }
    }


}
