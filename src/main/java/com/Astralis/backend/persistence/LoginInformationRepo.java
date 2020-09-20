package com.Astralis.backend.persistence;

import com.Astralis.backend.model.LoginInformation;
import org.springframework.data.repository.CrudRepository;

public interface LoginInformationRepo extends AbstractRepo<LoginInformation>, CrudRepository<LoginInformation, Long> {
}
