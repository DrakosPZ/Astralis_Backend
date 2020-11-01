package com.Astralis.logic.mechanic;


import com.Astralis.logic.mechanic._runnables.GameTicker;
import com.Astralis.logic.model.LogicGameState;
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

    // Todo: Add Commentary
    public void startLoop(LogicGameState activeGameState, SseEmitter emitter){
        GameTicker tickingLoop = new GameTicker(activeGameState, emitter);
        executorService.scheduleAtFixedRate(tickingLoop, 0, 1000, TimeUnit.MILLISECONDS);
    }

    // Todo: Add Commentary
    public void endLoop(){
        executorService.shutdown();
    }

}
