package com.Astralis.backend.gameLogic.mechanic;


import com.Astralis.backend.gameLogic.mechanic._runnables.GameTicker;
import com.Astralis.backend.gameLogic.model.LogicGameState;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@NoArgsConstructor
@AllArgsConstructor
public class GameLoop {

    ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);
    GameTicker activeLoop;
    String activeID;

    /**
     * Instantiates a GameLoop with a connected GameStateID, the LogicGamestate used for the Game, and an Emitter to
     *  be forwarded as messenger.
     *
     * @param gameStateID the Id of the GameState connected to the Game
     * @param activeGameState the LogicalGameState used as a basis for the Game
     * @param emitter the Emitter used as a Messenger
     */
    public void startLoop(String gameStateID, LogicGameState activeGameState, SseEmitter emitter){
        activeID = gameStateID;
        activeLoop = new GameTicker(activeGameState, emitter);
        executorService.scheduleAtFixedRate(activeLoop, 0, 1000, TimeUnit.MILLISECONDS);
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
     * Forward the emitter to the active Loop to add it to the emitter List
     *
     * @param emitter to be added SSEEmitter
     */
    public void joinGame(SseEmitter emitter){
        activeLoop.addEmitter(emitter);
    }

    /**
     * Forward the emitter to the active Loop to remove it from the emitter List
     *
     * @param emitter to be removed SSEEmitter
     */
    public void leaveGame(SseEmitter emitter){
        activeLoop.removeEmitter(emitter);
    }

    /**
     * Stops the Thread, and emits a game Closed Message
     */
    public void endLoop(){
        executorService.shutdown();
        activeLoop.cleanUpEmitters("Game closed");
    }

    /**
     * Forward to active Loop to stop the Game
     */
    public void lockLoop(){
        activeLoop.stopGame();
    }

    /**
     * Forward to active Loop to continue the Game
     */
    public void openLoop(){
        activeLoop.continueGame();
    }

}
