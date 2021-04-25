package com.Astralis.backend.gameLogic.mechanic.actionManager;

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
}
