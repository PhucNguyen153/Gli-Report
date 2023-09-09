package com.gli.report.service;

import com.gli.report.entity.Grade;
import com.gli.report.repository.GradeRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@Slf4j
public class GradeService {

    private final GradeRepo gradeRepo;
    private final ScholasticService scholasticService;

    public GradeService(GradeRepo gradeRepo, ScholasticService scholasticService) {
        this.gradeRepo = gradeRepo;
        this.scholasticService = scholasticService;
    }

    public List<Grade> getByUnitAndScholastic(List<Integer> unitIds, int scholasticId) {
        if (ObjectUtils.isEmpty(scholasticId)) {
            scholasticId = scholasticService.findLatest();
        }
        return gradeRepo.findAllByUnitIdInAndScholasticId(unitIds, scholasticId);
    }
}
