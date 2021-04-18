package com.Astralis.backend.multiplayerStack.web.controller;

import com.Astralis.backend.multiplayerStack.logicLoop.GameLoop;
import com.Astralis.backend.multiplayerStack.logicLoop.GameLoopManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Controller
//@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@RequestMapping(path = "/runningGame")
public class RunningGameController {

    private final SimpMessagingTemplate template;
    private final GameLoopManager gameLoopManager;

    @Autowired
    RunningGameController(SimpMessagingTemplate template, GameLoopManager gameLoopManager){
        this.template = template;
        this.gameLoopManager = gameLoopManager;
    }



    // Todo: Commentary
    //gets a SSE for partaking in Game
    @GetMapping(path = "/joinGame", params = "identifier")
    public SseEmitter joinGame(@RequestParam String identifier) {

        //#TODO: Shouldn't that part be handled by a service mehtod instead of the controller
        SseEmitter emitter = new SseEmitter(gameLoopManager.getTimeoutMillis());
        GameLoop gameLoop = gameLoopManager.findActiveGameLoop(identifier);

        if(gameLoop == null){
            throw new IllegalArgumentException("NO ACTIVE GAME FOUND WITH IDENTIFIER: " + identifier);
        }

        gameLoop.joinGame(emitter);
        return emitter;
    }

    // Todo: Commentary
    //removing own SSE From Emitter List to stop message flooding when not partaking
    @GetMapping(path = "/leaveGame", params = "identifier")
    public SseEmitter leaveGame(
            @RequestParam String identifier, @RequestBody SseEmitter emitter) {

        GameLoop gameLoop = gameLoopManager.findActiveGameLoop(identifier);

        if(gameLoop == null){
            throw new IllegalArgumentException("NO ACTIVE GAME FOUND WITH IDENTIFIER: " + identifier);
        }

        gameLoop.leaveGame(emitter);
        return emitter;
    }


    @MessageMapping("/send/message")
    public void sendMessage(String message){
        System.out.println(message);

        this.template.convertAndSend("/message",  message);
    }

}
