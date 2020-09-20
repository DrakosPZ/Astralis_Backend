package com.Astralis.backend.dto;

import com.Astralis.backend.model.*;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Data
@Inheritance(strategy= InheritanceType.TABLE_PER_CLASS)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = PublicOccasionComponentDto.class, name="publicComponent"),
        @JsonSubTypes.Type(value = ReferenceOccasionComponentDto.class, name="referenceComponent"),
        @JsonSubTypes.Type(value = TextOccasionComponentDto.class, name="textComponent"),
        @JsonSubTypes.Type(value = VirtualOccasionComponentDto.class, name="virtualComponent")
})
public abstract class OccasionComponentDto extends AbstractModelDto {
    private String usedIn;

    public OccasionComponentDto(OccasionComponent occasionComponent){
        super(occasionComponent.getIdentifier());
        this.usedIn = occasionComponent.getUsedIn().getIdentifier();
    }

    public static OccasionComponentDto instantiateRightComponentDto(OccasionComponent occasionComponent){
        if(occasionComponent instanceof PublicOccasionComponent){
            return new PublicOccasionComponentDto((PublicOccasionComponent)occasionComponent);
        }
        if(occasionComponent instanceof ReferenceOccasionComponent){
            return new ReferenceOccasionComponentDto((ReferenceOccasionComponent)occasionComponent);
        }
        if(occasionComponent instanceof TextOccasionComponent){
            return new TextOccasionComponentDto((TextOccasionComponent)occasionComponent);
        }
        if(occasionComponent instanceof VirtualOccasionComponent){
            return new VirtualOccasionComponentDto((VirtualOccasionComponent)occasionComponent);
        }
        throw new IllegalArgumentException("This point shouldn't be reached");
    }
}
