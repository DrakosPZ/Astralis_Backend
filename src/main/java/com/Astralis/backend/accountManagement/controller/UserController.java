package com.Astralis.backend.accountManagement.controller;

import com.Astralis.backend.accountManagement.dto.UserDTO;
import com.Astralis.backend.accountManagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@RequestMapping(path = "/user")
@CrossOrigin("http://localhost:4200")
public class UserController extends AbstractController<UserDTO> {
    private final UserService service;

    /**
     * calls the find all method of the appropriate service.
     *
     * @return a List of DTO Objects.
     */
    @Override
    List<UserDTO> findAllDTO() {
        return service.findAll();
    }

    /**
     * calls the findbyIdentifier method of the appropriate service.
     *
     * @return a DTO object.
     */
    @Override
    Optional<UserDTO> findByIdentifierDTO(String identifier) {
        return service.findModelByIdentifier(identifier);
    }

    /**
     * calls the save method of the appropriate service.
     * Before that also checks if Username is already in use.
     *
     * @return the saved DTO object.
     */
    @Override
    Optional<UserDTO> saveDTO(Optional<UserDTO> optionaldto) {
        if(!service.findByLoginName(optionaldto
                .orElseThrow(() -> new IllegalArgumentException("given User is empty"))
                .getLoginInformation().getLoginName()).isEmpty()){
            throw new IllegalArgumentException("Username is already used");
        }
        return service.save(optionaldto);
    }

    /**
     * calls the update method of the appropriate service.
     *
     * @return the updated DTO object.
     */
    @Override
    Optional<UserDTO> updateDTO(Optional<UserDTO> optionaldto) {
        return service.update(optionaldto);
    }

    /**
     * calls the deleteByIdentifier method of the appropriate service.
     *
     * @return the deleted DTO object.
     */
    @Override
    Optional<UserDTO> deleteByIdentifierDTO(String identifier) {
        Optional<UserDTO> optionaldto = service.findModelByIdentifier(identifier);
        service.deleteModelByIdentifier(identifier);
        return optionaldto;
    }









    //----------------------Custom Route Methods----------------------
    /**
     * Post route to return a loggedin Account,
     * out of security also double checks for the right password.
     *
     * @param holder a has Map with the username and password.
     * @return the to the username and password according Users DTO.
     */
    @PostMapping(path = "/afterLogin", consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<UserDTO> findByUsernameAndPassword(@RequestBody Map<String, String> holder ){
        String username = holder.keySet().stream()
                .map(key -> key)
                .collect(Collectors.toList()).get(0);
        String password = holder.keySet().stream()
                .map(key -> holder.get(key))
                .collect(Collectors.toList()).get(0);
        Optional<UserDTO> find = service.findByLoginNamePassword(username, password);
        if(find.isEmpty())
        {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.of(
                find.map(this::addSelfLink)
        );
    }

    /**
     * A route made to find a UserDTO based on their username.
     *
     * @param loginName the looked for Users's LoginName.
     * @return the looked for Users in a ResponseEntity.
     */
    @GetMapping(params = "loginName")
    public ResponseEntity<UserDTO> findByUsername(
            @RequestParam String loginName
    ) {
        Optional<UserDTO> find = service.findByLoginName(loginName);
        if(find.isEmpty())
        {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.of(
                find.map(this::addSelfLink)
        );
    }

    /**
     * A route made to register new Users
     *
     * @param dto the new UserDTO
     * @return the newly added UserDTO
     */
    @PostMapping(path = "/registration", consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<UserDTO> registration(
            @RequestBody UserDTO dto) {
        return create(dto);
    }
}
