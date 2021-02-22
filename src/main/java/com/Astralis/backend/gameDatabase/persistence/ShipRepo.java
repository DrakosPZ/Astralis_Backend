package com.Astralis.backend.gameDatabase.persistence;

import com.Astralis.backend.accountManagement.model.LoginInformation;
import com.Astralis.backend.accountManagement.persistence.AbstractRepo;
import com.Astralis.backend.gameLogic.model.Country;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipRepo extends AbstractRepo<Country>, CrudRepository<Country, Long> {
}
