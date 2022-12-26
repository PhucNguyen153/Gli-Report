package com.gli.report.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceResponse {

    private String holyName;
    private String fullName;
    private String gradeName;
    private int weekDay;
    private int sunday;
    private int attendClass;
    private String teacher;
}
