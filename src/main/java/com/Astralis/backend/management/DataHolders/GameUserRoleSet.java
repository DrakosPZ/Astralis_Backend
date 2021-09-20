package com.Astralis.backend.management.DataHolders;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GameUserRoleSet {
    private String identifierUser;
    private String identifierGameLobby;
    private String role;
}
