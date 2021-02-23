package com.Astralis.backend.gameDatabase.persistence;

import com.Astralis.backend.gameDatabase.persistence.AbstractRepo;
import com.Astralis.backend.gameDatabase.model.Country;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepo extends AbstractRepo<Country>, CrudRepository<Country, Long> {
}
