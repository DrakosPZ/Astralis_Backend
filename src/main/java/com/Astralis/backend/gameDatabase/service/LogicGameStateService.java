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
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class LogicGameStateService
        extends AbstractService<mLogicGameState, LogicGameState> {

    private final LogicGameStateRepo logicGameStateRepo;

    @Override
    LogicGameState setStandardData(LogicGameState model) {
        //set Standard Data
        return model;
    }

    @Override
    void preDeleteCleanUp(long id) {
    }

    @Override
    mLogicGameState convertModelIntoDTO(LogicGameState model) {
        return new mLogicGameState(model);
    }

    @Override
    LogicGameState convertDTOIntoModel(mLogicGameState dto) {
        return new LogicGameState(dto);
    }

    @Override
    LogicGameState compareUpdate(LogicGameState old, LogicGameState model) {
        //Set simple Values
        return old;
    }

    @Override
    LogicGameState storeListChanges(LogicGameState old, mLogicGameState dto) {
        //TODO: Check for these model
        return old;
    }

    @Override
    List<LogicGameState> findall() {
        return logicGameStateRepo.findAll();
    }

    @Override
    LogicGameState saveRep(LogicGameState model) {
        //clean Relations --
        return logicGameStateRepo.save(model);
    }

    @Override
    LogicGameState updateRep(LogicGameState model) {
        //clean Relations
        return logicGameStateRepo.save(model);
    }

    @Override
    Optional<LogicGameState> findById(long id) {
        return logicGameStateRepo.findById(id);
    }

    @Override
    void deleteById(long id) {
        logicGameStateRepo.deleteById(id);
    }
}
