package com.Astralis.backend.model;

import com.Astralis.backend.dto.OccasionComponentDto;
import com.Astralis.backend.dto.OccasionDto;
import com.Astralis.backend.dto.OccasionTagDto;
import com.Astralis.backend.dto.PersonDto;
import lombok.*;

import javax.persistence.*;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.*;

@Getter
@Setter
@Entity
@Builder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Occasion extends AbstractModel {

    private String title;
    private String description;
    private ZonedDateTime start;
    private ZonedDateTime end;
    private Duration duration;
    private String place;
    private boolean isPublic;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private List<OccasionTag> tags = new ArrayList<>();

    private OccasionType category;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<Person> partaking = new ArrayList<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy = "usedIn")
    private List<OccasionComponent> allUsedComponents = new ArrayList<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id")
    private Calender belongsTo;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id")
    private Person ownedBy;

    public Occasion(OccasionDto occasionDto) {
        this.identifier = Optional.ofNullable(occasionDto.getIdentifier())
                .orElse(UUID.randomUUID().toString());
        this.title = occasionDto.getTitle() == null ? "" : occasionDto.getTitle();
        this.description = occasionDto.getDescription() == null ? "" : occasionDto.getDescription();
        this.start = occasionDto.getStart() == null ? ZonedDateTime.now() : occasionDto.getStart();
        this.end = occasionDto.getEnd() == null ? ZonedDateTime.now() : occasionDto.getEnd();
        this.duration = occasionDto.getDuration() == null ? Duration.between(occasionDto.getStart(), occasionDto.getEnd()) : occasionDto.getDuration();
        this.place = occasionDto.getPlace() == null ? "" : occasionDto.getPlace();
        this.isPublic = occasionDto.isPublic();

        (occasionDto.getTags() == null ? new ArrayList<OccasionTagDto>() : occasionDto.getTags())
                .stream()
                .map(x -> new OccasionTag((OccasionTagDto) x))
                .forEach(this::addOccasionTag);
        this.category = OccasionType.valueOf(occasionDto.getCategory());
        (occasionDto.getPartaking() == null ? new ArrayList<PersonDto>() : occasionDto.getPartaking())
                .stream()
                .map(x -> new Person((PersonDto) x))
                .forEach(this::addPartaking);
        (occasionDto.getAllUsedComponents() == null ? new ArrayList<OccasionComponentDto>() : occasionDto.getAllUsedComponents())
                .stream()
                .map(x -> OccasionComponent.instantiateRightComponent((OccasionComponentDto) x))
                .forEach(this::addOccasionComponent);

        if (occasionDto.getOwnedBy() == null) {
            setOccasionOwnedBy(null);
        } else {
            setOccasionOwnedBy(new Person(occasionDto.getOwnedBy()));
        }
    }








    //----------------------N:M Relationship Methods----------------------
    public void addOccasionTag(OccasionTag occasionTag) {
        if (tags.contains(occasionTag)) {
            return;
        }
        tags.add(occasionTag);

        if (this.getId() != null) {
            occasionTag.addOccasion(this);
        }
    }

    public void removeOccasionTag(OccasionTag occasionTag) {
        if (!tags.contains(occasionTag)) {
            return;
        }
        occasionTag.removeOccasion(this);
        tags.remove(occasionTag);
    }

    public void removeAllOccasionTags() {
        tags.forEach(occasionTag -> occasionTag.getPresentIn().remove(this));
        tags = new ArrayList<>();
    }

    public void addPartaking(Person person) {
        if (partaking.contains(person)) {
            return;
        }
        partaking.add(person);

        if (this.getId() != null) {
            person.addPresentIn(this);
        }
    }

    public void removePartaking(Person person) {
        if (!partaking.contains(person)) {
            return;
        }
        person.removePresentIn(this);
        partaking.remove(person);
    }








    //----------------------1:N Relationship Methods----------------------
    public void addOccasionComponent(OccasionComponent occasionComponent) {
        if (allUsedComponents.contains(occasionComponent)) {
            return;
        }
        allUsedComponents.add(occasionComponent);
        occasionComponent.setUsedIn(this);
    }

    public void removeOccasionComponent(OccasionComponent occasionComponent) {
        if (!allUsedComponents.contains(occasionComponent)) {
            return;
        }
        occasionComponent.setUsedIn(null);
        allUsedComponents.remove(occasionComponent);
    }








    //----------------------N:1 Relationship Methods----------------------
    public void setCalenderBelongsTo(Calender calender) {
        if (Objects.equals(this.belongsTo, calender)) {
            return;
        }

        Calender oldCalender = this.belongsTo;
        this.belongsTo = calender;

        if (oldCalender != null) {
            oldCalender.removeOccasion(this);
        }

        if (calender != null) {
            calender.addOccasion(this);
        }

        setOccasionOwnedBy(calender.getBelongsTo());
    }


    public void setOccasionOwnedBy(Person person) {
        if (Objects.equals(this.ownedBy, person)) {
            return;
        }

        Person oldPerson = this.ownedBy;
        this.ownedBy = person;

        if (oldPerson != null) {
            oldPerson.removeOccasionFromOwned(this);
        }

        if (person != null) {
            person.addOccasionToOwned(this);
        }
    }
}
