package com.Astralis.backend.DataHolders;

import com.Astralis.backend.dto.GameStateDTO;
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
