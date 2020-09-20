package com.Astralis.backend.model;

import com.Astralis.backend.dto.VirtualOccasionComponentDto;
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
public class VirtualOccasionComponent extends OccasionComponent {
    private String link;
    private String infos;

    public VirtualOccasionComponent(VirtualOccasionComponentDto virtualOccasionComponentDto){
        this.identifier = Optional.ofNullable(virtualOccasionComponentDto.getIdentifier())
                .orElse(UUID.randomUUID().toString());
        this.link = virtualOccasionComponentDto.getLink() == null ? "" : virtualOccasionComponentDto.getLink();
        this.infos = virtualOccasionComponentDto.getInfos() == null ? "" : virtualOccasionComponentDto.getInfos();
    }
}
