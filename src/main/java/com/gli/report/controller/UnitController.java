package com.gli.report.controller;

import com.gli.report.service.UnitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/unit")
@CrossOrigin("*")
public class UnitController {

    private final UnitService unitService;

    public UnitController(UnitService unitService) {
        this.unitService = unitService;
    }

    @GetMapping("/{branch}")
    public ResponseEntity<?> getAllByBranch(@PathVariable String branch) {
        try {
            return ResponseEntity.ok(unitService.getByBranch(branch));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
