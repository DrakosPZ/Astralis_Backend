package com.Astralis.backend.service;

import com.Astralis.backend.dto.CalenderDto;
import com.Astralis.backend.model.Calender;
import com.Astralis.backend.model.Person;
import com.Astralis.backend.persistence.CalenderRepo;
import com.Astralis.backend.persistence.PersonRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CalenderService
        extends AbstractService<CalenderDto, Calender> {

    private final CalenderRepo calenderRepo;
    private final PersonRepo personRepo;

    /**
     * Convert a given Model into a respective DTO
     *
     * @param model to be transformed
     * @return transformed DTO
     */
    @Override
    CalenderDto convertModelIntoDTO(Calender model) {
        return new CalenderDto(model);
    }

    /**
     * Convert a given DTO into a respective Model
     *
     * @param dto to be transformed
     * @return transformed model
     */
    @Override
    Calender convertDTOIntoModel(CalenderDto dto) {
        Calender model = new Calender(dto);

        return model;
    }

    /**
     * Step 1. update simple fields
     * Compares the old to the new Calender model.
     * If a field has changed, the old model's field is updated to the new version.
     * The old model is then returned with the updated fields.
     *
     * @param old Calender model
     * @param model new Calender model
     * @return the old Calender model with the updated fields.
     */
    @Override
    Calender compareUpdate(Calender old, Calender model) {
        if(!old.getWorksFromTo().equals(model.getWorksFromTo())){
            old.setWorkWeek(model.getWorksFromTo());
        }if(!old.getPlannedOccasions().equals(model.getPlannedOccasions())){
            old.setPlannedOccasions(model.getPlannedOccasions());
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
     * @param old Calender model.
     * @param dto new Calender dto.
     * @return the updated model from the database.
     */
    @Override
    Calender storeListChanges(Calender old, CalenderDto dto) {
        //planned Occasion edited by button click--- so no behaviour needed.
        return old;
    }


    /**
     * Calls the find all method of the calenderRepo.
     *
     * @return a List of all stored Calender in the table.
     */
    @Override
    List<Calender> findall() {
        return calenderRepo.findAll();
    }

    /**
     * Calls the find by Identifier method of the respective repo
     *
     * @param identifier the looked for model's identifier
     * @return the looked for model.
     */
    @Override
    Optional<Calender> findByIdentifier(String identifier) {
        return calenderRepo.findByIdentifier(identifier);
    }

    /**
     * Calls the save method of the calenderRepo.
     *
     * @param model the to be stored model Calender.
     * @return the stored model.
     */
    @Override
    Calender saveRep(Calender model) {
        return calenderRepo.save(model);
    }

    /**
     * Sets for every created Calender the standard <...>
     * Nothing yet added!!!
     *
     * @param model the to be instantiated model.
     * @return the fully set-up model.
     */
    @Override
    Calender setStandardData(Calender model) {
        return model;
    }

    /**
     * For Updates only the fields are stored, relations are handled independently,
     * as such relation-fields are cleaned and the save method is called.
     *
     * @param model to be updated model Calender.
     * @return the updated model.
     */
    @Override
    Calender updateRep(Calender model) {
        //clean Relations
        return calenderRepo.save(model);
    }

    /**
     * Calls the delete by identifier method of the respective repo
     *
     * @param identifier the to be deleted model's identifier
     */
    @Override
    void deleteByIdentifier(String identifier) {
        calenderRepo.deleteByIdentifier(identifier);
    }

    /**
     * No specific pre delete behaviour needed.
     * @param identifier of the to be deleted Calender
     */
    @Override
    void preDeleteCleanUp(String identifier) {

    }






    //----------------------Custome Methods----------------------

    /**
     * Method to only update Filter Command.
     * As this update occurs after the object has already been added,
     *      the update doesn't have to be stored separately but is stored automatically.
     *
     * @param identifier of the to be updated Calendar object.
     * @param filterCommand to be stored in the Calendar object.
     * @return Option al of the updated Calendar object.
     */
    public Optional<CalenderDto> updateFilterCommand(String identifier, String filterCommand) {
        Calender calender = calenderRepo.findByIdentifier(identifier).get();
        calender.setFilterCommand(filterCommand);
        return Optional.of(calender)
                .map(m -> convertModelIntoDTO(m));
    }






    //----------------------Relationship Handeling Methods----------------------

    public Optional<CalenderDto> addCalenderToPerson(String leaderID, Optional<CalenderDto> personDto) {
        Person parent = personRepo.findByIdentifier(leaderID).orElseThrow(() -> new IllegalArgumentException("Persons ID has no according Person."));
        Calender child = calenderRepo.findByIdentifier(personDto.get().getIdentifier()).get();
        parent.addCalender(child);
        return Optional.of(child)
                .map(m -> convertModelIntoDTO(m));
    }

    public Optional<CalenderDto> removeCalenderToPerson(String leaderID, Optional<CalenderDto> childDto) {
        Person parent = personRepo.findByIdentifier(leaderID).orElseThrow(() -> new IllegalArgumentException("Persons ID has no according Person."));
        Calender child = calenderRepo.findByIdentifier(childDto.get().getIdentifier()).get();
        parent.removeCalender(child);
        return Optional
                .of(child)
                .map(this::convertModelIntoDTO);
    }
}
