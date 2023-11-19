package com.gli.report.service;

import com.gli.report.Util.Constant;
import com.gli.report.entity.Grade;
import com.gli.report.enumeration.RuleMassEnum;
import com.gli.report.repository.GradeRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collections;
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

    public List<DayOfWeek> getRuleMassByGradeId(Integer gradeId) {
        Grade grade = gradeRepo.findById(gradeId).get();
        if (Constant.CHIEN_UNIT_IDS.contains(grade.getUnitId())) {
            return RuleMassEnum.CHIEN.getValue();
        } else if (Constant.AU_UNIT_IDS.contains(grade.getUnitId())) {
            return RuleMassEnum.AU.getValue();
        } else if (Constant.THIEU_UNIT_IDS.contains(grade.getUnitId())) {
            return RuleMassEnum.THIEU.getValue();
        } else if (Constant.NGHIA_UNIT_IDS.contains(grade.getUnitId())) {
            return RuleMassEnum.NGHIA.getValue();
        } else if (Constant.HIEP_UNIT_IDS.contains(grade.getUnitId())) {
            return RuleMassEnum.HIEP.getValue();
        }
        return Collections.EMPTY_LIST;
    }
}
