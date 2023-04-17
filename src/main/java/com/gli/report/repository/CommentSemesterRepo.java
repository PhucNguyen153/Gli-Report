package com.gli.report.repository;

import com.gli.report.entity.CommentSemester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentSemesterRepo extends JpaRepository<CommentSemester, Integer> {
    @Query(value = "SELECT hk.TENHOCKY, gchv.GHICHU " +
            "FROM GHICHU_HOCVIEN gchv " +
            "join HOCKY hk on hk.MAHOCKY = gchv.MAHOCKY " +
            "WHERE " +
            "gchv.MAHOCVIEN = :studentId " +
            "and gchv.MANIENHOC = :scholasticId", nativeQuery = true)
     List<Object[]> getCommentByStudentAndScholastic(@Param("studentId") String studentId, @Param("scholasticId") int scholasticId);
}
