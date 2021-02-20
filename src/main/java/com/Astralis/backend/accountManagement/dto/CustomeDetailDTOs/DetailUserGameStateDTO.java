package com.Astralis.backend.accountManagement.dto.CustomeDetailDTOs;

import com.Astralis.backend.accountManagement.dto.AbstractModelDto;
import com.Astralis.backend.accountManagement.dto.UserDTO;
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
