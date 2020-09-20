package com.Astralis.backend.persistence;

import com.Astralis.backend.model.OccasionTag;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OccasionTagRepo extends AbstractRepo<OccasionTag>, CrudRepository<OccasionTag, Long> {
}
