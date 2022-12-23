package com.gli.report.service;

import com.gli.report.entity.Branch;
import com.gli.report.entity.Unit;
import com.gli.report.repository.UnitRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UnitService {

    private final UnitRepo unitRepo;

    public UnitService(UnitRepo unitRepo) {
        this.unitRepo = unitRepo;
    }

    public List<Unit> getByBranch(String branchName) {
        Branch branch = Branch.getByShortName(branchName);
        return unitRepo.findAllByNameContainingIgnoreCase(branch.getName());
    }
}
