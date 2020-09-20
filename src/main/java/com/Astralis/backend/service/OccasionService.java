package com.Astralis.backend.service;

import com.Astralis.backend.dto.OccasionDto;
import com.Astralis.backend.model.Calender;
import com.Astralis.backend.model.Occasion;
import com.Astralis.backend.model.OccasionTag;
import com.Astralis.backend.persistence.CalenderRepo;
import com.Astralis.backend.persistence.OccasionRepo;
import com.Astralis.backend.persistence.OccasionTagRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class OccasionService
        extends AbstractService<OccasionDto, Occasion> {

    private final OccasionRepo occasionRepo;
    private final OccasionTagRepo occasionTagRepo;
    private final CalenderRepo calenderRepo;

    /**
     * Convert a given Model into a respective DTO
     *
     * @param model to be transformed
     * @return transformed DTO
     */
    @Override
    OccasionDto convertModelIntoDTO(Occasion model) {
        return new OccasionDto(model);
    }

    /**
     * Convert a given DTO into a respective Model
     *
     * @param dto to be transformed
     * @return transformed model
     */
    @Override
    Occasion convertDTOIntoModel(OccasionDto dto) {
        Occasion model = new Occasion(dto);
        return model;
    }

    /**
     * Step 1. update simple fields
     * Compares the old to the new Occasion model.
     * If a field has changed, the old model's field is updated to the new version.
     * The old model is then returned with the updated fields.
     *
     * @param old Occasion model
     * @param model new Occasion model
     * @return the old Occasion model with the updated fields.
     */
    @Override
    public Occasion compareUpdate(Occasion old, Occasion model) {
        //only Check Fields
        if(!old.getTitle().equals(model.getTitle())){
            old.setTitle(model.getTitle());
        }
        if(!old.getDescription().equals(model.getDescription())){
            old.setDescription(model.getDescription());
        }
        if(!old.getStart().equals(model.getStart())){
            old.setStart(model.getStart());
        }
        if(!old.getEnd().equals(model.getEnd())){
            old.setEnd(model.getEnd());
        }
        if(!old.getDuration().equals(model.getDuration())){
            old.setDuration(model.getDuration());
        }
        if(!old.getPlace().equals(model.getPlace())){
            old.setPlace(model.getPlace());
        }
        if(!old.isPublic()==model.isPublic()){
            old.setPublic(model.isPublic());
        }
        if(!old.getCategory().equals(model.getCategory())){
            old.setCategory(model.getCategory());
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
     * @param old OccasionTag model.
     * @param dto new OccasionTag dto.
     * @return the updated model from the database.
     */
    public Occasion storeListChanges(Occasion old, OccasionDto dto){
        //for those that are fully loaded by the converter
        Occasion model = convertDTOIntoModel(dto);

        //Update Occasion Tags List from old to be identical with model
        List<OccasionTag> newList = model.getTags();
        List<OccasionTag> oldList = old.getTags();
        for (int index = 0; index < oldList.size(); index++) {
            OccasionTag occasionTag = oldList.get(index);
            if(!newList.contains(occasionTag)){
                removeTagFromOccasion(model.getIdentifier(), occasionTag.getIdentifier());
                newList.remove(occasionTag);
            } else if(newList.contains(occasionTag)) {
                newList.remove(occasionTag);
            }
        }
        newList.forEach(occasionTag -> addTagToOccasion(model.getIdentifier(), occasionTag.getIdentifier()));
        //allUsedComponents -- as Components aren't yet implemented fully this segment is for now left vacant.

        //partaking-ButtonPress so not
        //belongsTo-ButtonPress so not
        //ownedBy-ButtonPress so not

        return findByIdentifier(model.getIdentifier()).get();
    }

    /**
     * Calls the find all method of the occasionRepo.
     *
     * @return a List of all stored Occasions in the table.
     */
    @Override
    List<Occasion> findall() {
        return occasionRepo.findAll();
    }

    /**
     * For Updates only the fields are stored, relations are handled independently,
     * as such relation-fields are cleaned and the save method is called.
     *
     * @param model to be updated model Occasion.
     * @return the updated model.
     */
    Occasion updateRep(Occasion model) {
        //clean tags List
        model.setTags(new ArrayList<>());
        model.setAllUsedComponents(new ArrayList<>());
        return occasionRepo.save(model);
    }

    /**
     * Calls the save method of the occasionRepo.
     *
     * @param model the to be stored model Occasion.
     * @return the stored model.
     */
    @Override
    Occasion saveRep(Occasion model) {
        //clean Relations
        model.setTags(new ArrayList<>());
        model.setAllUsedComponents(new ArrayList<>());
        return occasionRepo.save(model);
    }

    /**
     * Sets for every created Occasion the standard <...>
     * Nothing yet added!!!
     *
     * @param model the to be instantiated model.
     * @return the fully set-up model.
     */
    @Override
    Occasion setStandardData(Occasion model) {
        return model;
    }


    /**
     * Calls the find by Identifier method of the respective repo
     *
     * @param identifier the looked for model's identifier
     * @return the looked for model.
     */
    @Override
    Optional<Occasion> findByIdentifier(String identifier) {
        return occasionRepo.findByIdentifier(identifier);
    }

    /**
     * Calls the delete by identifier method of the respective repo
     *
     * @param identifier the to be deleted model's identifier
     */
    @Override
    void deleteByIdentifier(String identifier) {
        occasionRepo.deleteByIdentifier(identifier);
    }

    /**
     * no changes made before deletion, yet!
     * @param identifier
     */
    @Override
    void preDeleteCleanUp(String identifier) {

    }









    //----------------------Relationship Handeling Methods----------------------
    public Optional<OccasionDto> addTagToOccasion(String occasionID, String tagID) {
        Occasion parent = occasionRepo.findByIdentifier(occasionID).orElseThrow(() -> new IllegalArgumentException("Occasion ID has no according Occasion."));
        OccasionTag child = occasionTagRepo.findByIdentifier(tagID).orElseThrow(() -> new IllegalArgumentException("OccasionTag ID has no according OccasionTag."));
        parent.addOccasionTag(child);
        return Optional.of(parent)
                .map(m -> convertModelIntoDTO(m));
    }

    public Optional<OccasionDto> removeTagFromOccasion(String occasionID, String tagID) {
        Occasion parent = occasionRepo.findByIdentifier(occasionID).orElseThrow(() -> new IllegalArgumentException("Occasion ID has no according Occasion."));
        OccasionTag child = occasionTagRepo.findByIdentifier(tagID).orElseThrow(() -> new IllegalArgumentException("OccasionTag ID has no according OccasionTag."));
        parent.removeOccasionTag(child);
        return Optional
                .of(parent)
                .map(this::convertModelIntoDTO);
    }


    public Optional<OccasionDto> addOccasionToCalender(String leaderID, Optional<OccasionDto> occasionDto) {
        Calender parent = calenderRepo.findByIdentifier(leaderID).orElseThrow(() -> new IllegalArgumentException("Persons ID has no according Person."));
        Occasion child = occasionRepo.findByIdentifier(occasionDto.get().getIdentifier()).get();
        parent.addOccasion(child);
        return Optional.of(child)
                .map(m -> convertModelIntoDTO(m));
    }

    public Optional<OccasionDto> removeOccasionFromCalender(String leaderID, Optional<OccasionDto> childDto) {
        Calender parent = calenderRepo.findByIdentifier(leaderID).orElseThrow(() -> new IllegalArgumentException("Persons ID has no according Person."));
        Occasion child = occasionRepo.findByIdentifier(childDto.get().getIdentifier()).get();
        parent.removeOccasion(child);
        return Optional
                .of(child)
                .map(this::convertModelIntoDTO);
    }
}
