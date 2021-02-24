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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CountryService
        extends AbstractService<mCountry, Country> {

    private final CountryRepo countryRepo;

    //#TODO: Add Documentation
    @Override
    Country setStandardData(Country model) {
        //set Standard Data
        return model;
    }

    //#TODO: Add Documentation
    @Override
    void preDeleteCleanUp(long id) {
    }

    //#TODO: Add Documentation
    @Override
    mCountry convertModelIntoDTO(Country model) {
        return new mCountry(model);
    }

    //#TODO: Add Documentation
    @Override
    Country convertDTOIntoModel(mCountry dto) {
        return new Country(dto);
    }

    /**
     * Step 1. update simple fields
     * Compares the old to the new Country model.
     * If a field has changed, the old model's field is updated to the new version.
     * The old model is then returned with the updated fields.
     *
     * @param old Country model
     * @param model new Country model
     * @return the old Country model with the updated fields.
     */
    @Override
    Country compareUpdate(Country old, Country model) {
        if(!old.getName().equals(model.getName())){
            old.setName(model.getName());
        }
        if(!old.getColour().equals(model.getColour())){
            old.setColour(model.getColour());
        }
        if(!old.getShip().equals(model.getShip())){
            old.setShip(model.getShip());
        }
        return old;
    }

    /**
     * Step 2. update relationship fields
     * Transforms the DTO into a new Model.
     * Compares every single relationship list of the old and new model.
     * If an element of the old list is not contained by the new list,
     *      the remove relationship method is called,
     *      as well as it's also removed from the new list.
     * If the element is present in the new list, it is removed from the new list.
     *
     * Finally all remaining elements of the new list are added according relationship method.
     *
     * As every got model at this point is a hibernate instance the changes are edited on the database.
     * So to commit the changes the model is called from the database.
     * The returned model is handed forward.
     *
     * @param old Country model.
     * @param dto new Country dto.
     * @return the updated model from the database.
     */
    @Override
    Country storeListChanges(Country old, mCountry dto) {
        //TODO: Check for these model
        return old;
    }

    //#TODO: Add Documentation
    @Override
    List<Country> findall() {
        return countryRepo.findAll();
    }

    //#TODO: Add Documentation
    @Override
    Country saveRep(Country model) {
        //clean Relations
        return countryRepo.save(model);
    }

    //#TODO: Add Documentation
    @Override
    Country updateRep(Country model) {
        //clean Relations
        return countryRepo.save(model);
    }

    //#TODO: Add Documentation
    @Override
    Optional<Country> findById(long id) {
        return countryRepo.findById(id);
    }

    //#TODO: Add Documentation
    @Override
    void deleteById(long id) {
        countryRepo.deleteById(id);
    }
}
