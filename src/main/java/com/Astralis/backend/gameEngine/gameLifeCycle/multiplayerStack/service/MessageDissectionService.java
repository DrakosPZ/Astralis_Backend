package com.Astralis.backend.gameEngine.gameLifeCycle.multiplayerStack.service;

import com.Astralis.backend.gameEngine.gameLifeCycle.multiplayerStack.model.Message;
import com.Astralis.backend.gameEngine.gameLifeCycle.multiplayerStack.model.MessageSpecialized;
import com.Astralis.backend.gameEngine.gameLifeCycle.multiplayerStack.model.specialized.MoveShip;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class MessageDissectionService {

    @Autowired
    private Gson gson;

    /**
     * TODO: ADD COMMENTARY
     * @param message
     * @return
     */
    public MessageSpecialized interpreteMessage(String message){
        Message messagePojo = JsonToPojo(message);
        return readPojo(messagePojo);
    }

    /**
     * TODO: ADD COMMENTARY
     * @param json
     * @return
     */
    private Message JsonToPojo(String json){
        Message message = gson.fromJson(json, Message.class);
        return message;
    }

    /**
     * TODO: ADD COMMENTARY
     * @param pojo
     * @return
     */
    private MessageSpecialized readPojo(Message pojo){
        MessageSpecialized read;
        switch (pojo.getAction()){
            case MOVE:
                read = new MessageSpecialized(pojo, gson.fromJson(pojo.getSpecializedObject(), MoveShip.class));
                break;
            default:
                throw new IllegalArgumentException("Error in Reading Message Pojo: No Action Found");
        }
        return read;
    }
}
