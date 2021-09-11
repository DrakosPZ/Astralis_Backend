package com.Astralis.backend.gameEngine.gameLogic.mechanic.actionManager;

import com.Astralis.backend.gameEngine.gameLogic.model.LogicGameState;
import com.Astralis.backend.gameEngine.gameLifeCycle.multiplayerStack.model.MessageSpecialized;

public class ActionEcoSystemManager {

    private static ActionEcoSystemManager refActionEcoSystemManager;

    private ActionMoveManager actionMoveManager;

    /**
     * ActionEcoSystemManager instance getter, to only instantiate ActionEcoSystemManager once.
     *      If ActionEcoSystemManager isn't instantiated already it is automatically instantiated.
     *
     * @return ActionEcoSystemManager instance
     */
    public static ActionEcoSystemManager getActionEcoSystemManager(){
        if(refActionEcoSystemManager == null){
            refActionEcoSystemManager = new ActionEcoSystemManager();
        }
        return refActionEcoSystemManager;
    }

    public ActionEcoSystemManager(){
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
     * @param logicGameState the logicGameState the action happens in.
     */
    public void receiveInput(MessageSpecialized action, LogicGameState logicGameState){
        switch (action.getAction()){
            case MOVE -> actionMoveManager.moveShip(action, logicGameState);
        }
    }


}
