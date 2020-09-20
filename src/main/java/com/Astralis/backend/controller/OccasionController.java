package com.Astralis.backend.controller;

import com.Astralis.backend.service.OccasionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@RequestMapping(path = "/occasion")
public class OccasionController extends AbstractController<OccasionDto>{
    private final OccasionService service;

    /**
     * calls the find all method of the appropriate service.
     *
     * @return a List of DTO Objects.
     */
    @Override
    List<OccasionDto> findAllDTO() {
        return service.findAll();
    }

    /**
     * calls the findbyIdentifier method of the appropriate service.
     *
     * @return a DTO object.
     */
    @Override
    Optional<OccasionDto> findByIdentifierDTO(String identifier) {
        return service.findModelByIdentifier(identifier);
    }

    /**
     * calls the save method of the appropriate service.
     * Before that also checks if Username is already in use..
     *
     * @return the saved DTO object.
     */
    @Override
    Optional<OccasionDto> saveDTO(Optional<OccasionDto> optionaldto) {
        return service.save(optionaldto);
    }

    /**
     * calls the update method of the appropriate service.
     *
     * @return the updated DTO object.
     */
    @Override
    Optional<OccasionDto> updateDTO(Optional<OccasionDto> optionaldto) {
        return service.update(optionaldto);
    }

    /**
     * calls the deleteByIdentifier method of the appropriate service.
     *
     * @return the deleted DTO object.
     */
    @Override
    Optional<OccasionDto> deleteByIdentifierDTO(String identifier) {
        Optional<OccasionDto> optionaldto = service.findModelByIdentifier(identifier);
        service.deleteModelByIdentifier(identifier);
        return optionaldto;
    }









    //----------------------Custom Route Methods----------------------
    /**
     * Post route to add a already existing Tag to an Occasion
     *
     * @param holder holds the identifier of the parent object, and the child object to be added
     * @return the Occasion after the Tag has been added to an Occasion.
     */
    @PostMapping(path = "/addTagToOccasion", consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<OccasionDto> addToTeam(
            @RequestBody Map<String, String> holder) {
        String identifierOC = holder.keySet().stream()
                .map(key -> key)
                .collect(Collectors.toList()).get(0);
        String identifierOCT = holder.keySet().stream()
                .map(key -> holder.get(key))
                .collect(Collectors.toList()).get(0);
        return ResponseEntity.ok(
                service.addTagToOccasion(
                        identifierOC, identifierOCT)
                        .map(this::addSelfLink)
                        .orElseThrow(IllegalArgumentException::new)
        );
    }


    /**
     * Delete route to remove a Tag from an Occasion
     *
     * @param holder holds the identifier of the parent object, and the child object to be added
     * @return the Occasion after the Tag has been removed from an Occasion.
     */
    @DeleteMapping(path = "/removeTagToOccasion", consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<OccasionDto> removeFromTeam(
            @RequestBody Map<String, String> holder) {
        String identifierOC = holder.keySet().stream()
                .map(key -> key)
                .collect(Collectors.toList()).get(0);
        String identifierOCT = holder.keySet().stream()
                .map(key -> holder.get(key))
                .collect(Collectors.toList()).get(0);
        return ResponseEntity.ok(
                service.removeTagFromOccasion(
                        identifierOC, identifierOCT)
                        .map(this::addSelfLink)
                        .orElseThrow(IllegalArgumentException::new)
        );
    }


    /**
     * Post route to add a new Occasion to an existing Calender.
     * It assumes also that the Occasion is new and thereby creates it before adding it.
     *
     * @param holder holds the identifier of the parent object, and the child object to be added
     * @return the Occasion after it has been added to a Calendar.
     */
    @PostMapping(path = "/addToCalender", consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<OccasionDto> addToCalender(
            @RequestBody Map<String, OccasionDto> holder) {
        String identifier = holder.keySet().stream()
                .map(key -> key)
                .collect(Collectors.toList()).get(0);
        OccasionDto newOccasion = holder.keySet().stream()
                .map(key -> holder.get(key))
                .collect(Collectors.toList()).get(0);
        newOccasion = service.save(Optional.of(newOccasion)).get();
        return ResponseEntity.ok(
                service.addOccasionToCalender(
                        identifier,
                        Optional.of(newOccasion))
                        .map(this::addSelfLink)
                        .orElseThrow(IllegalArgumentException::new)
        );
    }


    /**
     * Delete route to remove a Recruit from a Team
     *
     * @param holder holds the identifier of the parent object, and the child object to be added
     * @return the Occasion after it has been removed from a Calendar.
     */
    @DeleteMapping(path = "/removeFromCalender", consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<OccasionDto> removeFromCalender(
            @RequestBody Map<String, String> holder) {
        String identifier = holder.keySet().stream()
                .map(key -> key)
                .collect(Collectors.toList()).get(0);
        OccasionDto child = holder.keySet().stream()
                .map(key -> service.findModelByIdentifier(holder.get(key)).get())
                .collect(Collectors.toList()).get(0);
        return ResponseEntity.ok(
                service.removeOccasionFromCalender(
                        identifier,
                        Optional.of(child))
                        .map(this::addSelfLink)
                        .orElseThrow(IllegalArgumentException::new)
        );
    }

}
