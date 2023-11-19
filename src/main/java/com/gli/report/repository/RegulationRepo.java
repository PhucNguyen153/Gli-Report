package com.gli.report.repository;

import com.gli.report.entity.Regulation;
import org.springframework.data.repository.CrudRepository;

public interface RegulationRepo extends CrudRepository<Regulation, Integer> {

    Regulation findByUnitIdAndSemesterIdAndName(int unitId, int semesterId, String name);
}
