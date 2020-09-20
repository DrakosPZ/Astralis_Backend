package com.Astralis.backend.service;

import com.Astralis.backend.dto.LoginInformationDTO;
import com.Astralis.backend.dto.UserDTO;
import com.Astralis.backend.model.User;
import com.Astralis.backend.persistence.LoginInformationRepo;
import com.Astralis.backend.persistence.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserService
        extends AbstractService<UserDTO, User> {

    private final UserRepo userRepo;
    private final LoginInformationRepo loginInformationRepo;
    private final BCryptPasswordEncoder encoder;

    /**
     * Convert a given Model into a respective DTO
     *
     * @param model to be transformed
     * @return transformed DTO
     */
    @Override
    UserDTO convertModelIntoDTO(User model) {
        return new UserDTO(model);
    }

    /**
     * Convert a given DTO into a respective Model
     *
     * @param dto to be transformed
     * @return transformed model
     */
    @Override
    User convertDTOIntoModel(UserDTO dto) {
        User model = new User(dto);
        return model;
    }

    /**
     * Step 1. update simple fields
     * Compares the old to the new User model.
     * If a field has changed, the old model's field is updated to the new version.
     * The old model is then returned with the updated fields.
     *
     * @param old User model
     * @param model new User model
     * @return the old User model with the updated fields.
     */
    @Override
    User compareUpdate(User old, User model) {
        if(!old.getNickName().equals(model.getNickName())){
            old.setNickName(model.getNickName());
        }
        if(!old.getRole().equals(model.getRole())){
            old.setRole(model.getRole());
        }
        if(!old.getLoginInformation().getLoginName().equals(model.getLoginInformation().getLoginName())){
            old.getLoginInformation().setLoginName(model.getLoginInformation().getLoginName());
        }
        if(!encoder.matches(model.getLoginInformation().getPassword(), old.getLoginInformation().getPassword()) &&
           !model.getLoginInformation().getPassword().equals(old.getLoginInformation().getPassword())){
            old.getLoginInformation().setPassword(encoder.encode(model.getLoginInformation().getPassword()));
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
     * @param old User model.
     * @param dto new User dto.
     * @return the updated model from the database.
     */
    @Override
    User storeListChanges(User old, UserDTO dto) {
        //for those that are fully loaded by the converter
        User model = convertDTOIntoModel(dto);

        return findByIdentifier(model.getIdentifier()).get();
    }

    /**
     * Sets for every created User ...
     *             Not yet Used
     *
     * @param model the to be instantiated model.
     * @return the fully set-up model.
     */
    @Override
    User setStandardData(User model) {
        //set Standard Data

        return model;
    }

    /**
     * Calls the find all method of the personRepo.
     *
     * @return a List of all stored Persons in the table.
     */
    @Override
    List<User> findall() {
        return userRepo.findAll();
    }

    /**
     * Calls the save method of the personRepo.
     * Also encodes the Password, as the newly stored Person Model doesn't do it automatically.
     *
     * @param model the to be stored model person.
     * @return the stored model.
     */
    @Override
    User saveRep(User model) {
        model.getLoginInformation().setPassword(encoder.encode(model.getLoginInformation().getPassword()));
        //clean Relations --
        return userRepo.save(model);
    }

    /**
     * For Updates only the fields are stored, relations are handled independently,
     * as such relation-fields are cleaned and the save method is called.
     *
     * @param model to be updated model person.
     * @return the updated model.
     */
    @Override
    User updateRep(User model) {
        //clean Relations

        return userRepo.save(model);
    }

    /**
     * Calls the find by Identifier method of the respective repo
     *
     * @param identifier the looked for model's identifier
     * @return the looked for model.
     */
    @Override
    Optional<User> findByIdentifier(String identifier) {
        return userRepo.findByIdentifier(identifier);
    }

    /**
     * Calls the delete by identifier method of the respective repo
     *
     * @param identifier the to be deleted model's identifier
     */
    @Override
    void deleteByIdentifier(String identifier) {
        userRepo.deleteByIdentifier(identifier);
    }

    /**
     * no changes made before deletion, yet!
     * @param identifier
     */
    @Override
    void preDeleteCleanUp(String identifier) {
    }






    //----------------------Custome Methods----------------------
    /**
     * Returns the looked for UserDTO based on it's username.
     *
     * @param loginName the looked for model's LoginName
     * @return the looked for UserDTO
     */
    public Optional<UserDTO> findByLoginName(String loginName){
        return userRepo.findByLoginInformationLoginName(loginName)
                    .map(m -> convertModelIntoDTO(m));
    }

    /**
     * Returns the looked for UserDTO based on it's username and password.
     *
     * @param loginName the looked for model's LoginName
     * @param password the looked for model's Password
     * @return the looked for UserDTO
     */
    public Optional<UserDTO> findByLoginNamePassword(String loginName, String password){
        Optional<User> found = Optional.of(
                            loginInformationRepo.findByLoginName(loginName)
                                .orElseThrow( () -> new IllegalArgumentException("No User found with given LoginName"))
                                .getUser());
        if(found.isEmpty()){
            return found.map(m -> convertModelIntoDTO(m));
        }
        if(encoder.matches(password, found.get().getLoginInformation().getPassword())){
            return found.map(m -> convertModelIntoDTO(m));
        }
        throw new IllegalArgumentException("Passwords don't match");
    }









    //----------------------Relationship Handling Methods----------------------

}