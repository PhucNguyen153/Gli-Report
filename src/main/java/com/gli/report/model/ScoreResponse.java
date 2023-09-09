package com.gli.report.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScoreResponse {

    private String studentId;
    private String studentName;
    private double avgScore;
    private List<ScoreDetail> details;
}
