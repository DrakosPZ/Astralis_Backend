package com.Astralis.backend.dto;


import com.Astralis.backend.model.OccasionTag;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OccasionTagDto extends AbstractModelDto {

    private String tag;
    private String colourHEX;

    private List<String> presentIn;

    private String belongsTo;

    public OccasionTagDto(OccasionTag occasionTag){
        super(occasionTag.getIdentifier());
        this.tag = occasionTag.getTag();
        this.colourHEX = occasionTag.getColourHEX();
        this.presentIn = new ArrayList<>();
        this.presentIn = occasionTag.getPresentIn() == null ? new ArrayList<>() :
                occasionTag.getPresentIn()
                        .stream().map(element -> element.getIdentifier())
                        .collect(Collectors.toList());
        this.belongsTo = occasionTag.getBelongsTo() == null ? "" : occasionTag.getBelongsTo().getIdentifier();
    }


}
