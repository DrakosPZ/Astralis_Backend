package com.Astralis.backend.accountManagement.service;

import com.Astralis.backend.accountManagement.dto.AbstractModelDto;
import com.Astralis.backend.accountManagement.model.AbstractModel;
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
        <D extends AbstractModelDto, M extends AbstractModel> {

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
     * Returns a searched for object by the given identifier.
     *
     * @param identifier of the to find object
     * @return the looked for object
     */
    public Optional<D> findModelByIdentifier(String identifier) {
        return findByIdentifier(identifier)
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
        M listModel = setStandardData(dto.map(d -> convertDTOIntoModel(d)).get());
        listModel.setIdentifier(saved.getIdentifier());
        saved = storeListChanges(
                saved, convertModelIntoDTO(listModel)
        );
        return Optional.of(saved)
                .map(m -> convertModelIntoDTO(m));
        /*return Optional.of(saveRep(model))
                .map(m -> convertModelIntoDTO(m));*/

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
        Optional<M> old = findByIdentifier(dto.get().getIdentifier());
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
     * @param identifier of the to be deleted object
     * @return true if successful
     */
    public boolean deleteModelByIdentifier(String identifier) {
        preDeleteCleanUp(identifier);
        deleteByIdentifier(identifier);
        return findModelByIdentifier(identifier).isEmpty();
    }





    //----------------------Abstract Methods----------------------

    abstract  M setStandardData(M model);

    abstract void preDeleteCleanUp(String identifier);

    abstract D convertModelIntoDTO(M model);

    abstract M convertDTOIntoModel(D dto);

    abstract M compareUpdate(M old, M model);

    abstract M storeListChanges(M old, D dto);

    abstract List<M> findall();

    abstract M saveRep(M model);

    abstract M updateRep(M model);

    abstract Optional<M> findByIdentifier(String identifier);

    abstract void deleteByIdentifier(String identifier);

}

