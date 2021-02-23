package com.Astralis.backend.gameDatabase.service;

import com.Astralis.backend.gameDatabase.model.Ship;
import com.Astralis.backend.gameDatabase.persistence.ShipRepo;
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
public class ShipService
        extends AbstractService<mShip, Ship> {

    private final ShipRepo shipRepo;

    @Override
    Ship setStandardData(Ship model) {
        //set Standard Data
        return model;
    }

    @Override
    void preDeleteCleanUp(long id) {

    }

    @Override
    mShip convertModelIntoDTO(Ship model) {
        return new mShip(model);
    }

    @Override
    Ship convertDTOIntoModel(mShip dto) {
        return new Ship(dto);
    }

    @Override
    Ship compareUpdate(Ship old, Ship model) {
        //Set simple Values
        return old;
    }

    @Override
    Ship storeListChanges(Ship old, mShip dto) {
        //TODO: Check for these model
        return old;
    }

    @Override
    List<Ship> findall() {
        return shipRepo.findAll();
    }

    @Override
    Ship saveRep(Ship model) {
        //clean Relations --
        return shipRepo.save(model);
    }

    @Override
    Ship updateRep(Ship model) {
        //clean Relations

        return shipRepo.save(model);
    }

    @Override
    Optional<Ship> findById(long id) {
        return shipRepo.findById(id);
    }

    @Override
    void deleteById(long id) {
        shipRepo.deleteById(id);
    }
}
