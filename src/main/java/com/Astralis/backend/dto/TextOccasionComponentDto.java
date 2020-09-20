package com.Astralis.backend.dto;

import com.Astralis.backend.model.TextOccasionComponent;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TextOccasionComponentDto extends OccasionComponentDto {

    private String title;
    private String text;

    public TextOccasionComponentDto(TextOccasionComponent textOccasionComponent){
        super(textOccasionComponent);
        this.title = textOccasionComponent.getTitle() == null ? "" : textOccasionComponent.getTitle();
        this.text = textOccasionComponent.getTitle() == null ? "" : textOccasionComponent.getText();
    }
}
