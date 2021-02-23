package com.Astralis.backend.gameDatabase.persistence;

import com.Astralis.backend.gameDatabase.persistence.AbstractRepo;
import com.Astralis.backend.gameDatabase.model.Ship;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipRepo extends AbstractRepo<Ship>, CrudRepository<Ship, Long> {
}
