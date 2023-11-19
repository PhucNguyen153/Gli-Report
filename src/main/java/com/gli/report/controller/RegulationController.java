package com.gli.report.controller;

import com.gli.report.service.RegulationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/regulation")
@CrossOrigin("*")
@RequiredArgsConstructor
public class RegulationController {

    private final RegulationService regulationService;

    @GetMapping("")
    public ResponseEntity<?> getScoreByGrade(@RequestParam("unitId") Integer unitId,
                                             @RequestParam("semesterId") Integer semesterId) {
        try {
            return ResponseEntity.ok(regulationService.getByUnitAndSemester(unitId, semesterId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
