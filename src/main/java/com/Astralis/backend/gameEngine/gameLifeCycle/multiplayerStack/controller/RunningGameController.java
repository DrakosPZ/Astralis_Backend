package com.Astralis.backend.gameEngine.gameLifeCycle.multiplayerStack.controller;

import com.Astralis.backend.gameEngine.gameLifeCycle.logicLoop.GameLoop;
import com.Astralis.backend.gameEngine.gameLifeCycle.multiplayerStack.model.MessageSpecialized;
import com.Astralis.backend.gameEngine.gameLifeCycle.multiplayerStack.service.MessageDissectionService;
import com.Astralis.backend.gameEngine.gameLifeCycle.logicLoop.GameLoopManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/runningGame")
public class RunningGameController {

    private final String UPDATE_ROUTE = "/topic/gameUpdates/";
    private final String RECEIVING_ROUTE = "/message/{id}";

    //TODO: add Scream for help commentary
    private static RunningGameController reference;

    private final SimpMessagingTemplate messagingTemplate;
    private final GameLoopManager gameLoopManager;
    private final MessageDissectionService messageDissectionService;

    @Autowired
    public RunningGameController(SimpMessagingTemplate messagingTemplate,
                                 GameLoopManager gameLoopManager,
                                 MessageDissectionService messageDissectionService){
        this.messagingTemplate = messagingTemplate;
        this.gameLoopManager = gameLoopManager;
        this.messageDissectionService = messageDissectionService;

        this.reference = this;
    }

    public static RunningGameController getReference(){
        return reference;
    }

    //TODO: Commentary for Updating Method
    public String sendGameStateUpdate(String message, @DestinationVariable("id") String gameId){
        System.out.println(message);
        this.messagingTemplate.convertAndSend(UPDATE_ROUTE + gameId,  message);
        return  message;
    }

    //TODO: Add Commentary for the receiving server Side Link
    @MessageMapping(RECEIVING_ROUTE)
    public void receiveGameMessage(@Payload String message, @DestinationVariable("id") String gameId){
        System.out.println("Message to: " + gameId + " - " + message);
        MessageSpecialized actionCall = messageDissectionService.interpreteMessage(message);
        GameLoop gameLoop = gameLoopManager.findActiveGameLoop(gameId);
        if(gameLoop == null){
            throw new IllegalArgumentException("NO ACTIVE GAME FOUND WITH IDENTIFIER: " + gameId);
        }
        gameLoop.forwardAction(actionCall);
    }

}
