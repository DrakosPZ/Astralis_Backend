package com.Astralis.backend.multiplayerStack.web.service;

import com.Astralis.backend.multiplayerStack.web.model.Action;
import com.Astralis.backend.multiplayerStack.web.model.Message;
import com.Astralis.backend.multiplayerStack.web.model.MessageSpecialized;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor(onConstructor_ = {@Autowired})
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@NoArgsConstructor(onConstructor_ = {@Autowired})
public class MessageDissectionService {

    @Autowired
    private Gson gson;

    public MessageSpecialized interpreteMessage(String message){
        Message messagePojo = JsonToPojo(message);
        return readPojo(messagePojo);
    }

    private Message JsonToPojo(String json){
        //TODO: Call GSON
    }

    private MessageSpecialized readPojo(Message pojo){
        MessageSpecialized read;
        switch (pojo.getAction()){
            case Action.Move:
                //TODO: Code and use the specialized POJOs
                break;
        }
        return read;
    }
}
