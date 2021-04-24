package com.Astralis.backend.multiplayerStack.web.service;

import com.Astralis.backend.gameLogic.model.LogicGameState;
import com.Astralis.backend.multiplayerStack.logicLoop.GameLoopManager;
import com.Astralis.backend.multiplayerStack.web.controller.RunningGameController;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

public class MessageFormingService {
    //TODO: Add Cry For Help Commentary
    private static  MessageFormingService refrence;

    private final Gson gson;
    private final RunningGameController runningGameController;

    @Autowired
    public MessageFormingService(RunningGameController runningGameController,
                                 Gson gson){
        this.runningGameController = runningGameController;
        this.gson = gson;

        this.refrence = this;
    }

    public static MessageFormingService getRefrence(){
        return refrence;
    }

    public void sendGameState(String gameID, LogicGameState logicGameState){
        //TODO: Actually jsonfy the logic Game State
        String jsonfiedGameState = "";
        runningGameController.sendGameStateUpdate(jsonfiedGameState, gameID);
    }

    public void sendClosingMessage(String gameID){
        //TODO: Actuall closing event send out
    }

}
