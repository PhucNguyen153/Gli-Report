package com.gli.report.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "LOPHOC")
@Data
public class Grade implements Comparable<Grade>{

    @Id
    @Column(name = "MALOPHOC")
    private int id;

    @Column(name = "TENLOPHOC")
    private String name;

    @Column(name = "MANIENHOC")
    private int scholasticId;

    @Column(name = "CHUNHIEM")
    private String teacher;

    @Column(name = "GLV1")
    private String tutor;

    @Column(name = "MAKHOI")
    private int unitId;

    @Override
    public int compareTo(Grade o) {
        return this.name.compareTo(o.getName());
    }
}
