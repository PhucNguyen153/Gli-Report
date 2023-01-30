package com.gli.report.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentInfo {

    private String id;
    private String fullName;
    private String father;
    private String mother;
    private String teacher;
    private String phoneNumber;
    private String teacherPhoneNumber;
    private String grade;
    private String unit;
    private String diocese;
}
