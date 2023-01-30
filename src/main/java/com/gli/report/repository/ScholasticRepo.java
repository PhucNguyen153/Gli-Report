package com.gli.report.repository;

import com.gli.report.entity.Scholastic;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ScholasticRepo extends CrudRepository<Scholastic, Integer> {

    @Query("Select MAX(s.id) from Scholastic s")
    int findByMaxId();
}
