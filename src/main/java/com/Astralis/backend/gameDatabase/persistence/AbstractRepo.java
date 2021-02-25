package com.Astralis.backend.gameDatabase.persistence;

import com.Astralis.backend.accountManagement.model.AbstractModel;
import com.Astralis.backend.gameDatabase.model.AbstractGameModel;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
public interface AbstractRepo<T extends AbstractGameModel> {

    /**
     * returns all objects of a table
     *
     * @return List of all objects in a table
     */
    List<T> findAll();

}