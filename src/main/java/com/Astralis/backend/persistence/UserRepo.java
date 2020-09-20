package com.Astralis.backend.persistence;

import com.Astralis.backend.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepo extends AbstractRepo<User>, CrudRepository<User, Long> {
}