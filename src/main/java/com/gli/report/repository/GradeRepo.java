package com.gli.report.repository;

import com.gli.report.entity.Grade;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GradeRepo extends CrudRepository<Grade, Integer> {

    List<Grade> findAllByUnitIdInAndScholasticId(List<Integer> unitIds, int scholasticId);
}
