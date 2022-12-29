package com.gli.report.repository;

import com.gli.report.entity.StudentGrade;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentGradeRepo extends CrudRepository<StudentGrade, Integer> {

    @Query("select sg from StudentGrade sg where " +
           "sg.grade.id in (:gradeIds) " +
           "and sg.deletedDate is null")
    List<StudentGrade> findAllStudentByGrade(@Param("gradeIds") List<Integer> gradeIds);
}
