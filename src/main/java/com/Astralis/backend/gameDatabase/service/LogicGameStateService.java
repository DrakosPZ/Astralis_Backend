package com.Astralis.backend.gameDatabase.service;

import com.Astralis.backend.gameDatabase.model.Country;
import com.Astralis.backend.gameDatabase.model.LogicGameState;
import com.Astralis.backend.gameDatabase.persistence.CountryRepo;
import com.Astralis.backend.gameDatabase.persistence.LogicGameStateRepo;
import com.Astralis.backend.gameLogic.model.mCountry;
import com.Astralis.backend.gameLogic.model.mLogicGameState;
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
public class LogicGameStateService
        extends AbstractService<mLogicGameState, LogicGameState> {

    private final CountryService countryService;

    private final LogicGameStateRepo logicGameStateRepo;
    private final CountryRepo countryRepo;

    //#TODO: Add Documentation
    @Override
    LogicGameState setStandardData(LogicGameState model) {
        //set Standard Data
        return model;
    }

    //#TODO: Add Documentation
    @Override
    void preDeleteCleanUp(long id) {
    }

    //#TODO: Add Documentation
    @Override
    mLogicGameState convertModelIntoDTO(LogicGameState model) {
        return new mLogicGameState(model);
    }

    //#TODO: Add Documentation
    @Override
    LogicGameState convertDTOIntoModel(mLogicGameState dto) {
        return new LogicGameState(dto);
    }

    //#TODO: Add Documentation
    @Override
    LogicGameState cleanRelations(LogicGameState model) {
        model.setCountries(new ArrayList<>());
        return model;
    }


    /**
     * Step 1. update simple fields
     * Compares the old to the new LogicGameState model.
     * If a field has changed, the old model's field is updated to the new version.
     * The old model is then returned with the updated fields.
     *
     * @param old LogicGameState model
     * @param model new LogicGameState model
     * @return the old LogicGameState model with the updated fields.
     */
    @Override
    LogicGameState compareUpdate(LogicGameState old, LogicGameState model) {
        if(old.getYear() != model.getYear()){
            old.setYear(model.getYear());
        }
        if(old.getMonth() != model.getMonth()){
            old.setMonth(model.getMonth());
        }
        if(old.getDay() != model.getDay()){
            old.setDay(model.getDay());
        }
        if(old.getHour() != model.getHour()){
            old.setHour(model.getHour());
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
     * @param old LogicGameState model.
     * @param dto new LogicGameState dto.
     * @return the updated model from the database.
     */
    @Override
    LogicGameState storeListChanges(LogicGameState old, mLogicGameState dto) {
        //for those that are fully loaded by the converter
        LogicGameState model = convertDTOIntoModel(dto);
        //Countries
        List<Country> newListCountry = model.getCountries();
        List<Country> oldListCountry = old.getCountries();
        for (int index = 0; index < oldListCountry.size(); index++) {
            Country country = oldListCountry.get(index);
            if(!newListCountry.contains(country)){
                //1. Removed from List
                //#TODO: add to Garbage Collector country;
                removeCountryFromLogicGameState(model.getId(), country.getId());
                newListCountry.remove(country);
            } else if(newListCountry.contains(country)) {
                //unchanged doesn't have to be added. only covers completely unchanged objects
                newListCountry.remove(country);
            }
        }
        //added to Logic Gamestate
        newListCountry.forEach(country -> {
            //TODO: Check if safe is needed
            //2.New In List and Object & changes to be updated
            country = countryService.downwardSave(Optional.of(new mCountry(country))).get();
            //4.Change in Object in List
            boolean alreadyPresent = false;
            //#TODO: Check if already present in List
            /*List.foreach(Country c => {
                if(c.getId() == country.getId())
                    alreadyPresent = true;
                    //TODO Find out how to call of Streams
            })*/
            if(alreadyPresent){
                removeCountryFromLogicGameState(model.getId(), country.getId());
            }
            //4., 2. & 3.New In List, not new Object => Just Adding whatever is left at the end
            addCountryToLogicGameState(model.getId(), Optional.of(new mCountry(country)));
        });

        return old;
    }

    //#TODO: Add Documentation
    @Override
    List<LogicGameState> findall() {
        return logicGameStateRepo.findAll();
    }

    //#TODO: Add Documentation
    @Override
    LogicGameState saveRep(LogicGameState model) {
        //clean Relations
        model = cleanRelations(model);
        return logicGameStateRepo.save(model);
    }

    //#TODO: Add Documentation
    @Override
    LogicGameState updateRep(LogicGameState model) {
        //clean Relations
        model = cleanRelations(model);
        return logicGameStateRepo.save(model);
    }

    //#TODO: Add Documentation
    @Override
    Optional<LogicGameState> findById(long id) {
        return logicGameStateRepo.findById(id);
    }

    //#TODO: Add Documentation
    @Override
    void deleteById(long id) {
        logicGameStateRepo.deleteById(id);
    }









    //----------------------Relationship Handling Methods----------------------
    public Optional<mLogicGameState> addCountryToLogicGameState(long logicGameStateID, Optional<mCountry> mCountry) {
        LogicGameState parent = logicGameStateRepo.findById(logicGameStateID).orElseThrow(() -> new IllegalArgumentException("LogicGameState ID has no according LogicGameState."));
        Country child = countryRepo.findById(mCountry.get().getId()).orElse(
                //possibility of the given Country not yet being implemented
                child = countryRepo.save(new Country(mCountry.get()))
        );
        parent.addCountry(child);
        return Optional.of(parent)
                .map(m -> convertModelIntoDTO(m));
    }

    public Optional<mLogicGameState> removeCountryFromLogicGameState(long logicGameStateID, long countryID) {
        LogicGameState parent = logicGameStateRepo.findById(logicGameStateID).orElseThrow(() -> new IllegalArgumentException("LogicGameState ID has no according LogicGameState."));
        Country child = countryRepo.findById(countryID).orElseThrow(() -> new IllegalArgumentException("Country ID has no according Country."));
        parent.removeCountry(child);
        return Optional
                .of(parent)
                .map(this::convertModelIntoDTO);
    }

}
