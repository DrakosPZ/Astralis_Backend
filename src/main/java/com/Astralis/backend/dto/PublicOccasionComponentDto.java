package com.Astralis.backend.dto;

import com.Astralis.backend.model.PublicOccasionComponent;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublicOccasionComponentDto extends OccasionComponentDto {

    private int countOfPartakers;

    public PublicOccasionComponentDto(PublicOccasionComponent publicOccasionComponent){
        super(publicOccasionComponent);
        this.countOfPartakers = publicOccasionComponent.getCountOfPartakers() > 0 ? 0 : publicOccasionComponent.getCountOfPartakers();
    }
}
