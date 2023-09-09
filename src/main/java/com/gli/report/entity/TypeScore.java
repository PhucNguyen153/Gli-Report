package com.gli.report.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "COTDIEM")
public class TypeScore {

    @Id
    @Column(name = "MACOTDIEM")
    private int id;

    @Column(name = "MALOAIDIEM")
    private int typeId;

    @Column(name = "MAHOCKY")
    private int semesterId;

    @Column(name = "TENCOTDIEM")
    private String name;

    @Column(name = "HESOTINH")
    private int coefficient;

    @Column(name = "MAKHOI")
    private int unitId;
}
