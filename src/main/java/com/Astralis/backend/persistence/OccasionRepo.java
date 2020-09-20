package com.Astralis.backend.persistence;

import com.Astralis.backend.model.Occasion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OccasionRepo extends AbstractRepo<Occasion>, CrudRepository<Occasion, Long> {
}
