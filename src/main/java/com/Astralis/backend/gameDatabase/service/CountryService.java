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
    private final ShipService shipService;

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

    //#TODO: Add Documentation
    @Override
    Country cleanRelations(Country model) {
        model.setShip(null);
        model.setLogicGameState(null);
        return model;
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
     * In Parallel it has to be checked with every object of the lists if
     *      1. it is completely removed from the list => object is removed and thrown into the garbage collector
     *      2. it is new in the list and a new object => object is created and stored in the DB and added to the list
     *      3. it is new in the list, but not a new object => object is retrieved from the DB, changes adjusted
     *          and added to the List the Garbage Collector has to be called to check if it would be removed otherwise
     *      4. the object is already in the List and is just being changed => object is called from the DB and changed
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
        //Ship
        //1.Remove From List
        if(dto.getMShip() != null){
            Ship updatedShip = new Ship(dto.getMShip());
            //2.New In List and Object or a Changed Object to be updated
            updatedShip = shipService.downwardSave(Optional.of(new mShip(updatedShip))).get();

            //3.New In List, not new Object & 4.Change in Object in List
            if(old.getShip() == null || !old.getShip().equals(updatedShip)){
                //#TODO: Change the got Object from the Database to what has been given over the DAO
                //#TODO: Add to added Garbage Collector List so it isn't deleted later
                old.setShip(updatedShip);
            }
        } else {
            //#TODO: add to Garbage Collector old.getShip();
            old.setShip(null);
        }
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
        model = cleanRelations(model);
        return countryRepo.save(model);
    }

    //#TODO: Add Documentation
    @Override
    Country updateRep(Country model) {
        //clean Relations
        model = cleanRelations(model);
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
