package com.Astralis.backend.persistence;

import com.Astralis.backend.model.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepo extends AbstractRepo<Person>, CrudRepository<Person, Long> {
    /**
     * Finds a Person in the table with the given username
     *
     * @param identifier the looked for Username
     * @return the looked for Person.
     */
    Optional<Person> findByUsername(String identifier);
}