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

    //if someone finds this and has a a better Idea how to implement the Controller
    //which it may already be as read by the further down getReference Method,
    //please implement the better solutions
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

    //TODO: Maybe remove this part after 0.4 for I don't see why I implemented it in 0.3
    public static RunningGameController getReference(){
        return reference;
    }

    /**
     * Method to send out a web-socket message to all connected frontend clients.
     * It uses the "/runningGame/topic/gameUpdates/GAMEID" route.
     * For sending it uses SimpleMessagingTemplate.
     *
     * @param message The jsonfied GameState.
     * @param gameId the ID of the game for which it is supposed to be send out.
     */
    public void sendGameStateUpdate(String message, @DestinationVariable("id") String gameId){
        System.out.println(message);
        this.messagingTemplate.convertAndSend(UPDATE_ROUTE + gameId,  message);
        //return  message;
    }

    /**
     * Method to receive player's action input and forward them to the proper gameLoop.
     *
     * @param message The player's action as jsonfied String object.
     * @param gameId The according gameId to which the action belongs.
     */
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
