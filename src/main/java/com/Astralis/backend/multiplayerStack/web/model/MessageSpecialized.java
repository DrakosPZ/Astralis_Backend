package com.Astralis.backend.multiplayerStack.web.model;

import com.Astralis.backend.multiplayerStack.web.model.specialized.SpecializedMessage;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode()
public class  MessageSpecialized {
    private String gameID;
    private String userID;
    private Action action;
    private SpecializedMessage specializedObject;

    public MessageSpecialized(Message message, SpecializedMessage specializedObject){
        gameID = message.getGameID();
        userID = message.getUserID();
        action = message.getAction();
        this.specializedObject = specializedObject;
    }

    public String toString(){
        return "!--Action in Game" + getGameID() + " User: " + getUserID() + " does " + getAction() + ": " + specializedObject.toString();
    }
}
