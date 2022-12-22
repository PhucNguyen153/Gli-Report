package com.gli.report.controller;

import com.gli.report.service.ScholasticService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/scholastic")
@CrossOrigin("*")
public class ScholasticController {

    private final ScholasticService scholasticService;

    public ScholasticController(ScholasticService scholasticService) {
        this.scholasticService = scholasticService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(scholasticService.getById(id));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
