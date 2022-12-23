package com.gli.report.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "KHOI")
@Data
public class Unit {

    @Id
    @Column(name = "MAKHOI")
    private int id;

    @Column(name = "TENKHOI")
    private String name;
}
