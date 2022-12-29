package com.gli.report.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "THEOHOC")
@Data
public class StudentGrade {

    @Id
    @Column(name = "ID")
    private int id;

    @OneToOne
    @JoinColumn(name = "MAHOCVIEN")
    private Student student;

    @OneToOne
    @JoinColumn(name = "MALOPHOC")
    private Grade grade;
}
