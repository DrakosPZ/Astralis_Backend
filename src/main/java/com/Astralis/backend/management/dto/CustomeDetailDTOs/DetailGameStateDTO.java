package com.Astralis.backend.management.dto.CustomeDetailDTOs;

import com.Astralis.backend.management.dto.AbstractModelDto;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetailGameStateDTO extends AbstractModelDto {
    private String name;
    private String description;
    private String image;
    private String status;
    private List<DetailUserGameStateDTO> userGameStates = new ArrayList<>();
}
