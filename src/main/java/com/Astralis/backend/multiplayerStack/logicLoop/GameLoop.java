package com.Astralis.backend.multiplayerStack.logicLoop;


import com.Astralis.backend.accountManagement.model.GameStatus;
import com.Astralis.backend.gameLogic.mechanic._runnables.GameTicker;
import com.Astralis.backend.gameLogic.model.LogicGameState;
import com.Astralis.backend.multiplayerStack.web.model.MessageSpecialized;
import com.Astralis.backend.multiplayerStack.web.service.MessageFormingService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
     * Instantiates a GameLoop with a connected GameStateID, the LogicGamestate used for the Game, and an Emitter to
     *  be forwarded as messenger.
     *
     * @param gameStateID the Id of the GameState connected to the Game
     * @param activeGameState the LogicalGameState used as a basis for the Game
     */
    public void startLoop(String gameStateID, LogicGameState activeGameState){
        activeID = gameStateID;
        activeLoop = new GameTicker(activeGameState, this);

        messageFormingService = MessageFormingService.getRefrence();
        executorService.scheduleAtFixedRate(activeLoop, 0, 1000, TimeUnit.MILLISECONDS);
        forwardStatus(GameStatus.RUNNING);
    }

    /**
     * @return id of the running GameState Model
     */
    public String getID(){
        return activeID;
    }

    /**
     * @return logicGameState of the running loop
     */
    public LogicGameState getLogicGameState(){
        return activeLoop.getActiveState();
    }

    /**
     * Stops the Thread, and emits a game Closed Message
     */
    public void endLoop(){
        executorService.shutdown();
        messageFormingService.sendClosingMessage(activeID);
    }

    /**
     * TODO: ADD COmmentary
     */
    public void updateStatus(){
        messageFormingService.sendGameState(activeID, getLogicGameState());
    }

    /**
     * Forwards the changing of the Game Status to the active Loop
     *
     * @param gameStatus
     */
    public void forwardStatus(GameStatus gameStatus){
        activeLoop.setStatus(gameStatus);
    }

    /**
     * TODO: ADD COmmentary
     */
    public void forwardAction(MessageSpecialized action){

    }
}
