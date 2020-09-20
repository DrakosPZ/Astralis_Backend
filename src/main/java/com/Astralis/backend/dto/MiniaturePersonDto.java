package com.Astralis.backend.dto;

import com.Astralis.backend.model.Person;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;


@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class MiniaturePersonDto extends AbstractModelDto {
    private String eMail;
    private String firstName;
    private String lastName;
    private String type;
    private String memberOfID;

    public MiniaturePersonDto(Person person) {
        super(person.getIdentifier());
        this.eMail = person.getEMail() == null ? "" : person.getEMail();
        this.firstName = person.getFirstName() == null ? "" : person.getFirstName();
        this.lastName = person.getLastName() == null ? "" : person.getLastName();
        this.type = person.getType().toString();
        this.memberOfID = person.getMemberOfID() == null ? "" : person.getMemberOfID().getIdentifier();
    }
}
