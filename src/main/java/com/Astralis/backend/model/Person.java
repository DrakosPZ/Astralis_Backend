package com.Astralis.backend.model;

import com.Astralis.backend.dto.*;
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
public class Person extends AbstractModel {
    //recruite
    @NonNull
    private String eMail;
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    @NonNull
    private PersonType type;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(cascade = CascadeType.PERSIST,fetch = FetchType.LAZY, mappedBy = "memberOfID")
    private List<Person> hasMembers = new ArrayList<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id")
    private Person memberOfID;

    //partner
    private String username;
    private String password;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(cascade = {CascadeType.ALL, CascadeType.REFRESH}, fetch = FetchType.LAZY, mappedBy = "belongsTo")
    private List<Calender> calenders = new ArrayList<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(cascade = {CascadeType.ALL, CascadeType.REFRESH}, fetch = FetchType.LAZY, mappedBy = "belongsTo")
    private List<OccasionTag> tags = new ArrayList<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, mappedBy = "partaking")
    private List<Occasion> presentIn = new ArrayList<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, mappedBy = "ownedBy")
    private List<Occasion> ownsOccasions = new ArrayList<>();

    public Person(PersonDto personDto) {
        this.identifier = Optional.ofNullable(personDto.getIdentifier())
                .orElse(UUID.randomUUID().toString());
        this.eMail = personDto.getEMail() == null ? "" : personDto.getEMail();
        this.firstName = personDto.getFirstName() == null ? "" : personDto.getFirstName();
        this.lastName = personDto.getLastName() == null ? "" : personDto.getLastName();
        this.type = PersonType.valueOf(personDto.getType());
        this.username = personDto.getUsername() == null ? "" : personDto.getUsername();
        this.password = personDto.getPassword() == null ? "" : personDto.getPassword();

        (personDto.getHasMembers() == null ? new ArrayList<PersonDto>() : personDto.getHasMembers())
                .stream()
                .map(Person::new)
                .forEach(this::addMember);

        (personDto.getCalenders() == null ? new ArrayList<CalenderDto>() : personDto.getCalenders())
                .stream()
                .map(x -> new Calender((CalenderDto)x))
                .forEach(this::addCalender);
        (personDto.getTags() == null ? new ArrayList<OccasionTagDto>() : personDto.getTags())
                .stream()
                .map(x -> new OccasionTag((OccasionTagDto) x))
                .forEach(this::addOccasionTag);
        (personDto.getPresentIn() == null ? new ArrayList<OccasionDto>() : personDto.getPresentIn())
                .stream()
                .map(x -> new Occasion((OccasionDto) x))
                .forEach(this::addPresentIn);

        (personDto.getOwnsOccasions() == null ? new ArrayList<OccasionDto>() : personDto.getOwnsOccasions())
                .stream()
                .map(x -> new Occasion((OccasionDto) x))
                .forEach(this::addOccasionToOwned);

    }

    public Person(MiniaturePersonDto miniaturePersonDto) {
        this.identifier = Optional.ofNullable(miniaturePersonDto.getIdentifier())
                .orElse(UUID.randomUUID().toString());
        this.eMail = miniaturePersonDto.getEMail() == null ? "" : miniaturePersonDto.getEMail();
        this.firstName = miniaturePersonDto.getFirstName() == null ? "" : miniaturePersonDto.getFirstName();
        this.lastName = miniaturePersonDto.getLastName() == null ? "" : miniaturePersonDto.getLastName();
        this.type = PersonType.valueOf(miniaturePersonDto.getType());
    }








    //----------------------1:N Relationship Methods----------------------
    public void addMember(Person person) {
        if (hasMembers.contains(person)) {
            return;
        }
        hasMembers.add(person);
        person.setMemberOfID(this);
    }

    public void removeMember(Person person) {
        if (!hasMembers.contains(person)) {
            return;
        }
        person.setMemberOfID(null);
        hasMembers.remove(person);
    }

    public void addOccasionTag(OccasionTag occasionTag) {
        if (tags.contains(occasionTag)) {
            return;
        }
        tags.add(occasionTag);
        occasionTag.setBelongsTo(this);
    }

    public void removeOccasionTag(OccasionTag occasionTag) {
        if (!tags.contains(occasionTag)) {
            return;
        }
        occasionTag.setBelongsTo(null);
        tags.remove(occasionTag);
    }

    public void addCalender(Calender calender) {
        if (calenders.contains(calender)) {
            return;
        }
        calenders.add(calender);
        calender.setBelongsTo(this);
    }

    public void removeCalender(Calender calender) {
        if (!calenders.contains(calender)) {
            return;
        }
        calender.setBelongsTo(null);
        calenders.remove(calender);
    }

    public void addOccasionToOwned(Occasion occasion) {
        if (ownsOccasions.contains(occasion)) {
            return;
        }
        ownsOccasions.add(occasion);
        occasion.setOccasionOwnedBy(this);
    }

    public void removeOccasionFromOwned(Occasion occasion) {
        if (!ownsOccasions.contains(occasion)) {
            return;
        }
        occasion.setOccasionOwnedBy(null);
        ownsOccasions.remove(occasion);
    }




    //----------------------N:1 Relationship Methods----------------------
    public void setMemberOfID(Person person) {
        if (Objects.equals(this.memberOfID, person)) {
            return;
        }

        Person oldPerson = this.memberOfID;
        this.memberOfID = person;

        if (oldPerson != null) {
            oldPerson.removeMember(this);
        }

        if (person != null) {
            person.addMember(this);
        }
    }




    //----------------------N:M Relationship Methods----------------------
    public void addPresentIn(Occasion occasion) {
        if (presentIn.contains(occasion)) {
            return;
        }
        presentIn.add(occasion);

        if (this.getId() != null) {
            occasion.addPartaking(this);
        }
    }

    public void removePresentIn(Occasion occasion) {
        if (!tags.contains(occasion)) {
            return;
        }
        occasion.removePartaking(this);
        presentIn.remove(occasion);
    }
}
