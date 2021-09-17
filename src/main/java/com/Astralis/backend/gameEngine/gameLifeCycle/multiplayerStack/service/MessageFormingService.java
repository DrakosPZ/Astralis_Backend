package com.Astralis.backend.gameEngine.gameLifeCycle.multiplayerStack.service;

import com.Astralis.backend.gameEngine.gameLogic.model.LogicGameState;
import com.Astralis.backend.gameEngine.gameLifeCycle.multiplayerStack.controller.RunningGameController;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageFormingService {

    //If someone later on finds a way to bind this class in a way
    // that it can be manually implemented in the gameLoop class
    // constructor.
    private static  MessageFormingService reference;

    private final Gson gson;
    private final RunningGameController runningGameController;

    @Autowired
    public MessageFormingService(RunningGameController runningGameController,
                                 Gson gson){
        this.runningGameController = runningGameController;
        this.gson = gson;

        this.reference = this;
    }

    public static MessageFormingService getReference(){
        return reference;
    }

    /**
     * Method to transform logicGameState into a String to be further handed to
     * the runningGameController to be further send out to all connected users.
     *
     * @param gameID The ID of the game of which the logicGameState originates and to which clients it is then later send.
     * @param logicGameState The logicGameState which is to be jsonfied.
     */
    public void sendGameState(String gameID, LogicGameState logicGameState){
        String jsonfiedGameState = gson.toJson(logicGameState);
        runningGameController.sendGameStateUpdate(jsonfiedGameState, gameID);
    }

    /**
     * Method to formulate closing message and start closing process of a game Lobby.
     *
     * @param gameID The ID of the game that's supposed to be informed.
     */
    public void sendClosingMessage(String gameID){
        //TODO: Actuall closing event send out
    }

}
