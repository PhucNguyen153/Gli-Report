package com.gli.report.controller;

import com.gli.report.model.AttendanceRequest;
import com.gli.report.service.AttendanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/attendance")
@CrossOrigin("*")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping("")
    public ResponseEntity<?> getAttendanceByTime(@RequestBody AttendanceRequest request) {
        try {
            return ResponseEntity.ok(attendanceService.getAttendanceByTime(request));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
