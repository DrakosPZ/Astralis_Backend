package com.Astralis.backend.model;

import com.Astralis.backend.dto.WorkWeekDto;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.time.LocalTime;
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
public class WorkWeek extends AbstractModel {

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

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id")
    private Calender belongsTo;

    public WorkWeek(WorkWeekDto workWeekDto){
        this.identifier = Optional.ofNullable(workWeekDto.getIdentifier())
                .orElse(UUID.randomUUID().toString());
        this.startMonday = workWeekDto.getStartMonday() == null ? LocalTime.now() : workWeekDto.getStartMonday();
        this.endMonday = workWeekDto.getEndMonday() == null ? LocalTime.now() : workWeekDto.getEndMonday();
        this.startTuesday = workWeekDto.getStartTuesday() == null ? LocalTime.now() : workWeekDto.getStartTuesday();
        this.endTuesday = workWeekDto.getEndTuesday() == null ? LocalTime.now() : workWeekDto.getEndTuesday();
        this.startWednesday = workWeekDto.getStartWednesday() == null ? LocalTime.now() : workWeekDto.getStartWednesday();
        this.endWednesday = workWeekDto.getEndWednesday() == null ? LocalTime.now() : workWeekDto.getEndWednesday();
        this.startThursday = workWeekDto.getStartThursday() == null ? LocalTime.now() : workWeekDto.getStartThursday();
        this.endThursday = workWeekDto.getEndThursday() == null ? LocalTime.now() : workWeekDto.getEndThursday();
        this.startFriday = workWeekDto.getStartFriday() == null ? LocalTime.now() : workWeekDto.getStartFriday();
        this.endFriday = workWeekDto.getEndFriday() == null ? LocalTime.now() : workWeekDto.getEndFriday();
        this.startSaturday = workWeekDto.getStartSaturday() == null ? LocalTime.now() : workWeekDto.getStartSaturday();
        this.endSaturday = workWeekDto.getEndSaturday() == null ? LocalTime.now() : workWeekDto.getEndSaturday();
        this.startSunday = workWeekDto.getStartSunday() == null ? LocalTime.now() : workWeekDto.getStartSunday();
        this.endSunday = workWeekDto.getEndSunday() == null ? LocalTime.now() : workWeekDto.getEndSunday();
    }
}
