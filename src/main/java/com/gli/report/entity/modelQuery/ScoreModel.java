package com.gli.report.entity.modelQuery;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScoreModel {
    private String studentId;
    private int typeScoreId;
    private String typeScoreName;
    private int coefficient;
    private Double score;

}
