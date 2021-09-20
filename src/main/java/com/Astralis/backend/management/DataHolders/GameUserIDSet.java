package com.Astralis.backend.management.DataHolders;

import com.Astralis.backend.management.dto.GameLobbyDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GameUserIDSet {
    private GameLobbyDTO gameLobby;
    private String userIdentifier;
}
