package com.Astralis.backend.gameDatabase.service;

import com.Astralis.backend.gameDatabase.model.Country;
import com.Astralis.backend.gameDatabase.model.Ship;
import com.Astralis.backend.gameDatabase.persistence.CountryRepo;
import com.Astralis.backend.gameDatabase.persistence.ShipRepo;
import com.Astralis.backend.gameLogic.model.mCountry;
import com.Astralis.backend.gameLogic.model.mShip;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CountryService
        extends AbstractService<mCountry, Country> {

    private final CountryRepo countryRepo;

    @Override
    Country setStandardData(Country model) {
        //set Standard Data
        return model;
    }

    @Override
    void preDeleteCleanUp(long id) {
    }

    @Override
    mCountry convertModelIntoDTO(Country model) {
        return new mCountry(model);
    }

    @Override
    Country convertDTOIntoModel(mCountry dto) {
        return new Country(dto);
    }

    @Override
    Country compareUpdate(Country old, Country model) {
        //Set simple Values
        return old;
    }

    @Override
    Country storeListChanges(Country old, mCountry dto) {
        //TODO: Check for these model
        return old;
    }

    @Override
    List<Country> findall() {
        return countryRepo.findAll();
    }

    @Override
    Country saveRep(Country model) {
        //clean Relations --
        return countryRepo.save(model);
    }

    @Override
    Country updateRep(Country model) {
        //clean Relations
        return countryRepo.save(model);
    }

    @Override
    Optional<Country> findById(long id) {
        return countryRepo.findById(id);
    }

    @Override
    void deleteById(long id) {
        countryRepo.deleteById(id);
    }
}
