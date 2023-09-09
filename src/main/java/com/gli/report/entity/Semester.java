package com.gli.report.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "HOCKY")
@Data
public class Semester {

    @Id
    @Column(name = "MAHOCKY")
    private int id;

    @Column(name = "MANIENHOC")
    private int scholasticId;

    @Column(name = "TENHOCKY")
    private String name;

//    @Column(name = "TUNGAY")
//    private Date from;
//
//    @Column(name = "DENNGAY")
//    private Date to;

    @Transient
    private String scholasticName;

    public Semester(int id, int scholasticId, String name, String scholasticName) {
        this.id = id;
        this.name = name;
        this.scholasticId = scholasticId;
        this.scholasticName = scholasticName;
    }
}
