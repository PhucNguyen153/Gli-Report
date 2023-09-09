package com.gli.report.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "DIEM")
public class Score {

    @EmbeddedId
    private ScoreId id;

    @Column(name = "HOCKY_NIENHOC")
    private int semesterId;

    @Column(name = "DIEMDAT")
    private Double score;
}
