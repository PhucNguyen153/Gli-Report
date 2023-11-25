package com.gli.report.controller;

import com.gli.report.model.AttendanceRequest;
import com.gli.report.model.SearchRequest;
import com.gli.report.service.AttendanceService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
            return ResponseEntity.ok(attendanceService.getAttendanceByTimeV2(request));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/export")
    public ResponseEntity<?> export(@RequestBody AttendanceRequest request) {
        try {
            ByteArrayInputStream result = attendanceService.exportExcel(request);
            if (result != null) {
                StringBuilder headerValues = new StringBuilder();
                headerValues.append("attachment;filename=")
                        .append("Report_")
                        .append(request.getStartDate().toString())
                        .append("_")
                        .append(request.getEndDate().toString())
                        .append(".xlsx");
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .header(HttpHeaders.CONTENT_DISPOSITION, headerValues.toString())
                        .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                        .body(new InputStreamResource(result));
            }
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something Wrong");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchByName(@RequestBody SearchRequest searchRequest) {
        try {
            return ResponseEntity.ok(attendanceService.search(searchRequest));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/export-detail")
    public ResponseEntity<?> exportDetailMass(@RequestBody AttendanceRequest request) {
        try {
            ByteArrayInputStream result = attendanceService.exportExcelDetail(request);
            if (result != null) {
                StringBuilder headerValues = new StringBuilder();
                headerValues.append("attachment;filename=")
                        .append("Report_Detail_Mass_")
                        .append(request.getStartDate().toString())
                        .append("_")
                        .append(request.getEndDate().toString())
                        .append(".xlsx");
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .header(HttpHeaders.CONTENT_DISPOSITION, headerValues.toString())
                        .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                        .body(new InputStreamResource(result));
            }
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something Wrong");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
