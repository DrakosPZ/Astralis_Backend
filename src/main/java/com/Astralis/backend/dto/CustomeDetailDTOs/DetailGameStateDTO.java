package com.Astralis.backend.dto.CustomeDetailDTOs;

import com.Astralis.backend.dto.AbstractModelDto;
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
    private List<DetailUserGameStateDTO> userGameStates = new ArrayList<>();
}
