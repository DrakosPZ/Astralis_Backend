package com.Astralis.backend.model;

import com.Astralis.backend.dto.*;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public abstract class OccasionComponent extends AbstractModel {

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinColumn(referencedColumnName = "id")
    private Occasion usedIn;

    public OccasionComponent(OccasionComponentDto occasionComponentDto){
        this.identifier = Optional.ofNullable(occasionComponentDto.getIdentifier())
                .orElse(UUID.randomUUID().toString());
    }

    public void setUsedIn(Occasion occasion) {
        if (Objects.equals(this.usedIn, occasion)) {
            return;
        }

        Occasion oldOccasion = this.usedIn;
        this.usedIn = occasion;

        if (oldOccasion != null) {
            oldOccasion.removeOccasionComponent(this);
        }

        if (occasion != null) {
            occasion.addOccasionComponent(this);
        }
    }

    public static OccasionComponent instantiateRightComponent(OccasionComponentDto occasionComponentDto){
        if(occasionComponentDto instanceof PublicOccasionComponentDto){
            return new PublicOccasionComponent((PublicOccasionComponentDto)occasionComponentDto);
        }
        if(occasionComponentDto instanceof ReferenceOccasionComponentDto){
            return new ReferenceOccasionComponent((ReferenceOccasionComponentDto)occasionComponentDto);
        }
        if(occasionComponentDto instanceof TextOccasionComponentDto){
            return new TextOccasionComponent((TextOccasionComponentDto)occasionComponentDto);
        }
        if(occasionComponentDto instanceof VirtualOccasionComponentDto){
            return new VirtualOccasionComponent((VirtualOccasionComponentDto)occasionComponentDto);
        }
        throw new IllegalArgumentException("This point shouldn't be reached");
    }
}
