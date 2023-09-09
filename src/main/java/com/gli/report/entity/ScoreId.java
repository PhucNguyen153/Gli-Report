package com.gli.report.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
public class ScoreId implements Serializable {

    @Column(name = "MAHOCVIEN")
    private String studentId;

    @Column(name = "MALOPHOC")
    private int gradeId;

    @Column(name = "MACOTDIEM")
    private int typeScoreId;
}
