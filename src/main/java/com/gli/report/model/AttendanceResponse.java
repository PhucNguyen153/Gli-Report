package com.gli.report.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceResponse {

    private String id;
    private String holyName;
    private String lastName;
    private String firstName;
    private String gradeName;
    private int weekDay;
    private int sunday;
    private int attendClass;
    private String teacher;

    public String getFullName() {
        return this.lastName + " " + this.firstName;
    }
}
