package com.gli.report.controller;

import com.gli.report.service.ScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/score")
@CrossOrigin("*")
@RequiredArgsConstructor
public class ScoreController {

    private final ScoreService scoreService;

    @GetMapping("")
    public ResponseEntity<?> getScoreByGrade(@RequestParam("gradeId") Integer gradeId,
                                             @RequestParam("semesterIds") List<Integer> semesterIds) {
        try {
            return ResponseEntity.ok(scoreService.get(gradeId, semesterIds));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
