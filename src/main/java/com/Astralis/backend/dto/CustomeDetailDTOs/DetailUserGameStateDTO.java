package com.Astralis.backend.dto.CustomeDetailDTOs;

import com.Astralis.backend.dto.AbstractModelDto;
import com.Astralis.backend.dto.UserDTO;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetailUserGameStateDTO extends AbstractModelDto {
    private UserDTO user;
    private String gameRole;
    private String gameState;
}
