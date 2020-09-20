package com.Astralis.backend.dto;

import com.Astralis.backend.model.Occasion;
import lombok.*;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OccasionDto extends AbstractModelDto {

    private String title;
    private String description;
    private ZonedDateTime start;
    private ZonedDateTime end;
    private Duration duration;
    private String place;
    private boolean isPublic;


    private List<OccasionTagDto> tags;
    private String category;
    private List<PersonDto> partaking;
    private List<OccasionComponentDto> allUsedComponents;

    private String belongsTo;
    private MiniaturePersonDto ownedBy;

    public OccasionDto(Occasion occasion) {
        super(occasion.getIdentifier());
        this.title = occasion.getTitle() == null ? "" : occasion.getTitle();
        this.description = occasion.getDescription() == null ? "" : occasion.getDescription();
        this.start = occasion.getStart() == null ? ZonedDateTime.now() : occasion.getStart();
        this.end = occasion.getEnd() == null ? ZonedDateTime.now() : occasion.getEnd();
        this.duration = occasion.getDuration() == null ? Duration.between(occasion.getStart(), occasion.getEnd()) : occasion.getDuration();
        this.place = occasion.getPlace() == null ? "" : occasion.getPlace();
        this.isPublic = occasion.isPublic();

        this.tags = occasion.getTags() == null ? new ArrayList<>() : occasion.getTags()
                .stream().map(element -> new OccasionTagDto(element))
                .collect(Collectors.toList());
        this.category = occasion.getCategory().toString();
        this.partaking = occasion.getPartaking() == null ? new ArrayList<>() : occasion.getPartaking()
                .stream().map(element -> new PersonDto(element))
                .collect(Collectors.toList());
        this.allUsedComponents = occasion.getAllUsedComponents() == null ? new ArrayList<>() : occasion.getAllUsedComponents()
                .stream().map(element -> OccasionComponentDto.instantiateRightComponentDto(element))
                .collect(Collectors.toList());
        this.belongsTo = occasion.getBelongsTo() == null ? "" : occasion.getBelongsTo().getIdentifier();

        this.ownedBy = occasion.getOwnedBy() == null ? null : new MiniaturePersonDto(occasion.getOwnedBy());
    }
}
