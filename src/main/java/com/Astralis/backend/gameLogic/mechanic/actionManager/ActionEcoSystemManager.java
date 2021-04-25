package com.Astralis.backend.gameLogic.mechanic.actionManager;

import com.Astralis.backend.gameLogic.mechanic.MovementManager;

public class ActionEcoSystemManager {

    private static ActionEcoSystemManager refActionEcoSystemManager;

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


}
