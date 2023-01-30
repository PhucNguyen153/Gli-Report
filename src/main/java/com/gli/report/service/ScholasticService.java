package com.gli.report.service;

import com.gli.report.entity.Scholastic;
import com.gli.report.repository.ScholasticRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    public List<Scholastic> getAll() {
        return new ArrayList<>((Collection) scholasticRepo.findAll());
    }

    public int findLatest() {
        return scholasticRepo.findByMaxId();
    }
}
