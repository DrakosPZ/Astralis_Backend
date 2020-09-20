package com.Astralis.backend.dto;

import com.Astralis.backend.model.WorkWeek;
import lombok.*;

import java.time.LocalTime;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkWeekDto extends AbstractModelDto {

    private LocalTime startMonday;
    private LocalTime endMonday;
    private LocalTime startTuesday;
    private LocalTime endTuesday;
    private LocalTime startWednesday;
    private LocalTime endWednesday;
    private LocalTime startThursday;
    private LocalTime endThursday;
    private LocalTime startFriday;
    private LocalTime endFriday;
    private LocalTime startSaturday;
    private LocalTime endSaturday;
    private LocalTime startSunday;
    private LocalTime endSunday;

    private String belongsTo;

    public WorkWeekDto(WorkWeek workWeek){
        super(workWeek.getIdentifier());
        this.startMonday = workWeek.getStartMonday() == null ? LocalTime.now() : workWeek.getStartMonday();
        this.endMonday = workWeek.getStartMonday() == null ? LocalTime.now() : workWeek.getEndMonday();
        this.startTuesday = workWeek.getStartMonday() == null ? LocalTime.now() : workWeek.getStartTuesday();
        this.endTuesday = workWeek.getStartMonday() == null ? LocalTime.now() : workWeek.getEndTuesday();
        this.startWednesday = workWeek.getStartMonday() == null ? LocalTime.now() : workWeek.getStartWednesday();
        this.endWednesday = workWeek.getStartMonday() == null ? LocalTime.now() : workWeek.getEndWednesday();
        this.startThursday = workWeek.getStartMonday() == null ? LocalTime.now() : workWeek.getStartThursday();
        this.endThursday = workWeek.getStartMonday() == null ? LocalTime.now() : workWeek.getEndThursday();
        this.startFriday = workWeek.getStartMonday() == null ? LocalTime.now() : workWeek.getStartFriday();
        this.endFriday = workWeek.getStartMonday() == null ? LocalTime.now() : workWeek.getEndFriday();
        this.startSaturday = workWeek.getStartMonday() == null ? LocalTime.now() : workWeek.getStartSaturday();
        this.endSaturday = workWeek.getStartMonday() == null ? LocalTime.now() : workWeek.getEndSaturday();
        this.startSunday = workWeek.getStartMonday() == null ? LocalTime.now() : workWeek.getStartSunday();
        this.endSunday = workWeek.getStartMonday() == null ? LocalTime.now() : workWeek.getEndSunday();
        this.belongsTo = workWeek.getBelongsTo() == null ? "" : workWeek.getBelongsTo().getIdentifier();
    }

}
