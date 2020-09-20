package com.Astralis.backend.dto;

import com.Astralis.backend.model.VirtualOccasionComponent;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VirtualOccasionComponentDto extends OccasionComponentDto {
    private String link;
    private String infos;

    public VirtualOccasionComponentDto(VirtualOccasionComponent virtualOccasionComponent){
        super(virtualOccasionComponent);
        this.link = virtualOccasionComponent.getLink() == null ? "" : virtualOccasionComponent.getLink();
        this.infos = virtualOccasionComponent.getInfos() == null ? "" : virtualOccasionComponent.getInfos();
    }
}
