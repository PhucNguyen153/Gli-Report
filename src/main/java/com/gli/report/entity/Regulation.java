package com.gli.report.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "QUYDINH")
@Data
public class Regulation {

    @Id
    @Column(name = "MAQUYDINH")
    private int id;

    @Column(name = "MAKHOI")
    private int unitId;

    @Column(name = "MAHOCKY")
    private int semesterId;

    @Column(name = "TENQUYDINH")
    private String name;

    @Column(name = "GHICHU")
    private String rule;

}
