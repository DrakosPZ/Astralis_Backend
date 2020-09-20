package com.Astralis.backend.service;

import com.Astralis.backend.dto.WorkWeekDto;
import com.Astralis.backend.model.WorkWeek;
import com.Astralis.backend.persistence.WorkWeekRepo;
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
public class WorkWeekService
        extends AbstractService<WorkWeekDto, WorkWeek> {

    private final WorkWeekRepo workWeekRepo;

    /**
     * Convert a given Model into a respective DTO
     *
     * @param model to be transformed
     * @return transformed DTO
     */
    @Override
    WorkWeekDto convertModelIntoDTO(WorkWeek model) {
        return new WorkWeekDto(model);
    }

    /**
     * Convert a given DTO into a respective Model
     *
     * @param dto to be transformed
     * @return transformed model
     */
    @Override
    WorkWeek convertDTOIntoModel(WorkWeekDto dto) {
        WorkWeek model = new WorkWeek(dto);
        return model;
    }

    /**
     * Step 1. update simple fields
     * Compares the old to the new WorkWeek model.
     * If a field has changed, the old model's field is updated to the new version.
     * The old model is then returned with the updated fields.
     *
     * @param old WorkWeek model
     * @param model new WorkWeek model
     * @return the old WorkWeek model with the updated fields.
     */
    @Override
    WorkWeek compareUpdate(WorkWeek old, WorkWeek model) {
        if(!old.getStartMonday().equals(model.getStartMonday())){
            old.setStartMonday(model.getStartMonday());
        }
        if(!old.getEndMonday().equals(model.getEndMonday())){
            old.setEndMonday(model.getEndMonday());
        }
        if(!old.getStartTuesday().equals(model.getStartTuesday())){
            old.setStartTuesday(model.getStartTuesday());
        }
        if(!old.getEndTuesday().equals(model.getEndTuesday())){
            old.setEndTuesday(model.getEndTuesday());
        }
        if(!old.getStartWednesday().equals(model.getStartWednesday())){
            old.setStartWednesday(model.getStartWednesday());
        }
        if(!old.getEndWednesday().equals(model.getEndWednesday())){
            old.setEndWednesday(model.getEndWednesday());
        }
        if(!old.getStartThursday().equals(model.getStartThursday())){
            old.setStartThursday(model.getStartThursday());
        }
        if(!old.getEndThursday().equals(model.getEndThursday())){
            old.setEndThursday(model.getEndThursday());
        }
        if(!old.getStartFriday().equals(model.getStartFriday())){
            old.setStartFriday(model.getStartFriday());
        }
        if(!old.getEndFriday().equals(model.getEndFriday())){
            old.setEndFriday(model.getEndFriday());
        }
        if(!old.getStartSaturday().equals(model.getStartSaturday())){
            old.setStartSaturday(model.getStartSaturday());
        }
        if(!old.getEndSaturday().equals(model.getEndSaturday())){
            old.setEndSaturday(model.getEndSaturday());
        }
        if(!old.getStartSunday().equals(model.getStartSunday())){
            old.setStartSunday(model.getStartSunday());
        }
        if(!old.getEndSunday().equals(model.getEndSunday())){
            old.setEndSunday(model.getEndSunday());
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
     * !!!This method is not yet important for WorkWeek, as there are no List Relationships
     *      in this model.
     *
     * @param old WorkWeek model.
     * @param dto new WorkWeek model.
     * @return the updated model from the database.
     */
    @Override
    WorkWeek storeListChanges(WorkWeek old, WorkWeekDto dto) {
        return old;
    }


    /**
     * Sets for every created Workweek <...>
     *     Nothing yet!!!.
     *
     * @param model the to be instantiated model.
     * @return the fully set-up model.
     */
    @Override
    WorkWeek setStandardData(WorkWeek model) {
        return model;
    }

    /**
     * Calls the find all method of the workWeekRepo.
     *
     * @return a List of all stored workWeeks in the table.
     */
    @Override
    List<WorkWeek> findall() {
        return workWeekRepo.findAll();
    }

    /**
     * Calls the save method of the workWeekRepo.
     *
     * @param model the to be stored model person.
     * @return the stored model.
     */
    @Override
    WorkWeek saveRep(WorkWeek model) {
        return workWeekRepo.save(model);
    }

    /**
     * For Updates only the fields are stored, relations are handled independently,
     * as such relation-fields are cleaned and the save method is called.
     *
     * @param model to be updated model WorkWeek.
     * @return the updated model.
     */
    @Override
    WorkWeek updateRep(WorkWeek model) {
        //clean Relations
        return workWeekRepo.save(model);
    }

    /**
     * Calls the find by Identifier method of the respective repo
     *
     * @param identifier the looked for model's identifier
     * @return the looked for model.
     */
    @Override
    Optional<WorkWeek> findByIdentifier(String identifier) {
        return workWeekRepo.findByIdentifier(identifier);
    }

    /**
     * Calls the delete by identifier method of the respective repo
     *
     * @param identifier the to be deleted model's identifier
     */
    @Override
    void deleteByIdentifier(String identifier) {
        workWeekRepo.deleteByIdentifier(identifier);
    }

    /**
     * no changes made before deletion, yet!
     * @param identifier
     */
    @Override
    void preDeleteCleanUp(String identifier) {

    }
}
