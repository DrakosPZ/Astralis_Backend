package com.Astralis.backend.service;

import com.Astralis.backend.dto.OccasionTagDto;
import com.Astralis.backend.model.Occasion;
import com.Astralis.backend.model.OccasionTag;
import com.Astralis.backend.model.Person;
import com.Astralis.backend.persistence.OccasionRepo;
import com.Astralis.backend.persistence.OccasionTagRepo;
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
public class OccasionTagService
        extends AbstractService<OccasionTagDto, OccasionTag> {

    private final OccasionTagRepo occasionTagRepo;
    private final OccasionService occasionService;
    private final OccasionRepo occasionRepo;
    private final PersonRepo personRepo;

    /**
     * Convert a given Model into a respective DTO
     *
     * @param model to be transformed
     * @return transformed DTO
     */
    @Override
    OccasionTagDto convertModelIntoDTO(OccasionTag model) {
        return new OccasionTagDto(model);
    }

    /**
     * Convert a given DTO into a respective Model
     *
     * @param dto to be transformed
     * @return transformed model
     */
    @Override
    OccasionTag convertDTOIntoModel(OccasionTagDto dto) {
        OccasionTag model = new OccasionTag(dto);
        return model;
    }

    /**
     * Step 1. update simple fields
     * Compares the old to the new OccasionTag model.
     * If a field has changed, the old model's field is updated to the new version.
     * The old model is then returned with the updated fields.
     *
     * @param old OccasionTag model
     * @param model new OccasionTag model
     * @return the old OccasionTag model with the updated fields.
     */
    @Override
    OccasionTag compareUpdate(OccasionTag old, OccasionTag model) {
        if(!old.getColourHEX().equals(model.getColourHEX())){
            old.setColourHEX(model.getColourHEX());
        }
        if(!old.getTag().equals(model.getTag())){
            old.setTag(model.getTag());
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
    @Override
    OccasionTag storeListChanges(OccasionTag old, OccasionTagDto dto) {
        //for those that are fully loaded by the converter
        OccasionTag model = convertDTOIntoModel(dto);
        //PresentIn --- not done as it would require the frontend to keep the data structure
        // as consistent as the backend does! To big of a change that late in the project,
        // for too little of a trade Off.
        /*List<Occasion> newListPresentIn = new ArrayList<>();
            dto.getPresentIn()
                    .forEach(s -> newListPresentIn.add(occasionRepo.findByIdentifier(s).get()));
            List<Occasion> oldListPresentIn = old.getPresentIn();
            for (int index = 0; index < oldListPresentIn.size(); index++) {
                Occasion occasion = oldListPresentIn.get(index);
                if(!newListPresentIn.contains(occasion)){
                    removePresentInFromOccassionTag(model.getIdentifier(), occasion.getIdentifier());
                    newListPresentIn.remove(occasion);
                } else if(newListPresentIn.contains(occasion)) {
                    newListPresentIn.remove(occasion);
                }
            }
            newListPresentIn.forEach(occasion -> addPresentInToOccassionTag(model.getIdentifier(), occasion.getIdentifier()));
        */
        return old;
    }

    /**
     * Sets for every created OccasionTag the standard <...>
     * Nothing yet added!!!
     *
     * @param model the to be instantiated model.
     * @return the fully set-up model.
     */
    @Override
    OccasionTag setStandardData(OccasionTag model) {
        return model;
    }

    /**
     * Calls the find all method of the occasionTagRepo.
     *
     * @return a List of all stored OccasionTags in the table.
     */
    @Override
    List<OccasionTag> findall() {
        return occasionTagRepo.findAll();
    }

    /**
     * Calls the save method of the occasionTagRepo.
     *
     * @param model the to be stored model OccasionTag.
     * @return the stored model.
     */
    @Override
    OccasionTag saveRep(OccasionTag model) {
        //clean Relations
        return occasionTagRepo.save(model);
    }

    /**
     * For Updates only the fields are stored, relations are handled independently,
     * as such relation-fields are cleaned and the save method is called.
     *
     * @param model to be updated model OccasionTag.
     * @return the updated model.
     */
    @Override
    OccasionTag updateRep(OccasionTag model) {
        //clean Relations
        return occasionTagRepo.save(model);
    }


    /**
     * Calls the find by Identifier method of the respective repo
     *
     * @param identifier the looked for model's identifier
     * @return the looked for model.
     */
    @Override
    Optional<OccasionTag> findByIdentifier(String identifier) {
        return occasionTagRepo.findByIdentifier(identifier);
    }

    /**
     * Calls the delete by identifier method of the respective repo
     *
     * @param identifier the to be deleted model's identifier
     */
    @Override
    void deleteByIdentifier(String identifier) {
        occasionTagRepo.deleteByIdentifier(identifier);
    }

    /**
     * Uses the given Identifier to get the to be deleted model from the database.
     *      Then it removes itself from the owners tags list,
     *      As well as also removing itself from all the Occasions where it has been used.
     *
     * @param identifier of the to be deleted OccasionTag
     */
    @Override
    void preDeleteCleanUp(String identifier) {
        OccasionTag toDelete = occasionTagRepo.findByIdentifier(identifier)
                .orElseThrow(() -> new IllegalArgumentException("No OccasionTag found to delete"));
        //remove from linked Person
        toDelete.getBelongsTo().removeOccasionTag(toDelete);
        //Cleaning all linked Occasions
        for (int i = 0; i < toDelete.getPresentIn().size(); i++) {
            Occasion occasion = toDelete.getPresentIn().get(i);
            occasionService.removeTagFromOccasion(occasion.getIdentifier(), toDelete.getIdentifier());
            i--;
        }
    }






    //----------------------Relationship Handeling Methods----------------------
    public Optional<OccasionTagDto> addOccasionTagFromPerson(String ownerID, Optional<OccasionTagDto> occasionTagDto) {
        Person parent = personRepo.findByIdentifier(ownerID)
                .orElseThrow(() -> new IllegalArgumentException("Persons ID has no according Person."));
        OccasionTag child = occasionTagRepo.findByIdentifier(occasionTagDto.get().getIdentifier()).get();
        parent.addOccasionTag(child);
        return Optional.of(child)
                .map(m -> convertModelIntoDTO(m));
    }

    public Optional<OccasionTagDto> removeOccasionTagFromPerson(String ownerID, Optional<OccasionTagDto> occasionTagDto) {
        Person parent = personRepo.findByIdentifier(ownerID)
                .orElseThrow(() -> new IllegalArgumentException("Persons ID has no according Person."));
        OccasionTag child = occasionTagRepo.findByIdentifier(occasionTagDto.get().getIdentifier()).get();
        parent.removeOccasionTag(child);
        occasionTagRepo.deleteByIdentifier(occasionTagDto.get().getIdentifier());
        return Optional
                .of(child)
                .map(this::convertModelIntoDTO);
    }

    public Optional<OccasionTagDto> addPresentInToOccassionTag(String tagID, String occasionID) {
        OccasionTag parent = occasionTagRepo.findByIdentifier(tagID)
                .orElseThrow(() -> new IllegalArgumentException("Tag ID has no according OccassionTag."));
        Occasion child = occasionRepo.findByIdentifier(occasionID)
                .orElseThrow(() -> new IllegalArgumentException("Occassion ID has no according Occassion."));
        parent.addOccasion(child);
        return Optional.of(parent)
                .map(m -> convertModelIntoDTO(m));
    }

    public Optional<OccasionTagDto> removePresentInFromOccassionTag(String tagID, String occasionID) {
        OccasionTag parent = occasionTagRepo.findByIdentifier(tagID)
                .orElseThrow(() -> new IllegalArgumentException("Tag ID has no according OccassionTag."));
        Occasion child = occasionRepo.findByIdentifier(occasionID)
                .orElseThrow(() -> new IllegalArgumentException("Occassion ID has no according Occassion."));
        parent.removeOccasion(child);
        return Optional
                .of(parent)
                .map(this::convertModelIntoDTO);
    }
}
