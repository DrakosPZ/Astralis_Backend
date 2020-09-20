package com.Astralis.backend.persistence;

import com.Astralis.backend.model.WorkWeek;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkWeekRepo extends AbstractRepo<WorkWeek>, CrudRepository<WorkWeek, Long> {
}
