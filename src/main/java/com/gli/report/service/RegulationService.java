package com.gli.report.service;

import com.gli.report.Util.Constant;
import com.gli.report.entity.Regulation;
import com.gli.report.repository.RegulationRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RegulationService {

    private final RegulationRepo regulationRepo;
//    private final Constant constant;

    public List<DayOfWeek> getByUnitAndSemester(int unitId, int semesterId) {
        Regulation regulation = regulationRepo.findByUnitIdAndSemesterIdAndName(unitId, semesterId, Constant.RULE_MASSES);
        List<DayOfWeek> ruleMassesDays = new ArrayList<>();
        if (!ObjectUtils.isEmpty(regulation.getRule())) {
            List<String> splitRules = Arrays.asList(regulation.getRule().split(","));
            for (String rule: splitRules) {
                switch (rule) {
                    case "1":
                        ruleMassesDays.add(DayOfWeek.SUNDAY);
                        break;
                    case "2":
                        ruleMassesDays.add(DayOfWeek.MONDAY);
                        break;
                    case "3":
                        ruleMassesDays.add(DayOfWeek.TUESDAY);
                        break;
                    case "4":
                        ruleMassesDays.add(DayOfWeek.WEDNESDAY);
                        break;
                    case "5":
                        ruleMassesDays.add(DayOfWeek.THURSDAY);
                        break;
                    case "6":
                        ruleMassesDays.add(DayOfWeek.FRIDAY);
                        break;
                    case "7":
                        ruleMassesDays.add(DayOfWeek.SATURDAY);
                        break;
                }
            }
        }
        return ruleMassesDays;
    }

}
