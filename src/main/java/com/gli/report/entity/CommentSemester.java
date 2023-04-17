package com.gli.report.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "GHICHU_HOCVIEN")
@Data
public class CommentSemester {

    @Id
    @Column(name = "ID")
    private int id;

    @Column(name = "MANIENHOC")
    private int scholasticId;

    @Column(name = "MAHOCKI")
    private int semesterId;

    @Column(name = "GHICHU")
    private String comment;
}
