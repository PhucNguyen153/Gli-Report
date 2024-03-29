package com.gli.report.repository;

import com.gli.report.entity.Student;
import com.gli.report.model.StudentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepo extends JpaRepository<Student, String> {

    @Query(value = "SELECT hv.MAHOCVIEN, hv.TENTHANH as hvHoly, hv.HOCANHAN as hvLast, hv.TENCANHAN as hvFirst, hv.HOTENPHCHA, hv.HOTENPHME, " +
            "glv.TENTHANH, glv.HOCANHAN, glv.TENCANHAN, hv.SODIENTHOAI as hvPhone, hv.SODIENTHOAI2 as hvPhone2, glv.SODIENTHOAI, lh.TENLOPHOC, " +
            "k.TENKHOI, gh.TENGIAOHO, th.GHICHU as nhanxet, hv.NGAYSINH , hv.TINHTRANG " +
            "FROM HOCVIEN hv " +
            "left outer join KHONGCONHOC kch on kch.MAHOCVIEN = hv.MAHOCVIEN " +
            "join THEOHOC th on hv.MAHOCVIEN = th.MAHOCVIEN " +
            "join LOPHOC lh on th.MALOPHOC = lh.MALOPHOC " +
            "join DAY_LOP dl on lh.MALOPHOC = dl.MALOPHOC " +
            "join GIAOLYVIEN glv on dl.MAGLV  = glv.MAGLV " +
            "join GIAOHO gh on hv.MAGIAOHO = gh.MAGIAOHO " +
            "join KHOI k on k.MAKHOI = lh.MAKHOI " +
            "WHERE " +
            "(UPPER(hv.LNAME + ' ' + hv.FNAME)) like UPPER(concat('%', :name,'%')) " +
            "and th.MANIENHOC = :scholasticId " +
            "and dl.VAITRO != 'GLV1' " +
            "and th.NGAYXOA is null " +
            "and (:gradeId is null or lh.MALOPHOC = :gradeId) ", nativeQuery = true)
    List<Object[]> searchByNameAndScholasticId(@Param("name") String name, @Param("scholasticId") int scholasticId, @Param("gradeId") Integer gradeId);
}
