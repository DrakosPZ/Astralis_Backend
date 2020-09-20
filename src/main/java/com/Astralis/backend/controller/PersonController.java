package com.Astralis.backend.controller;

import com.Astralis.backend.service.OccasionTagService;
import com.Astralis.backend.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@RequestMapping(path = "/person")
public class PersonController extends AbstractController<PersonDto>{
    private final PersonService service;
    private final OccasionTagService serviceOT;

    /**
     * calls the find all method of the appropriate service.
     *
     * @return a List of DTO Objects.
     */
    @Override
    List<PersonDto> findAllDTO() {
        return service.findAll();
    }

    /**
     * calls the findbyIdentifier method of the appropriate service.
     *
     * @return a DTO object.
     */
    @Override
    Optional<PersonDto> findByIdentifierDTO(String identifier) {
        return service.findModelByIdentifier(identifier);
    }

    /**
     * calls the save method of the appropriate service.
     * Before that also checks if Username is already in use.
     *
     * @return the saved DTO object.
     */
    @Override
    Optional<PersonDto> saveDTO(Optional<PersonDto> optionaldto) {
        if(!service.findByUsername(optionaldto
                .orElseThrow(() -> new IllegalArgumentException("given Person is empty"))
                .getUsername()).isEmpty()){
            throw new IllegalArgumentException("Username is already used");
        }
        return service.save(optionaldto);
    }

    /**
     * calls the update method of the appropriate service.
     *
     * @return the updated DTO object.
     */
    @Override
    Optional<PersonDto> updateDTO(Optional<PersonDto> optionaldto) {
        return service.update(optionaldto);
    }

    /**
     * calls the deleteByIdentifier method of the appropriate service.
     *
     * @return the deleted DTO object.
     */
    @Override
    Optional<PersonDto> deleteByIdentifierDTO(String identifier) {
        Optional<PersonDto> optionaldto = service.findModelByIdentifier(identifier);
        if(!optionaldto.get().getMemberOfID().isEmpty()){
            service.removePersonFromTeam(optionaldto.get().getMemberOfID(),optionaldto);
        }
        service.deleteModelByIdentifier(identifier);
        return optionaldto;
    }









    //----------------------Custom Route Methods----------------------
    /**
     * Post route to add a new Recruit to an existing Team.
     *
     * @param holder holds the identifier of the parent object, and the child object to be added.
     * @return the parent Person with the added Person.
     */
    @PostMapping(path = "/addToTeam", consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<PersonDto> addToTeam(
            @RequestBody Map<String, PersonDto> holder) {
        String identifier = holder.keySet().stream()
                .map(key -> key)
                .collect(Collectors.toList()).get(0);
        PersonDto newRecruit = holder.keySet().stream()
                .map(key -> holder.get(key))
                .collect(Collectors.toList()).get(0);
        newRecruit = service.save(Optional.of(newRecruit)).get();
        return ResponseEntity.ok(
                service.addPersonToTeam(
                        identifier,
                            Optional.of(newRecruit))
                        .map(this::addSelfLink)
                        .orElseThrow(IllegalArgumentException::new)
        );
    }


    /**
     * Delete route to remove a Recruit from a Team
     *
     * @param holder holds the identifier of the parent object, and the child object to be added
     * @return the parent Person without the removed Person.
     */
    @DeleteMapping(path = "/removeFromTeam", consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<PersonDto> removeFromTeam(
            @RequestBody Map<String, String> holder) {
        String identifier = holder.keySet().stream()
                .map(key -> key)
                .collect(Collectors.toList()).get(0);
        PersonDto child = holder.keySet().stream()
                .map(key -> service.findModelByIdentifier(holder.get(key)).get())
                .collect(Collectors.toList()).get(0);
        return ResponseEntity.ok(
                service.removePersonFromTeam(
                        identifier,
                        Optional.of(child))
                        .map(this::addSelfLink)
                        .orElseThrow(IllegalArgumentException::new)
        );
    }

    /**
     * Post route to return a loggedin Account,
     * out of security also double checks for the right password.
     *
     * @param holder a has Map with the username and password.
     * @return the to the username and password according Person DTO.
     */
    @PostMapping(path = "/afterLogin", consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<PersonDto> findByUsernameAndPassword(@RequestBody Map<String, String> holder ){
        String username = holder.keySet().stream()
                .map(key -> key)
                .collect(Collectors.toList()).get(0);
        String password = holder.keySet().stream()
                .map(key -> holder.get(key))
                .collect(Collectors.toList()).get(0);
        Optional<PersonDto> find = service.findByUsernamePassword(username, password);
        if(find.isEmpty())
        {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.of(
                find.map(this::addSelfLink)
        );
    }

    /**
     * A route made to find a PersonDTO based on their username.
     *
     * @param username the looked for Person's username.
     * @return the looked for Person in a ResponseEntity.
     */
    @GetMapping(params = "username")
    public ResponseEntity<PersonDto> findByUsername(
            @RequestParam String username
    ) {
        Optional<PersonDto> find = service.findByUsername(username);
        if(find.isEmpty())
        {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.of(
                find.map(this::addSelfLink)
        );
    }



    /**
     * Get route to get the calendar owner's parent parent, for the possible Origins
     *
     * @param identifier of the calendar in question
     * @return a Person DTO under which all the possible origins are.
     */
    @GetMapping(path = "/possibleOrigins", params = "identifier", consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<PersonDto> possibleOrigins(@RequestParam String identifier) {
        return ResponseEntity.ok(
                service.getPossibleOrigins(identifier,2)
                        .map(this::addSelfLink)
                        .orElseThrow(IllegalArgumentException::new)
        );
    }
}
