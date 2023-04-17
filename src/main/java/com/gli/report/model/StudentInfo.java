package com.gli.report.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private String note;

    private Map<String, String> comments;

    public StudentInfo(String id, String fullName, String father, String mother, String teacher, String phone1,
                       String phone2, String teacherPhoneNumber, String grade, String unit, String diocese, String note) {
        this.id = id;
        this.fullName = fullName;
        this.father = father;
        this.mother = mother;
        this.teacher = teacher;
        this.teacherPhoneNumber = teacherPhoneNumber;
        this.grade = grade;
        this.unit = unit;
        this.diocese = diocese;
        this.note = note;
        this.phoneNumbers = new ArrayList<>();
        if (!ObjectUtils.isEmpty(phone1)) this.phoneNumbers.add(phone1);
        if (!ObjectUtils.isEmpty(phone2)) this.phoneNumbers.add(phone2);
    }
}
