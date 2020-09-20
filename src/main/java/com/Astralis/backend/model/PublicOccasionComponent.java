package com.Astralis.backend.model;

import com.Astralis.backend.dto.PublicOccasionComponentDto;
import lombok.*;

import javax.persistence.Entity;
import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PublicOccasionComponent extends OccasionComponent {

    private int countOfPartakers;

    public PublicOccasionComponent(PublicOccasionComponentDto publicOccasionComponentDto){
        this.identifier = Optional.ofNullable(publicOccasionComponentDto.getIdentifier())
                .orElse(UUID.randomUUID().toString());
        this.countOfPartakers = publicOccasionComponentDto.getCountOfPartakers() > 0 ? 0 : publicOccasionComponentDto.getCountOfPartakers();
    }
}
