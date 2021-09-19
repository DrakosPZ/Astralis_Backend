package com.Astralis.backend.gameEngine.gameLifeCycle.logicLoop;


import com.Astralis.backend.management.model.GameStatus;
import com.Astralis.backend.gameEngine.gameLogic.mechanic._runnables.GameTicker;
import com.Astralis.backend.gameEngine.gameLogic.model.LogicGameState;
import com.Astralis.backend.gameEngine.gameLifeCycle.multiplayerStack.model.MessageSpecialized;
import com.Astralis.backend.gameEngine.gameLifeCycle.multiplayerStack.service.MessageFormingService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Configurable;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@NoArgsConstructor
@AllArgsConstructor
@Configurable(preConstruction = true)
public class GameLoop {

    ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);
    GameTicker activeLoop;
    String activeID;

    private MessageFormingService messageFormingService;

    /**
     * Instantiates a GameLoop with a connected GameStateID, the LogicGamestate used for the Game.
     *
     * @param gameStateID the Id of the GameState connected to the Game.
     * @param activeGameState the LogicalGameState used as a basis for the Game.
     */
    public void startLoop(String gameStateID, LogicGameState activeGameState){
        activeID = gameStateID;
        activeLoop = new GameTicker(activeGameState, this);

        messageFormingService = MessageFormingService.getReference();
        executorService.scheduleAtFixedRate(activeLoop, 0, 1000, TimeUnit.MILLISECONDS);
        forwardStatus(GameStatus.RUNNING);
    }

    /**
     * @return id of the running GameState Model.
     */
    public String getID(){
        return activeID;
    }

    /**
     * @return logicGameState of the running loop.
     */
    public LogicGameState getLogicGameState(){
        return activeLoop.getActiveState();
    }

    /**
     * Stops the Thread, and emits a game Closed Message.
     */
    public void endLoop(){
        executorService.shutdown();
        messageFormingService.sendClosingMessage(activeID);
    }

    //TODO: Add Documentation
    public void disconnectClienSession(String userID){
        messageFormingService.playerDisconnectedMessage(activeID, userID);
    }

    /**
     * Forwards the changing of the Game Status to the active Loop
     *
     * @param gameStatus The Status that's supposed to be set on the gameState.
     */
    public void forwardStatus(GameStatus gameStatus){
        activeLoop.setStatus(gameStatus);
    }

    /**
     * Method to send out the current gameState to all connected Players.
     * (Sends out each GameTick)
     */
    public void updateStatus(){
        messageFormingService.sendGameState(activeID, getLogicGameState());
    }

    /**
     * Method to forward a player's in-Game Action to the running Loop.
     *
     * @param action The transmitted Message containing the Action Information.
     */
    public void forwardAction(MessageSpecialized action){
        activeLoop.input(action);
    }
}
