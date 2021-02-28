package com.Astralis.backend.gameDatabase.service;

import com.Astralis.backend.gameDatabase.model.AbstractGameModel;
import com.Astralis.backend.gameDatabase.model.Country;
import com.Astralis.backend.gameLogic.model.AbstractMemoryModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public abstract class AbstractService
        <D extends AbstractMemoryModel, M extends AbstractGameModel> {

    /**
     * Returns a List of all Objects in a table.
     * @return List of all objects in the table.
     */
    public List<D> findAll(){
        return findall()
                .stream()
                .map(item -> convertModelIntoDTO(item))
                .collect(Collectors.toList());
    }

    /**
     * Returns a searched for object by the given id.
     *
     * @param id of the to find object
     * @return the looked for object
     */
    public Optional<D> findModelById(long id) {
        return findById(id)
                .map(m -> convertModelIntoDTO(m));
    }

    /**
     * Saves a given object to the database and returns it afterwards.
     * Before saving standard fields and relations are set/ created.
     *
     * @param dto new object
     * @return the newly saved object
     */
    public Optional<D> save(Optional<D> dto) {
        M model = dto.map(d -> convertDTOIntoModel(d)).get();
        //possibly set Standard data if wanted
        model = setStandardData(model);
        //use save Rep to store only field methods (currently practically upadeteRep, because otherwhise duplicates are
        // generated on relation fields, which I don't know yet on how to solve)
        M saved = saveRep(model);
        D listModel = dto.get();
        listModel.setId(saved.getId());
        saved = storeListChanges(
                saved, listModel
        );
        return Optional.of(saved)
                .map(m -> convertModelIntoDTO(m));

    }

    /**
     * Updates a given object to the database and returns it afterwards.
     * First it gets the old object in the table.
     * Second it replaces the updated fields on the old object with the new ones.
     * These are then stored.
     * Third all changes made to relations are updated on the old object.
     *
     * The fully corrected old object is then returned.
     *
     * @param dto the updated object.
     * @return the newly updated object.
     */
    public Optional<D> update (Optional<D> dto) {
        Optional<M> old = findById(dto.get().getId());
        if(old.isEmpty())
        {
            return Optional.empty();
        }
        M model = dto.map(d -> convertDTOIntoModel(d)).get();
        M toSave = compareUpdate(old.get(), model);

        //store changed Fields
        M saved = updateRep(toSave);
        //Store changed Relations
        saved = storeListChanges(toSave, dto.get());

        return Optional.of(convertModelIntoDTO(saved));
    }

    /**
     * Deletes and returns a given object afterwards.
     *
     * @param id of the to be deleted object
     * @return true if successful
     */
    public boolean deleteModelById(long id) {
        preDeleteCleanUp(id);
        deleteById(id);
        return findModelById(id).isEmpty();
    }


    public Optional<M> downwardSave(Optional<D> newD){
        if(newD.get().getId() != null){
            //ID set - update
            Optional<M> old = findById(newD.get().getId());
            if(old.isEmpty())
            {
                return Optional.empty();
            }
            M model = newD.map(d -> convertDTOIntoModel(d)).get();
            M toSave = compareUpdate(old.get(), model);

            //store changed Fields
            M saved = updateRep(toSave);
            //Store changed Relations
            saved = storeListChanges(toSave, newD.get());

            return Optional.of(saved);

        } else {
            // ID empty - save
            M model = newD.map(d -> convertDTOIntoModel(d)).get();
            //possibly set Standard data if model
            model = setStandardData(model);
            M saved = saveRep(model);
            model = setStandardData(model);
            D listModel = newD.get();
            listModel.setId(saved.getId());
            saved = storeListChanges(
                    saved, listModel
            );
            return Optional.of(saved);
        }
    }




    //----------------------Abstract Methods----------------------

    abstract  M setStandardData(M model);

    abstract void preDeleteCleanUp(long id);

    abstract D convertModelIntoDTO(M model);

    abstract M convertDTOIntoModel(D dto);

    abstract M compareUpdate(M old, M model);

    abstract M storeListChanges(M old, D dto);

    abstract M storeListChanges(M old, M model);

    abstract List<M> findall();

    abstract M saveRep(M model);

    abstract M updateRep(M model);

    abstract Optional<M> findById(long id);

    abstract void deleteById(long id);

}