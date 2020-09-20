package com.Astralis.backend.dto;

import com.Astralis.backend.model.Person;
import com.Astralis.backend.model.ReferenceOccasionComponent;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReferenceOccasionComponentDto extends OccasionComponentDto {

    public String referenceType;
    public ReducedPersonDto refrencedPerson;

    public ReferenceOccasionComponentDto(ReferenceOccasionComponent referenceOccasionComponent){
        super(referenceOccasionComponent);
        this.referenceType = referenceOccasionComponent.getReferenceType().toString();
        this.refrencedPerson = null;
    }

    public void setReducedPersonDto(Person person){
        this.refrencedPerson = new ReducedPersonDto(person);
    }
}
