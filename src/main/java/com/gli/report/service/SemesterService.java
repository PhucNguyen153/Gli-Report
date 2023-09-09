package com.gli.report.service;

import com.gli.report.entity.Semester;
import com.gli.report.repository.SemesterRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SemesterService {

    private final SemesterRepo semesterRepo;

    public List<Semester> findAll() {
        return semesterRepo.findAllSemester();
    }
}
