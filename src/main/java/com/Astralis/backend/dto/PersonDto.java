package com.Astralis.backend.dto;

import com.Astralis.backend.model.Person;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonDto extends AbstractModelDto {

    //recruite
    private String eMail;
    private String firstName;
    private String lastName;
    private String type;
    private String memberOfID;
    private List<PersonDto> hasMembers = new ArrayList<>();
    //partner
    private String username;
    private String password;

    private List<CalenderDto> calenders;
    private List<OccasionTagDto> tags;
    private List<OccasionDto> presentIn;
    private List<OccasionDto> ownsOccasions;

    public PersonDto(Person person) {
        super(person.getIdentifier());
        this.eMail = person.getEMail() == null ? "" : person.getEMail();
        this.firstName = person.getFirstName() == null ? "" : person.getFirstName();
        this.lastName = person.getLastName() == null ? "" : person.getLastName();
        this.type = person.getType().toString();
        this.memberOfID = person.getMemberOfID() == null ? "" : person.getMemberOfID().getIdentifier();
        this.username = person.getUsername() == null ? "" : person.getUsername();
        this.password = person.getPassword() == null ? "" : person.getPassword();
        this.hasMembers = person.getHasMembers() == null ? new ArrayList<>() : person.getHasMembers()
            .stream().map(element -> new PersonDto(element))
            .collect(Collectors.toList());

        this.calenders = person.getCalenders() == null ? new ArrayList<>() : person.getCalenders()
                .stream().map(element -> new CalenderDto(element))
                .collect(Collectors.toList());
        this.tags = person.getTags() == null ? new ArrayList<>() : person.getTags()
                .stream().map(element -> new OccasionTagDto(element))
                .collect(Collectors.toList());
        this.presentIn = person.getPresentIn() == null ? new ArrayList<>() : person.getPresentIn()
                .stream().map(element -> new OccasionDto(element))
                .collect(Collectors.toList());

        this.ownsOccasions = person.getOwnsOccasions() == null ? new ArrayList<>() : person.getOwnsOccasions()
                .stream().map(element -> new OccasionDto(element))
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PersonDto personDto = (PersonDto) o;
        return Objects.equals(eMail, personDto.eMail) &&
                Objects.equals(getFirstName(), personDto.getFirstName()) &&
                Objects.equals(getLastName(), personDto.getLastName()) &&
                Objects.equals(getType(), personDto.getType()) &&
                Objects.equals(getMemberOfID(), personDto.getMemberOfID()) &&
                Objects.equals(getUsername(), personDto.getUsername()) &&
                Objects.equals(getPassword(), personDto.getPassword()) &&
                Objects.equals(getHasMembers(), personDto.getHasMembers());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), eMail, getFirstName(), getLastName(), getType(), getMemberOfID(), getUsername(), getPassword(), getHasMembers());
    }

    @Override
    public String toString() {
        return "PersonDto{" +
                "eMail='" + eMail + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", type='" + type + '\'' +
                ", memberOfID=" + memberOfID +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", hasMembers=" + hasMembers +
                '}';
    }
}
