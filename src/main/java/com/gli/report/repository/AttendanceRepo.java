package com.gli.report.repository;

import com.gli.report.entity.Attendance;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepo extends CrudRepository<Attendance, Integer> {

    @Query("select a from Attendance a where " +
            "a.grade.id in (:gradeIds) " +
            "and a.attendanceDate <= :end " +
            "and a.attendanceDate >= :start")
    List<Attendance> findAllByGradeIdAndTime(@Param("gradeIds") List<Integer> gradeIds,
                                             @Param("start") LocalDate start,
                                             @Param("end") LocalDate end);

    @Query("select a from Attendance a where " +
            "a.student.id in (:studentIds) " +
            "and a.attendanceDate <= :end " +
            "and a.attendanceDate >= :start")
    List<Attendance> findAllByStudentIdAndTime(@Param("studentIds") List<String> studentIds,
                                             @Param("start") LocalDate start,
                                             @Param("end") LocalDate end);
}
