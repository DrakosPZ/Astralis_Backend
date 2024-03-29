package com.Astralis.backend.management.persistence;

import com.Astralis.backend.management.model.AbstractModel;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
public interface AbstractRepo<T extends AbstractModel> {

    /**
     * finds an Object by it's given identifier
     *
     * @param identifier of the searched for object
     * @return the searched for object
     */
    Optional<T> findByIdentifier(String identifier);

    /**
     * returns all objects of a table
     *
     * @return List of all objects in a table
     */
    List<T> findAll();


    /**
     * deletes an objects based on the given identifier
     *
     * @param identifier of the to be deleted Object.
     */
    void deleteByIdentifier(String identifier);

}