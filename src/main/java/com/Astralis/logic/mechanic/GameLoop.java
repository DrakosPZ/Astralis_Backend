package com.Astralis.logic.mechanic;


import com.Astralis.logic.mechanic._runnables.GameTicker;
import com.Astralis.logic.model.LogicGameState;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@NoArgsConstructor
@AllArgsConstructor
public class GameLoop {

    ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);

    // Todo: Add Commentary
    public void startLoop(LogicGameState activeGameState){
        GameTicker obj = new GameTicker(activeGameState);
        executorService.scheduleAtFixedRate(obj, 0, 10, TimeUnit.MILLISECONDS);
    }

    // Todo: Add Commentary
    public void endLoop(){
        executorService.shutdown();
    }

}
