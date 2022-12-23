package com.gli.report.service;

import com.gli.report.entity.Grade;
import com.gli.report.repository.GradeRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class GradeService {

    private final GradeRepo gradeRepo;

    public GradeService(GradeRepo gradeRepo) {
        this.gradeRepo = gradeRepo;
    }

    public List<Grade> getByUnitAndScholastic(List<Integer> unitIds, int scholasticId) {
        return gradeRepo.findAllByUnitIdInAndScholasticId(unitIds, scholasticId);
    }
}
