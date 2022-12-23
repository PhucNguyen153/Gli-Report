package com.gli.report.controller;

import com.gli.report.service.GradeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/grade")
@CrossOrigin("*")
public class GradeController {

    private final GradeService gradeService;

    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @GetMapping("")
    public ResponseEntity<?> getAllByUnitAndScholastic(@RequestParam("unitIds") List<Integer> unitIds,
                                                       @RequestParam(value = "scholasticId", defaultValue = "3") Integer scholasticId) {
        try {
            return ResponseEntity.ok(gradeService.getByUnitAndScholastic(unitIds, scholasticId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
