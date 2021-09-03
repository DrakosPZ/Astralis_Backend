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
     * TODO: Add Commentary
     *
     * @param action
     */
    public void recieveInput(MessageSpecialized action, LogicGameState logicGameState){
        switch (action.getAction()){
            case MOVE -> actionMoveManager.moveShip(action, logicGameState);
        }
    }


}
