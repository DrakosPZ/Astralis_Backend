package com.Astralis.backend.gameEngine.gameLifeCycle.multiplayerStack.service;

import com.Astralis.backend.gameEngine.gameLifeCycle.multiplayerStack.model.Action;
import com.Astralis.backend.gameEngine.gameLifeCycle.multiplayerStack.model.Message;
import com.Astralis.backend.gameEngine.gameLifeCycle.multiplayerStack.model.MessageSpecialized;
import com.Astralis.backend.gameEngine.gameLifeCycle.multiplayerStack.model.specialized.GameStateUpdate;
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
        Message gameStateMessage =
                Message.builder()
                        .gameID(gameID)
                        .userID("")
                        .action(Action.GAMEUPDATE)
                        .specializedObject(gson.toJson(new GameStateUpdate(logicGameState)))
                        .build();
        String jsonfiedMessage = gson.toJson(gameStateMessage);
        runningGameController.sendGameUpdate(jsonfiedMessage, gameID);
    }

    /**
     * Method to formulate closing message and sending it out to all connected clients
     *
     * @param gameID The ID of the game that's supposed to be informed.
     */
    public void sendClosingMessage(String gameID){
        Message closingmessage =
                Message.builder()
                        .gameID(gameID)
                        .userID("")
                        .action(Action.CLOSEDGAME)
                        .specializedObject(null)
                    .build();
        String jsonfiedMessage = gson.toJson(closingmessage);
        runningGameController.sendGameUpdate(jsonfiedMessage, gameID);
    }

    //TODO DOCUMENTATION
    public void playerDisconnectedMessage(String gameID, String playerID){
        Message closingmessage =
                Message.builder()
                        .gameID(gameID)
                        .userID(playerID)
                        .action(Action.DISCONNECTED)
                        .specializedObject(null)
                        .build();
        String jsonfiedMessage = gson.toJson(closingmessage);
        runningGameController.sendGameUpdate(jsonfiedMessage, gameID);
    }

}
