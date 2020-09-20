package com.Astralis.backend.model;

import com.Astralis.backend.dto.CalenderDto;
import com.Astralis.backend.dto.OccasionDto;
import lombok.*;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@Entity
@Builder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Calender extends AbstractModel {

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY, mappedBy = "belongsTo")
    private List<Occasion> plannedOccasions = new ArrayList<>();

    @Column(length=1000000)
    private String filterCommand;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY,  mappedBy = "belongsTo")
    private WorkWeek worksFromTo = new WorkWeek();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id")
    private Person belongsTo;

    public Calender(CalenderDto calenderDto){
        this.identifier = Optional.ofNullable(calenderDto.getIdentifier())
                .orElse(UUID.randomUUID().toString());
        (calenderDto.getPlannedOccasions() == null ? new ArrayList<OccasionDto>() : calenderDto.getPlannedOccasions())
                .stream()
                .map(x -> new Occasion((OccasionDto) x))
                .forEach(this::addOccasion);
        this.filterCommand = calenderDto.getFilterCommand() == null ? "" : calenderDto.getFilterCommand();
        if(calenderDto.getWorksFromTo() != null) {
            setWorkWeek(new WorkWeek(calenderDto.getWorksFromTo()));
        } else {
            setWorkWeek(new WorkWeek());
        }
    }








    //----------------------1:1 Relationship Methods----------------------
    public void setWorkWeek(WorkWeek workWeek) {
        if(worksFromTo != null) {
            if (worksFromTo.equals(workWeek)) {
                return;
            }
        }
        worksFromTo = workWeek;
        workWeek.setBelongsTo(this);
    }








    //----------------------1:N Relationship Methods----------------------
    public void addOccasion(Occasion occasion) {
        if (plannedOccasions.contains(occasion)) {
            return;
        }
        plannedOccasions.add(occasion);
        occasion.setCalenderBelongsTo(this);
    }

    public void removeOccasion(Occasion occasion) {
        if (!plannedOccasions.contains(occasion)) {
            return;
        }
        occasion.setCalenderBelongsTo(null);
        plannedOccasions.remove(occasion);
    }








    //----------------------N:1 Relationship Methods----------------------
    public void setBelongsTo(Person person) {
        if (Objects.equals(this.belongsTo, person)) {
            return;
        }

        Person oldPerson = this.belongsTo;
        this.belongsTo = person;

        if (oldPerson != null) {
            oldPerson.removeCalender(this);
        }

        if (person != null) {
            person.addCalender(this);
        }
    }
}
