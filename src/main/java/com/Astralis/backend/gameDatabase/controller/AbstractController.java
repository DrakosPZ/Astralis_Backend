package com.Astralis.backend.gameDatabase.controller;

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
                        findAllModel()
                )
        );
    }


    /**
     * Get route to return a single object by it's id.
     *
     * @param id the looked for object's id.
     * @return a ResponseEntity of the DTO that is looked for.
     */
    @GetMapping(params = "id")
    public ResponseEntity<D> findById(@RequestParam long id)
    {
        Optional<D> find = findByIdModel(id);
        if(find.isEmpty())
        {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.of(
                findByIdModel(id)
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
        dto.setId(null);
        return ResponseEntity.ok(
                saveModel(Optional.of(dto))
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
        var updated = updateModel(Optional.of(dto));
        if(updated.isEmpty())
        {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(
                updated.orElseThrow(IllegalArgumentException::new)
        );
    }

    /**
     * Delete route to delete an object based on the given id
     *
     * @param id of the to be deleted object
     * @return the deleted object, encased in a ResponseEntity.
     */
    @DeleteMapping(params = "id")
    public Optional<D> delete(@RequestParam long id)
    {
        return deleteByIdModel(id);
    }

    abstract List<D> findAllModel();

    abstract Optional<D> findByIdModel(long id);

    abstract Optional<D> saveModel(Optional<D> optionaldto);

    abstract Optional<D> updateModel(Optional<D> optionaldto);

    abstract Optional<D> deleteByIdModel(long id);
}

