package com.gli.report.service;

import com.gli.report.entity.Scholastic;
import com.gli.report.repository.ScholasticRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ScholasticService {

    private final ScholasticRepo scholasticRepo;

    public ScholasticService(ScholasticRepo scholasticRepo) {
        this.scholasticRepo = scholasticRepo;
    }

    public Scholastic getById(int id) {
        return scholasticRepo.findById(id).get();
    }
}
