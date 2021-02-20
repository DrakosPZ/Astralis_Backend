package com.Astralis.backend.accountManagement.DataHolders;

import com.Astralis.backend.accountManagement.dto.GameStateDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GameUserIDSet {
    private GameStateDTO gameState;
    private String userIdentifier;
}
