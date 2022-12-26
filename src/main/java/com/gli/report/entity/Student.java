package com.gli.report.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "HOCVIEN")
@Data
public class Student {

    @Id
    @Column(name = "MAHOCVIEN")
    private String id;

    @Column(name = "TENTHANH")
    private String holyName;

    @Column(name = "HOCANHAN")
    private String lastName;

    @Column(name = "TENCANHAN")
    private String firstName;

    public String getFullName() {
        return lastName + " " + firstName;
    }
}
