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

    //#TODO: Add Documentation
    @Override
    Ship setStandardData(Ship model) {
        //set Standard Data
        return model;
    }

    //#TODO: Add Documentation
    @Override
    void preDeleteCleanUp(long id) {

    }

    //#TODO: Add Documentation
    @Override
    mShip convertModelIntoDTO(Ship model) {
        return new mShip(model);
    }

    //#TODO: Add Documentation
    @Override
    Ship convertDTOIntoModel(mShip dto) {
        return new Ship(dto);
    }

    //#TODO: Add Documentation
    @Override
    Ship cleanRelations(Ship model) {
        return model;
    }


    /**
     * Step 1. update simple fields
     * Compares the old to the new Ship model.
     * If a field has changed, the old model's field is updated to the new version.
     * The old model is then returned with the updated fields.
     *
     * @param old Ship model
     * @param model new Ship model
     * @return the old Ship model with the updated fields.
     */
    @Override
    Ship compareUpdate(Ship old, Ship model) {
        if(!old.getTargetPosition().equals(model.getTargetPosition())){
            old.getTargetPosition().setX(model.getTargetPosition().getX());
            old.getTargetPosition().setY(model.getTargetPosition().getY());
        }
        if(!old.getCurrentPosition().equals(model.getCurrentPosition())){
            old.getCurrentPosition().setX(model.getCurrentPosition().getX());
            old.getCurrentPosition().setY(model.getCurrentPosition().getY());
        }
        if(old.getMovementSpeed() != model.getMovementSpeed()){
            old.setMovementSpeed(model.getMovementSpeed());
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
     * @param old Ship model.
     * @param dto new Ship dto.
     * @return the updated model from the database.
     */
    @Override
    Ship storeListChanges(Ship old, mShip dto) {
        return old;
    }

    //#TODO: Add Documentation
    @Override
    List<Ship> findall() {
        return shipRepo.findAll();
    }

    //#TODO: Add Documentation
    @Override
    Ship saveRep(Ship model) {
        //clean Relations --
        model = cleanRelations(model);
        return shipRepo.save(model);
    }

    //#TODO: Add Documentation
    @Override
    Ship updateRep(Ship model) {
        //clean Relations
        model = cleanRelations(model);
        return shipRepo.save(model);
    }

    //#TODO: Add Documentation
    @Override
    Optional<Ship> findById(long id) {
        return shipRepo.findById(id);
    }

    //#TODO: Add Documentation
    @Override
    void deleteById(long id) {
        shipRepo.deleteById(id);
    }
}
