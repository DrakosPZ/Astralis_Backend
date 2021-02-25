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
                removeCountryFromLogicGameState(model.getId(), country.getId());
                newListCountry.remove(country);
            } else if(newListCountry.contains(country)) {
                newListCountry.remove(country);
            }
        }
        //TODO: ADD DOWNWARD SAVING BEFORE THIS POINT
        newListCountry.forEach(country -> addCountryToLogicGameState(model.getId(), Optional.of(new mCountry(country))));

        //TODO: Check for GameState Relations
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
        model.setCountries(new ArrayList<>());

        return logicGameStateRepo.save(model);
    }

    //#TODO: Add Documentation
    @Override
    LogicGameState updateRep(LogicGameState model) {
        //clean Relations
        model.setCountries(new ArrayList<>());

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
