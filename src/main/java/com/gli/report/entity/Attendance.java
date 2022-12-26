package com.gli.report.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "DIEM_DANH")
@Data
public class Attendance {

    @Id
    @Column(name = "ID")
    private int id;

    @OneToOne
    @JoinColumn(name = "MAHOCVIEN")
    private Student student;

    @Column(name = "NGAYDIEMDANH")
    private LocalDate attendanceDate;

    @OneToOne
    @JoinColumn(name = "MALOPHOC")
    private Grade grade;

    @Column(name = "LOAI")
    private int type;

    public boolean isDuplicated(Attendance opponent) {
        if (this.student.getId().equals(opponent.getStudent().getId())
                && this.attendanceDate.isEqual(opponent.getAttendanceDate())
                && this.type == opponent.getType()) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id) ^ student.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Attendance))
            return false;

        Attendance a = (Attendance) obj;
        return isDuplicated(a);
    }
}
