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

    // Todo: Add Commentary
    public void startLoop(String gameStateID, LogicGameState activeGameState, SseEmitter emitter){
        activeLoop = new GameTicker(activeGameState, emitter);
        activeID = gameStateID;
        executorService.scheduleAtFixedRate(activeLoop, 0, 1000, TimeUnit.MILLISECONDS);
    }

    // Todo: Add Commentary
    public String getID(){
        return activeID;
    }

    // Todo: Add Commentary
    public void joinGame(SseEmitter emitter){
        activeLoop.addEmitter(emitter);
    }

    // Todo: Add Commentary
    public void leaveGame(SseEmitter emitter){
        activeLoop.removeEmitter(emitter);
    }

    // Todo: Add Commentary
    public void endLoop(){
        executorService.shutdown();
        activeLoop.cleanUpEmitters("Game closed");
    }

}
