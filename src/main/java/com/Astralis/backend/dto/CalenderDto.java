package com.Astralis.backend.dto;

import com.Astralis.backend.model.Calender;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalenderDto extends AbstractModelDto {

    private List<OccasionDto> plannedOccasions;
    private WorkWeekDto worksFromTo;
    private String filterCommand;

    private String belongsTo;

    public CalenderDto(Calender calender){
        super(calender.getIdentifier());
        this.filterCommand = calender.getFilterCommand() == null ? "": calender.getFilterCommand();
        this.plannedOccasions = calender.getPlannedOccasions() == null ? new ArrayList<>() :
                calender.getPlannedOccasions()
                    .stream().map(element -> new OccasionDto(element))
                    .collect(Collectors.toList());
        this.worksFromTo = new WorkWeekDto(calender.getWorksFromTo());
        this.belongsTo = calender.getBelongsTo() == null ? "" : calender.getBelongsTo().getIdentifier();
    }
}
