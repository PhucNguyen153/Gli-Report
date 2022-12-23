package com.gli.report.repository;

import com.gli.report.entity.Unit;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UnitRepo extends CrudRepository<Unit, Integer> {

//    @Query("select u from Unit u where u.name like :branch% ")
    List<Unit> findAllByNameContainingIgnoreCase(String branch);
}
