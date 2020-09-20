package com.Astralis.backend.persistence;

import com.Astralis.backend.model.Calender;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalenderRepo extends AbstractRepo<Calender>, CrudRepository<Calender, Long> {
}
