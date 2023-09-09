package com.gli.report.service;

import com.gli.report.entity.Student;
import com.gli.report.entity.StudentGrade;
import com.gli.report.entity.modelQuery.ScoreModel;
import com.gli.report.model.ScoreDetail;
import com.gli.report.model.ScoreResponse;
import com.gli.report.repository.ScoreRepo;
import com.gli.report.repository.StudentGradeRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScoreService {

    private final ScoreRepo scoreRepo;
    private final StudentGradeRepo studentGradeRepo;

    public List<ScoreResponse> get(int gradeId, List<Integer> semesterIds) {
        List<ScoreResponse> result = new ArrayList<>();
        List<StudentGrade> stgLst = studentGradeRepo.findAllStudentByGrade(Collections.singletonList(gradeId));
        Map<String, Student> studentMap = stgLst.stream().map(StudentGrade::getStudent).collect(Collectors.toMap(Student::getId, Function.identity()));
        Map<String, List<ScoreModel>> scoreByStudents = scoreRepo.getAllByStudentsAndSemesters(stgLst.stream()
                .map(s -> s.getStudent().getId())
                .collect(Collectors.toList()), semesterIds)
                .stream().collect(Collectors.groupingBy(ScoreModel::getStudentId));

        scoreByStudents.forEach((stId, scoreLst) -> {
            ScoreResponse res = new ScoreResponse();
            Student student = studentMap.get(stId);
            if (ObjectUtils.isEmpty(student)) return;
            res.setStudentId(stId);
            res.setStudentName(student.getFullName());
            res.setDetails(mapperDetails(scoreLst));
            res.setAvgScore(calculateAvg(res.getDetails()));
            result.add(res);
        });

        return result;
    }

    private List<ScoreDetail> mapperDetails(List<ScoreModel> models) {
        models.sort(Comparator.comparing(ScoreModel::getTypeScoreId));
        List<ScoreDetail> result = new ArrayList<>();
        for (ScoreModel model: models) {
            ScoreDetail detail = new ScoreDetail();
            detail.setScore(model.getScore() == null ? 0 : model.getScore());
            detail.setScoreName(model.getTypeScoreName());
            detail.setCoefficient(model.getCoefficient());
            result.add(detail);
        }

        return result;
    }

    private Double calculateAvg(List<ScoreDetail> details) {
        double result = 0;
        if (ObjectUtils.isEmpty(details)) return result;
        double sumScore = 0;
        double sumCoefficient = 0;

        for (ScoreDetail detail: details) {
            sumScore += detail.getScore() * detail.getCoefficient();
            sumCoefficient += detail.getCoefficient();
        }
        if (sumCoefficient != 0) {
            DecimalFormat df = new DecimalFormat("0.00");
            result = Double.parseDouble(df.format(sumScore / sumCoefficient));
        }

        return result;
    }
}
