package com.Astralis.backend.controller;

import com.Astralis.backend.dto.OccasionTagDto;
import com.Astralis.backend.service.OccasionTagService;
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
@RequestMapping(path = "/occasionTag")
public class OccasionTagController extends AbstractController<OccasionTagDto>{
    private final OccasionTagService service;

    /**
     * calls the find all method of the appropriate service.
     *
     * @return a List of DTO Objects.
     */
    @Override
    List<OccasionTagDto> findAllDTO() {
        return service.findAll();
    }

    /**
     * calls the findbyIdentifier method of the appropriate service.
     *
     * @return a DTO object.
     */
    @Override
    Optional<OccasionTagDto> findByIdentifierDTO(String identifier) {
        return service.findModelByIdentifier(identifier);
    }

    /**
     * calls the save method of the appropriate service.
     *
     * @return the saved DTO object.
     */
    @Override
    Optional<OccasionTagDto> saveDTO(Optional<OccasionTagDto> optionaldto) {
        return service.save(optionaldto);
    }

    /**
     * calls the update method of the appropriate service.
     *
     * @return the updated DTO object.
     */
    @Override
    Optional<OccasionTagDto> updateDTO(Optional<OccasionTagDto> optionaldto) {
        return service.update(optionaldto);
    }

    /**
     * calls the deleteByIdentifier method of the appropriate service.
     *
     * @return the deleted DTO object.
     */
    @Override
    Optional<OccasionTagDto> deleteByIdentifierDTO(String identifier) {
        Optional<OccasionTagDto> optionaldto = service.findModelByIdentifier(identifier);
        service.deleteModelByIdentifier(identifier);
        return optionaldto;
    }









    //----------------------Custom Route Methods----------------------
    /**
     * Post route to add a new OccasionTag to a OccasionTagsList and also create it
     *
     * @param holder holds the identifier of the parent object, and the child object to be added
     * @return the OccasionTagDTO in a ResponseEntity.
     */
    @PostMapping(path = "/addOccasionTagToPerson", consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<OccasionTagDto> addOccasionTag(
            @RequestBody Map<String, OccasionTagDto> holder) {
        String identifier = holder.keySet().stream()
                .map(key -> key)
                .collect(Collectors.toList()).get(0);
        OccasionTagDto newOccasionTag = holder.keySet().stream()
                .map(key -> holder.get(key))
                .collect(Collectors.toList()).get(0);
        newOccasionTag = service.save(Optional.of(newOccasionTag)).get();
        return ResponseEntity.ok(
                service.addOccasionTagFromPerson(
                        identifier,
                        Optional.of(newOccasionTag))
                        .map(this::addSelfLink)
                        .orElseThrow(IllegalArgumentException::new)
        );
    }

    /**
     * Delete route to remove a OccasionTag from Persons list and also removes the object
     *
     * @param holder holds the identifier of the parent object, and the child object to be added
     * @return the OccasionTagDTO in a ResponseEntity.
     */
    @DeleteMapping(path = "/removeOccasionTagFromPerson", consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<OccasionTagDto> removeFromTeam(
            @RequestBody Map<String, String> holder) {
        String identifier = holder.keySet().stream()
                .map(key -> key)
                .collect(Collectors.toList()).get(0);
        OccasionTagDto child = holder.keySet().stream()
                .map(key -> service.findModelByIdentifier(holder.get(key)).get())
                .collect(Collectors.toList()).get(0);
        return ResponseEntity.ok(
                service.removeOccasionTagFromPerson(
                        identifier,
                        Optional.of(child))
                        .map(this::addSelfLink)
                        .orElseThrow(IllegalArgumentException::new)
        );
    }

}
