package com.gli.report.repository;

import com.gli.report.entity.Score;
import com.gli.report.entity.ScoreId;
import com.gli.report.entity.modelQuery.ScoreModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScoreRepo extends CrudRepository<Score, ScoreId> {

    @Query("SELECT new com.gli.report.entity.modelQuery.ScoreModel(s.id.studentId, s.id.typeScoreId, ts.name, " +
            "ts.coefficient, s.score) " +
            "FROM Score s " +
            "JOIN TypeScore ts on s.id.typeScoreId = ts.id " +
            "WHERE s.id.studentId in :studentIds " +
            "AND s.semesterId in :semesterIds " +
            "AND ts.typeId = 1 ")
    List<ScoreModel> getAllByStudentsAndSemesters(@Param("studentIds") List<String> studentIds,
                                                  @Param("semesterIds") List<Integer> semesterIds);
}
