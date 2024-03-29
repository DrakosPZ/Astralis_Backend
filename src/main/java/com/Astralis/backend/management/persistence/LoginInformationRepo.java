package com.Astralis.backend.management.persistence;

import com.Astralis.backend.management.model.LoginInformation;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface LoginInformationRepo extends AbstractRepo<LoginInformation>, CrudRepository<LoginInformation, Long> {
    /**
     * Finds the LoginInformation in the table with the given LoginName
     *
     * @param loginName the looked for LoginName
     * @return the looked for LoginInformation.
     */
    Optional<LoginInformation> findByLoginName(String loginName);

}
