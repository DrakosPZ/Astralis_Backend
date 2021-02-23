package com.Astralis.backend.gameDatabase.controller;

import com.Astralis.backend.accountManagement.dto.AbstractModelDto;
import com.Astralis.backend.gameLogic.model.AbstractMemoryModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@CrossOrigin("http://localhost:4200")
public abstract class AbstractController<D extends AbstractMemoryModel> {

    /**
     * Get route to return all Objects of a table.
     *
     * @return a ResponseEntity List of DTOs out of the table.
     */
    @GetMapping
    public ResponseEntity<CollectionModel<D>> findAll() {
        return ResponseEntity.ok(
                new CollectionModel<>(
                        findAllDTO()
                                .stream()
                                .map(this::addSelfLink)
                                .collect(Collectors.toList())
                )
        );
    }


    /**
     * Get route to return a single object by it's identifier.
     *
     * @param identifier the looked for object's identifier.
     * @return a ResponseEntity of the DTO that is looked for.
     */
    @GetMapping(params = "identifier")
    public ResponseEntity<D> findByIdentifier(@RequestParam String identifier)
    {
        Optional<D> find = findByIdentifierDTO(identifier);
        if(find.isEmpty())
        {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.of(
                findByIdentifierDTO(identifier)
                        .map(this::addSelfLink)
        );
    }

    /**
     * Post route to create a new object based on the given object
     *
     * @param dto The to be created object.
     * @return the newly created object, encased in a ResponseEntity.
     */
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<D> create(
            @RequestBody D dto) {
        dto.setIdentifier(null);
        return ResponseEntity.ok(
                saveDTO(Optional.of(dto))
                        .map(this::addSelfLink)
                        .orElseThrow(IllegalArgumentException::new)
        );
    }


    /**
     * Put route to update a new object based on the given object.
     *
     * @param dto the to be updated object.
     * @return the newly updated object, encased in a ResponseEntity.
     */
    @PutMapping(consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<D> update(
            @RequestBody D dto) {
        var updated = updateDTO(Optional.of(dto));
        if(updated.isEmpty())
        {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(
                updated.map(this::addSelfLink)
                        .orElseThrow(IllegalArgumentException::new)
        );
    }

    /**
     * Delete route to delete an object based on the given identifier
     *
     * @param identifier of the to be deleted object
     * @return the deleted object, encased in a ResponseEntity.
     */
    @DeleteMapping(params = "identifier")
    public Optional<D> delete(@RequestParam String identifier)
    {
        return deleteByIdentifierDTO(identifier);
    }

    /**
     * This method is supposed to add a self Link to every response Entity.
     * It is currently not in use, as the frontend isn't constructed to work with it.
     *
     * @param dto the to be referenced DTO.
     * @return the DTO with the self reference.
     */
    D addSelfLink(D dto) {
        /*dto.add(
                linkTo(
                        ControllerLinkBuilder.methodOn(this.getClass())
                                .findByIdentifier(dto.getIdentifier())
                ).withSelfRel()
        );*/
        return dto;
    }

    abstract List<D> findAllDTO();

    abstract Optional<D> findByIdentifierDTO(String identifier);

    abstract Optional<D> saveDTO(Optional<D> optionaldto);

    abstract Optional<D> updateDTO(Optional<D> optionaldto);

    abstract Optional<D> deleteByIdentifierDTO(String identifier);
}

