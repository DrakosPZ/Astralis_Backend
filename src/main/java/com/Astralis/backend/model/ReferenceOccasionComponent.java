package com.Astralis.backend.model;

import com.Astralis.backend.dto.ReferenceOccasionComponentDto;
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
public class ReferenceOccasionComponent extends OccasionComponent {

    public PersonType referenceType;

    //@ToString.Exclude
    //@EqualsAndHashCode.Exclude
    //@ManyToOne(fetch = FetchType.LAZY)
    public String refrencedPersonID;

    public ReferenceOccasionComponent(ReferenceOccasionComponentDto referenceOccasionComponentDto){
        this.identifier = Optional.ofNullable(referenceOccasionComponentDto.getIdentifier())
                .orElse(UUID.randomUUID().toString());
        this.referenceType = PersonType.valueOf(referenceOccasionComponentDto.getReferenceType());
        this.refrencedPersonID = referenceOccasionComponentDto.getRefrencedPerson().getIdentifier() == null ? "" : referenceOccasionComponentDto.getRefrencedPerson().getIdentifier();
    }
}
