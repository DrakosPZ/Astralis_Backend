package com.Astralis.backend.management.persistence;

import com.Astralis.backend.management.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepo extends AbstractRepo<User>, CrudRepository<User, Long> {
    /**
     * Finds the User in the table with the given LoginName
     *
     * @param loginName the looked for LoginName
     * @return the looked for User.
     */
    Optional<User> findByLoginInformationLoginName(String loginName);
}