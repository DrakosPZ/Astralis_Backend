package com.Astralis.backend.multiplayerStack.web.controller;

import com.google.gson.Gson;
import com.Astralis.backend.gameLogic.model.LogicGameState;
import com.Astralis.backend.multiplayerStack.logicLoop.GameLoop;
import com.Astralis.backend.multiplayerStack.logicLoop.GameLoopManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/runningGame")
public class RunningGameController {

    //TODO: add Scream for help commentary
    private static RunningGameController refrence;

    private final SimpMessagingTemplate messagingTemplate;
    private final GameLoopManager gameLoopManager;

    @Autowired
    public RunningGameController(SimpMessagingTemplate messagingTemplate,
                                 GameLoopManager gameLoopManager){
        this.messagingTemplate = messagingTemplate;
        this.gameLoopManager = gameLoopManager;

        this.refrence = this;
    }

    public static RunningGameController getRefrence(){
        return refrence;
    }


    // Todo: Commentary
    //gets a SSE for partaking in Game
    @GetMapping(path = "/joinGame", params = "identifier")
    public LogicGameState joinGame(@RequestParam String identifier) {

        GameLoop gameLoop = gameLoopManager.findActiveGameLoop(identifier);
        if(gameLoop == null){
            throw new IllegalArgumentException("NO ACTIVE GAME FOUND WITH IDENTIFIER: " + identifier);
        }

        return gameLoop.getLogicGameState();
    }

    // Todo: Commentary
    //removing own SSE From Emitter List to stop message flooding when not partaking
    /*@GetMapping(path = "/joinGame/{gameIdentifier}")
    public SseEmitter leaveGame(
            @PathVariable String gameIdentifier, @RequestBody SseEmitter emitter) {

        GameLoop gameLoop = gameLoopManager.findActiveGameLoop(identifier);

        if(gameLoop == null){
            throw new IllegalArgumentException("NO ACTIVE GAME FOUND WITH IDENTIFIER: " + identifier);
        }

        gameLoop.leaveGame(emitter);
        return emitter;
    }*/


    @SendTo("/topic/reply/{id}")
    public String sendGameStateUpdate(String message, @DestinationVariable("id") String gameId){
        System.out.println(message);
        //this.messagingTemplate.convertAndSend("/message",  message);
        return  message;
    }

    @MessageMapping("/message/{id}")
    public void receiveGameMessage(@Payload String message, @DestinationVariable("id") String gameId){
    //TODO: Dissect Message from Client
    }

}
