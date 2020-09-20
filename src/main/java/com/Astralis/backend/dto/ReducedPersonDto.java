package com.Astralis.backend.dto;

import com.Astralis.backend.model.Person;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReducedPersonDto extends AbstractModelDto{

    private String eMail;
    private String firstName;
    private String lastName;
    private String type;
    private String memberOfID;
    //partner
    private String username;
    private String password;

    public ReducedPersonDto(Person person) {
        super(person.getIdentifier());
        this.eMail = person.getEMail() == null ? "" : person.getEMail();
        this.firstName = person.getFirstName() == null ? "" : person.getFirstName();
        this.lastName = person.getLastName() == null ? "" : person.getLastName();
        this.type = person.getType().toString();
        //this.memberOfID = person.getMemberOfID()==null ? null : new PersonDto(person.getMemberOfID());
        this.memberOfID = person.getMemberOfID() == null ? "" : person.getMemberOfID().getIdentifier();
        this.username = person.getUsername() == null ? "" : person.getUsername();
        this.password = person.getPassword() == null ? "" : person.getPassword();
    }
}
