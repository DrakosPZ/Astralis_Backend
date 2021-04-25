package com.Astralis.backend.multiplayerStack.web.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString()
@EqualsAndHashCode()
public class MessageSpecialized {
    private String gameID;
    private String userID;
    private Action action;
    private Object specializedObject;
}
