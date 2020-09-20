package com.Astralis.backend.controller;

import com.Astralis.backend.dto.WorkWeekDto;
import com.Astralis.backend.service.WorkWeekService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@RequestMapping(path = "/workweek")
public class WorkWeekController extends AbstractController<WorkWeekDto>{
    private final WorkWeekService service;

    /**
     * calls the find all method of the appropriate service.
     *
     * @return a List of DTO Objects.
     */
    @Override
    List<WorkWeekDto> findAllDTO() {
        return service.findAll();
    }

    /**
     * calls the findbyIdentifier method of the appropriate service.
     *
     * @return a DTO object.
     */
    @Override
    Optional<WorkWeekDto> findByIdentifierDTO(String identifier) {
        return service.findModelByIdentifier(identifier);
    }

    /**
     * calls the save method of the appropriate service.
     *
     * @return the saved DTO object.
     */
    @Override
    Optional<WorkWeekDto> saveDTO(Optional<WorkWeekDto> optionaldto) {
        return service.save(optionaldto);
    }

    /**
     * calls the update method of the appropriate service.
     *
     * @return the updated DTO object.
     */
    @Override
    Optional<WorkWeekDto> updateDTO(Optional<WorkWeekDto> optionaldto) {
        return service.update(optionaldto);
    }

    /**
     * calls the deleteByIdentifier method of the appropriate service.
     *
     * @return the deleted DTO object.
     */
    @Override
    Optional<WorkWeekDto> deleteByIdentifierDTO(String identifier) {
        Optional<WorkWeekDto> optionaldto = service.findModelByIdentifier(identifier);
        service.deleteModelByIdentifier(identifier);
        return optionaldto;
    }


}
