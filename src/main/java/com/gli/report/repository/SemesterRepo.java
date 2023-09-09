package com.gli.report.repository;

import com.gli.report.entity.Semester;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SemesterRepo extends CrudRepository<Semester, Integer> {

    @Query("SELECT new com.gli.report.entity.Semester(s.id, s.scholasticId, s.name, sc.name) " +
            "FROM Semester s " +
            "JOIN Scholastic sc on s.scholasticId = sc.id ")
    List<Semester> findAllSemester();
}
