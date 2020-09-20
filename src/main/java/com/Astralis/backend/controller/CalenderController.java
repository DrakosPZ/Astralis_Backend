package com.Astralis.backend.controller;

import com.Astralis.backend.dto.CalenderDto;
import com.Astralis.backend.service.CalenderService;
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
@RequestMapping(path = "/calender")
public class CalenderController extends AbstractController<CalenderDto>{
    private final CalenderService service;

    /**
     * calls the find all method of the appropriate service.
     *
     * @return a List of DTO Objects.
     */
    @Override
    List<CalenderDto> findAllDTO() {
        return service.findAll();
    }

    /**
     * calls the findbyIdentifier method of the appropriate service.
     *
     * @return a DTO object.
     */
    @Override
    Optional<CalenderDto> findByIdentifierDTO(String identifier) {
        return service.findModelByIdentifier(identifier);
    }

    /**
     * calls the save method of the appropriate service.
     *
     * @return the saved DTO object.
     */
    @Override
    Optional<CalenderDto> saveDTO(Optional<CalenderDto> optionaldto) {
        return service.save(optionaldto);
    }

    /**
     * calls the update method of the appropriate service.
     *
     * @return the updated DTO object.
     */
    @Override
    Optional<CalenderDto> updateDTO(Optional<CalenderDto> optionaldto) {
        return service.update(optionaldto);
    }

    /**
     * calls the deleteByIdentifier method of the appropriate service.
     *
     * @return the deleted DTO object.
     */
    @Override
    Optional<CalenderDto> deleteByIdentifierDTO(String identifier) {
        Optional<CalenderDto> optionaldto = service.findModelByIdentifier(identifier);
        service.deleteModelByIdentifier(identifier);
        return optionaldto;
    }









    //----------------------Custom Route Methods----------------------
    /**
     * Put route to update the filter Command Field of a given (identifier) calendar
     *
     * @param holder holds the identifier of the parent object, and the child object to be added
     * @return the updated CalendarDTO encased in a ResponseEntity.
     */
    @PutMapping(path = "/updateFilter", consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<CalenderDto> updateFilter(
            @RequestBody Map<String, String> holder) {
        String identifier = holder.keySet().stream()
                .map(key -> key)
                .collect(Collectors.toList()).get(0);
        String filtercommand = holder.keySet().stream()
                .map(key -> holder.get(key))
                .collect(Collectors.toList()).get(0);
        return ResponseEntity.ok(
                service.updateFilterCommand(
                        identifier, filtercommand)
                        .map(this::addSelfLink)
                        .orElseThrow(IllegalArgumentException::new)
        );
    }


    /**
     * Post route to add a new Calender to an existing Person
     *
     * @param holder holds the identifier of the parent object, and the child object to be added
     * @return the CalenderDTO after being added to a Person.
     */
    @PostMapping(path = "/addToPerson", consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<CalenderDto> addToPerson(
            @RequestBody Map<String, CalenderDto> holder) {
        String identifier = holder.keySet().stream()
                .map(key -> key)
                .collect(Collectors.toList()).get(0);
        CalenderDto newCalender = holder.keySet().stream()
                .map(key -> holder.get(key))
                .collect(Collectors.toList()).get(0);
        newCalender = service.save(Optional.of(newCalender)).get();
        return ResponseEntity.ok(
                service.addCalenderToPerson(
                        identifier,
                        Optional.of(newCalender))
                        .map(this::addSelfLink)
                        .orElseThrow(IllegalArgumentException::new)
        );
    }


    /**
     * Delete route to remove a Calender from a Person
     *
     * @param holder holds the identifier of the parent object, and the child object to be added
     * @return the CalenderDTO after being removed from a Person.
     */
    @DeleteMapping(path = "/removeFromPerson", consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<CalenderDto> removeFromTeam(
            @RequestBody Map<String, String> holder) {
        String identifier = holder.keySet().stream()
                .map(key -> key)
                .collect(Collectors.toList()).get(0);
        CalenderDto child = holder.keySet().stream()
                .map(key -> service.findModelByIdentifier(holder.get(key)).get())
                .collect(Collectors.toList()).get(0);
        return ResponseEntity.ok(
                service.removeCalenderToPerson(
                        identifier,
                        Optional.of(child))
                        .map(this::addSelfLink)
                        .orElseThrow(IllegalArgumentException::new)
        );
    }


}
