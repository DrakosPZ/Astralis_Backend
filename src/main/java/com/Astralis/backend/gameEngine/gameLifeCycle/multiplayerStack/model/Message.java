package com.Astralis.backend.gameEngine.gameLifeCycle.multiplayerStack.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString()
@EqualsAndHashCode()
public class Message {
    private String gameID;
    private String userID;
    private Action action;
    private String specializedObject;
}
