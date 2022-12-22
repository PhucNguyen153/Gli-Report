package com.gli.report.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "NIENHOC")
@Data
public class Scholastic {

    @Id
    @Column(name = "MANIENHOC")
    private int id;

    @Column(name = "TENNIENHOC")
    private String name;
}
