package com.gli.report.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentInfo {

    private String id;
    private String fullName;
    private String father;
    private String mother;
    private String teacher;
    private List<String> phoneNumbers;
    private String teacherPhoneNumber;
    private String grade;
    private String unit;
    private String diocese;
}
