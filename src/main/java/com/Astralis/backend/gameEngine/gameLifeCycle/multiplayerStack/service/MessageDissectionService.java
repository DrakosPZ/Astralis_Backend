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
     * Method to turn a jsonfied player's action into a fully constructed message
     * Object containing the player's action.
     *
     * As the Message object contains the general information (for example what type of action it is.)
     * and the specialized action specific information, it first has to dejsonfy the general object,
     * after which the specialized Object can be dejsonfied.
     *
     * @param message The jsonfied player's action.
     * @return The fully dejsonfied Message Object, containing the player's action.
     */
    public MessageSpecialized interpreteMessage(String message){
        Message messagePojo = JsonToPojo(message);
        return readPojo(messagePojo);
    }

    /**
     * Method to turn the jsonfied message into a message Object.
     * The message object holds the jsonfied action specific object.
     *
     * @param json The jsonfied message object.
     * @return The message object containing the general action information.
     */
    private Message JsonToPojo(String json){
        Message message = gson.fromJson(json, Message.class);
        return message;
    }

    /**
     * Method to dejsonfy the specialized message string inside the given message pojo.
     *
     * @param pojo The message pojo containing the jsonfied specialized message.
     * @return The fully dejsonfied specialized message object containing a players action.
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
