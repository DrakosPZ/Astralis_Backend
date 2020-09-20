package com.Astralis.backend.model;

import com.Astralis.backend.dto.TextOccasionComponentDto;
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
public class TextOccasionComponent extends OccasionComponent {
    private String title;
    private String text;


    public TextOccasionComponent(TextOccasionComponentDto textOccasionComponentDto){
        this.identifier = Optional.ofNullable(textOccasionComponentDto.getIdentifier())
                .orElse(UUID.randomUUID().toString());
        this.title = textOccasionComponentDto.getTitle() == null ? "" : textOccasionComponentDto.getTitle();
        this.text = textOccasionComponentDto.getText() == null ? "" : textOccasionComponentDto.getText();
    }
}
