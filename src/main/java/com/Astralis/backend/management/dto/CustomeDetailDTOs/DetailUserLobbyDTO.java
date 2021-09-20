package com.Astralis.backend.management.dto.CustomeDetailDTOs;

import com.Astralis.backend.management.dto.AbstractModelDto;
import com.Astralis.backend.management.dto.UserDTO;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetailUserLobbyDTO extends AbstractModelDto {
    private UserDTO user;
    private String gameRole;
    private String gameLobby;
}
