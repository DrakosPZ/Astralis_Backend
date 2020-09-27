package com.Astralis.backend.DataHolders;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GameUserRoleSet {
    private String identifierUser;
    private String identifierGameState;
    private String role;
}
