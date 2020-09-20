package com.Astralis.backend.service;

import com.Astralis.backend.dto.CalenderDto;
import com.Astralis.backend.dto.OccasionTagDto;
import com.Astralis.backend.dto.PersonDto;
import com.Astralis.backend.model.*;
import com.Astralis.backend.persistence.CalenderRepo;
import com.Astralis.backend.persistence.OccasionRepo;
import com.Astralis.backend.persistence.OccasionTagRepo;
import com.Astralis.backend.persistence.PersonRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class PersonService
        extends AbstractService<PersonDto, Person> {

    private final PersonRepo personRepo;
    private final CalenderRepo calenderRepo;
    private final OccasionRepo occasionRepo;
    private final OccasionTagRepo occasionTagRepo;
    private final BCryptPasswordEncoder encoder;

    /**
     * Convert a given Model into a respective DTO
     *
     * @param model to be transformed
     * @return transformed DTO
     */
    @Override
    PersonDto convertModelIntoDTO(Person model) {
        return new PersonDto(model);
    }

    /**
     * Convert a given DTO into a respective Model
     *
     * @param dto to be transformed
     * @return transformed model
     */
    @Override
    Person convertDTOIntoModel(PersonDto dto) {
        Person model = new Person(dto);
        return model;
    }

    /**
     * Step 1. update simple fields
     * Compares the old to the new Person model.
     * If a field has changed, the old model's field is updated to the new version.
     * The old model is then returned with the updated fields.
     *
     * @param old Person model
     * @param model new Person model
     * @return the old Person model with the updated fields.
     */
    @Override
    Person compareUpdate(Person old, Person model) {
        if(!old.getFirstName().equals(model.getFirstName())){
            old.setFirstName(model.getFirstName());
        }
        if(!old.getLastName().equals(model.getLastName())){
            old.setLastName(model.getLastName());
        }
        if(!old.getEMail().equals(model.getEMail())){
            old.setEMail(model.getEMail());
        }
        if(!old.getType().equals(model.getType())){
            old.setType(model.getType());
        }
        if(!old.getUsername().equals(model.getUsername())){
            old.setUsername(model.getUsername());
        }
        if(!encoder.matches(model.getPassword(), old.getPassword()) &&
           !model.getPassword().equals(old.getPassword())){
            old.setPassword(encoder.encode(model.getPassword()));
        }
        return old;
    }

    /**
     * Step 2. update relationship fields
     * Transforms the DTO into a new Model.
     * Compares every single relationship list of the old and new model.
     * If an element of the old list is not contained by the new list,
     *      the remove relationship method is called,
     *      as well as it's also removed from the new list.
     * If the element is present in the new list, it is removed from the new list.
     *
     * Finally all remaining elements of the new list are added according relationship method.
     *
     * As every got model at this point is a hibernate instance the changes are edited on the database.
     * So to commit the changes the model is called from the database.
     * The returned model is handed forward.
     *
     * @param old person model.
     * @param dto new Person dto.
     * @return the updated model from the database.
     */
    @Override
    Person storeListChanges(Person old, PersonDto dto) {
        //for those that are fully loaded by the converter
        Person model = convertDTOIntoModel(dto);
        //Tags
        List<OccasionTag> newListTag = model.getTags();
        List<OccasionTag> oldListTag = old.getTags();
        for (int index = 0; index < oldListTag.size(); index++) {
            OccasionTag occasionTag = oldListTag.get(index);
            if(!newListTag.contains(occasionTag)){
                removeTagFromPerson(model.getIdentifier(), occasionTag.getIdentifier());
                newListTag.remove(occasionTag);
            } else if(newListTag.contains(occasionTag)) {
                newListTag.remove(occasionTag);
            }
        }
        newListTag.forEach(occasionTag -> addTagToPerson(model.getIdentifier(), Optional.of(new OccasionTagDto(occasionTag))));

        //Calendars
        List<Calender> newListCalendar = model.getCalenders();
        List<Calender> oldListCalendar = old.getCalenders();
        for (int index = 0; index < oldListCalendar.size(); index++) {
            Calender calender = oldListCalendar.get(index);
            if(!newListCalendar.contains(calender)){
                removeCalendarFromPerson(model.getIdentifier(), calender.getIdentifier());
                newListCalendar.remove(calender);
            } else if(newListCalendar.contains(calender)) {
                newListCalendar.remove(calender);
            }
        }
        newListCalendar.forEach(calender -> addCalendarToPerson(model.getIdentifier(), Optional.of(new CalenderDto(calender))));

        return findByIdentifier(model.getIdentifier()).get();
    }

    /**
     * Sets for every created Person the standard tags,
     *          an empty calendar and
     *          an empty workweek.
     *
     * @param model the to be instantiated model.
     * @return the fully set-up model.
     */
    @Override
    Person setStandardData(Person model) {
        //set Standard Data
        OccasionTag tag = OccasionTag.builder().tag("Wichtig").colourHEX("#B00020").build();
        tag.setIdentifier(UUID.randomUUID().toString());
        model.addOccasionTag(tag);

        tag = OccasionTag.builder().tag("Neutral").colourHEX("#f4d03f").build();
        tag.setIdentifier(UUID.randomUUID().toString());
        model.addOccasionTag(tag);

        tag = OccasionTag.builder().tag("Positiv").colourHEX("#229954").build();
        tag.setIdentifier(UUID.randomUUID().toString());
        model.addOccasionTag(tag);

        WorkWeek emptyWorkweek = WorkWeek.builder().build();
        emptyWorkweek.setIdentifier(UUID.randomUUID().toString());

        Calender calender = Calender.builder().build();
        calender.setIdentifier(UUID.randomUUID().toString());
        calender.setWorkWeek(emptyWorkweek);
        calender.setFilterCommand("");
        model.addCalender(calender);

        return model;
    }

    /**
     * Calls the find all method of the personRepo.
     *
     * @return a List of all stored Persons in the table.
     */
    @Override
    List<Person> findall() {
        return personRepo.findAll();
    }

    /**
     * Calls the save method of the personRepo.
     * Also encodes the Password, as the newly stored Person Model doesn't do it automatically.
     *
     * @param model the to be stored model person.
     * @return the stored model.
     */
    @Override
    Person saveRep(Person model) {
        model.setPassword(encoder.encode(model.getPassword()));
        //clean Relations -- tags and calendars are supposed to be newly added on person creation
        model.setTags(new ArrayList<>());
        model.setCalenders(new ArrayList<>());
        return personRepo.save(model);
    }

    /**
     * For Updates only the fields are stored, relations are handled independently,
     * as such relation-fields are cleaned and the save method is called.
     *
     * @param model to be updated model person.
     * @return the updated model.
     */
    @Override
    Person updateRep(Person model) {
        //clean Relations
        model.setTags(new ArrayList<>());
        model.setCalenders(new ArrayList<>());

        return personRepo.save(model);
    }

    /**
     * Calls the find by Identifier method of the respective repo
     *
     * @param identifier the looked for model's identifier
     * @return the looked for model.
     */
    @Override
    Optional<Person> findByIdentifier(String identifier) {
        return personRepo.findByIdentifier(identifier);
    }

    /**
     * Calls the delete by identifier method of the respective repo
     *
     * @param identifier the to be deleted model's identifier
     */
    @Override
    void deleteByIdentifier(String identifier) {
        personRepo.deleteByIdentifier(identifier);
    }

    /**
     * no changes made before deletion, yet!
     * @param identifier
     */
    @Override
    void preDeleteCleanUp(String identifier) {
    }






    //----------------------Custome Methods----------------------
    /**
     * Returns the parent of the searched for calendars owner's parent
     *
     * @param identifier of person from whom the origins want to be known.
     * @param depth 0 = owner of calendar; 1 = parent of owner; 2 = parent of parent of owner; 3 = ...
     * @return List of possible PersonDto
     */
    public Optional<PersonDto> getPossibleOrigins(String identifier, int depth) {
        Calender calendarGiven = calenderRepo.findByIdentifier(identifier)
                .orElseThrow(() -> new IllegalArgumentException("No Calendar found with identifier: " + identifier));

        String tempIdentifier = calendarGiven.getBelongsTo().getIdentifier();
        Person owner = findByIdentifier(tempIdentifier)
                .orElseThrow(() -> new IllegalArgumentException("No Owner found with identifier: " + calendarGiven.getBelongsTo().getIdentifier()));
        for (int i = 0; i < depth; i++) {
            if(owner.getMemberOfID() == null){
                break;
            }
            tempIdentifier = owner.getMemberOfID().getIdentifier();
            final String errorIdentifier = tempIdentifier;
            owner = findByIdentifier(tempIdentifier)
                    .orElseThrow(() -> new IllegalArgumentException("No Owner found with identifier: " + errorIdentifier));

        }

        return Optional.of(owner)
                .map(m -> convertModelIntoDTO(m));
    }

    /**
     * Returns the looked for PersonDto based on it's username.
     *
     * @param username the looked for model's Username
     * @return the looked for PersonDto
     */
    public Optional<PersonDto> findByUsername(String username){
        return personRepo.findByUsername(username)
                .map(m -> convertModelIntoDTO(m));
    }

    /**
     * Returns the looked for PersonDto based on it's username and password.
     *
     * @param username the looked for model's Username
     * @param password the looked for model's Password
     * @return the looked for PersonDto
     */
    public Optional<PersonDto> findByUsernamePassword(String username, String password){
        Optional<Person> found = personRepo.findByUsername(username);
        if(found.isEmpty()){
            return found.map(m -> convertModelIntoDTO(m));
        }
        if(encoder.matches(password, found.get().getPassword())){
            return found.map(m -> convertModelIntoDTO(m));
        }
        throw new IllegalArgumentException("Passwords don't match");
    }









    //----------------------Relationship Handeling Methods----------------------
    public Optional<PersonDto> addPersonToTeam(String leaderID, Optional<PersonDto> personDto) {
        Person parent = personRepo.findByIdentifier(leaderID).orElseThrow(() -> new IllegalArgumentException("Persons ID has no according Person."));
        Person child = personRepo.findByIdentifier(personDto.get().getIdentifier()).get();
        parent.addMember(child);
        return Optional.of(child)
                .map(m -> convertModelIntoDTO(m));
    }

    public Optional<PersonDto> removePersonFromTeam(String leaderID, Optional<PersonDto> childDto) {
        Person parent = personRepo.findByIdentifier(leaderID).orElseThrow(() -> new IllegalArgumentException("Persons ID has no according Person."));
        Person child = personRepo.findByIdentifier(childDto.get().getIdentifier()).get();
        parent.removeMember(child);
        return Optional
                .of(child)
                .map(this::convertModelIntoDTO);
    }

    public Optional<PersonDto> addCalendarToPerson(String personID, Optional<CalenderDto> calenderDto) {
        Person parent = personRepo.findByIdentifier(personID).orElseThrow(() -> new IllegalArgumentException("Person ID has no according Person."));
        Calender child;
        child = calenderRepo.findByIdentifier(calenderDto.get().getIdentifier()).orElse(
                //possibility of the given Calendar not yet being implemented, as it is set as default data
                child = calenderRepo.save(new Calender(calenderDto.get()))
        );
        parent.addCalender(child);
        return Optional.of(parent)
                .map(m -> convertModelIntoDTO(m));
    }

    public Optional<PersonDto> removeCalendarFromPerson(String personID, String calendarID) {
        Person parent = personRepo.findByIdentifier(personID).orElseThrow(() -> new IllegalArgumentException("Person ID has no according Person."));
        Calender child = calenderRepo.findByIdentifier(calendarID).orElseThrow(() -> new IllegalArgumentException("Calendar ID has no according Calendar."));
        parent.removeCalender(child);
        return Optional
                .of(parent)
                .map(this::convertModelIntoDTO);
    }

    public Optional<PersonDto> addTagToPerson(String personID, Optional<OccasionTagDto> occasionTagDto) {
        Person parent = personRepo.findByIdentifier(personID).orElseThrow(() -> new IllegalArgumentException("Person ID has no according Person."));
        OccasionTag child = occasionTagRepo.findByIdentifier(occasionTagDto.get().getIdentifier()).orElse(
                //possibility of the given Tag not yet being implemented, as it is set as default data
                child = occasionTagRepo.save(new OccasionTag(occasionTagDto.get()))
        );
        parent.addOccasionTag(child);
        return Optional.of(parent)
                .map(m -> convertModelIntoDTO(m));
    }

    public Optional<PersonDto> removeTagFromPerson(String personID, String tagID) {
        Person parent = personRepo.findByIdentifier(personID).orElseThrow(() -> new IllegalArgumentException("Person ID has no according Person."));
        OccasionTag child = occasionTagRepo.findByIdentifier(tagID).orElseThrow(() -> new IllegalArgumentException("Tag ID has no according Tag."));
        parent.removeOccasionTag(child);
        return Optional
                .of(parent)
                .map(this::convertModelIntoDTO);
    }

    public Optional<PersonDto> addPresentInToPerson(String personID, String occasionID) {
        Person parent = personRepo.findByIdentifier(personID).orElseThrow(() -> new IllegalArgumentException("Person ID has no according Person."));
        Occasion child = occasionRepo.findByIdentifier(occasionID).orElseThrow(() -> new IllegalArgumentException("Calendar ID has no according Calendar."));
        parent.addPresentIn(child);
        return Optional.of(parent)
                .map(m -> convertModelIntoDTO(m));
    }

    public Optional<PersonDto> removePresentInFromPerson(String personID, String occasionID) {
        Person parent = personRepo.findByIdentifier(personID).orElseThrow(() -> new IllegalArgumentException("Person ID has no according Person."));
        Occasion child = occasionRepo.findByIdentifier(occasionID).orElseThrow(() -> new IllegalArgumentException("Calendar ID has no according Calendar."));
        parent.removePresentIn(child);
        return Optional
                .of(parent)
                .map(this::convertModelIntoDTO);
    }

    public Optional<PersonDto> addOwnedOccasionToPerson(String personID, String occasionID) {
        Person parent = personRepo.findByIdentifier(personID).orElseThrow(() -> new IllegalArgumentException("Person ID has no according Person."));
        Occasion child = occasionRepo.findByIdentifier(occasionID).orElseThrow(() -> new IllegalArgumentException("Calendar ID has no according Calendar."));
        parent.addOccasionToOwned(child);
        return Optional.of(parent)
                .map(m -> convertModelIntoDTO(m));
    }

    public Optional<PersonDto> removeOwnedOccasionFromPerson(String personID, String occasionID) {
        Person parent = personRepo.findByIdentifier(personID).orElseThrow(() -> new IllegalArgumentException("Person ID has no according Person."));
        Occasion child = occasionRepo.findByIdentifier(occasionID).orElseThrow(() -> new IllegalArgumentException("Calendar ID has no according Calendar."));
        parent.removeOccasionFromOwned(child);
        return Optional
                .of(parent)
                .map(this::convertModelIntoDTO);
    }
}