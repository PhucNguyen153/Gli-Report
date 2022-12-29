package com.gli.report.service;

import com.gli.report.entity.Attendance;
import com.gli.report.entity.Grade;
import com.gli.report.entity.Student;
import com.gli.report.entity.StudentGrade;
import com.gli.report.model.AttendanceRequest;
import com.gli.report.model.AttendanceResponse;
import com.gli.report.repository.AttendanceRepo;
import com.gli.report.repository.StudentGradeRepo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.Collator;
import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AttendanceService {

    private final AttendanceRepo attendanceRepo;
    private final StudentGradeRepo studentGradeRepo;

    public AttendanceService(AttendanceRepo attendanceRepo,
                             StudentGradeRepo studentGradeRepo) {
        this.attendanceRepo = attendanceRepo;
        this.studentGradeRepo = studentGradeRepo;
    }

    public List<AttendanceResponse> getAttendanceByTime(AttendanceRequest request) {
        List<Attendance> arrayList = attendanceRepo.findAllByGradeIdAndTime(request.getGradeIds(), request.getStartDate(), request.getEndDate());
        List<AttendanceResponse> result = new ArrayList<>();
        Set<Attendance> uniqueElements = new HashSet<>(arrayList);
        Map<Student, List<Attendance>> mapByStudent = uniqueElements.stream().collect(Collectors.groupingBy(Attendance::getStudent));
        mapByStudent.forEach((s, aLst) -> {
            AttendanceResponse res = new AttendanceResponse();
            res.setHolyName(s.getHolyName());
            res.setFirstName(s.getFirstName());
            res.setLastName(s.getLastName());
            res.setId(s.getId());
            res.setTeacher(aLst.get(0).getGrade().getTeacher());
            res.setGradeName(aLst.get(0).getGrade().getName());
            int weekDay = 0;
            int sunDay = 0;
            int attendClass = 0;

            for (Attendance a: aLst) {
                if (a.getType() == 2) {
                    attendClass++;
                    continue;
                }
                if (a.getAttendanceDate().getDayOfWeek().equals(DayOfWeek.SUNDAY) && a.getType() == 1) {
                   sunDay++;
                } else {
                   weekDay++;
                }
            }

            res.setWeekDay(weekDay);
            res.setSunday(sunDay);
            res.setAttendClass(attendClass);
            result.add(res);
        });
        return result;
    }

    public List<AttendanceResponse> getAttendanceByTimeV2(AttendanceRequest request) {
        //Get all attendances and remove duplicate
        List<Attendance> arrayList = attendanceRepo.findAllByGradeIdAndTime(request.getGradeIds(), request.getStartDate(), request.getEndDate());
        List<AttendanceResponse> result = new ArrayList<>();
        Set<Attendance> uniqueElements = new HashSet<>(arrayList);
        //Map all attendance by student id
        Map<String, List<Attendance>> mapByStudent = uniqueElements.stream().collect(Collectors.groupingBy(a -> a.getStudent().getId()));

        //Get all student by class ids and map by class
        List<StudentGrade> sgLst = studentGradeRepo.findAllStudentByGrade(request.getGradeIds());
        Map<Grade, List<StudentGrade>> mapByGrade = sgLst.stream().collect(Collectors.groupingBy(StudentGrade::getGrade));

        //Sort
        TreeMap<Grade, List<StudentGrade>> sorted = new TreeMap<>(mapByGrade);

        sorted.forEach((grade, sLst) -> {
            Collator esCollator = Collator.getInstance(new Locale("vi", "VN"));
            sLst.sort((s1, s2) -> {
                return esCollator.compare(s1.getStudent().getFullName(), s2.getStudent().getFullName());
            });
            for (StudentGrade sg: sLst) {
                AttendanceResponse res = new AttendanceResponse();
                res.setHolyName(sg.getStudent().getHolyName());
                res.setFirstName(sg.getStudent().getFirstName());
                res.setLastName(sg.getStudent().getLastName());
                res.setId(sg.getStudent().getId());
                res.setTeacher(grade.getTeacher());
                res.setGradeName(grade.getName());
                int weekDay = 0;
                int sunDay = 0;
                int attendClass = 0;

                //Get Attendance of student and count
                List<Attendance> aLst = mapByStudent.get(sg.getStudent().getId());
                if (!ObjectUtils.isEmpty(aLst)) {
                    for (Attendance a: aLst) {
                        if (a.getType() == 2) {
                            attendClass++;
                            continue;
                        }
                        if (a.getAttendanceDate().getDayOfWeek().equals(DayOfWeek.SUNDAY) && a.getType() == 1) {
                            sunDay++;
                        } else {
                            weekDay++;
                        }
                    }
                }
                res.setWeekDay(weekDay);
                res.setSunday(sunDay);
                res.setAttendClass(attendClass);
                result.add(res);
            }
        });


        return result;
    }

    public ByteArrayInputStream exportExcel(AttendanceRequest request) throws IOException {
        List<AttendanceResponse> data = getAttendanceByTimeV2(request);
        Map<String, List<AttendanceResponse>> mapByGradeName = data.stream().collect(Collectors.groupingBy(AttendanceResponse::getGradeName));
        Workbook workbook = new XSSFWorkbook();
        mapByGradeName.forEach((gName, aLst) -> {
            Sheet sheet = workbook.createSheet(gName);
            //Create data for header
            createHeader(sheet, workbook, aLst.get(0).getTeacher());

            //Create data for each row
            CellStyle normalStyle = setStyleForCell("normal", workbook);
            createDataRow(sheet, normalStyle, aLst);
            for (int i = 0; i < 11; i++) {
                sheet.autoSizeColumn(i);
                sheet.setColumnWidth(i, sheet.getColumnWidth(i) * 17 / 10);
            }

        });

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        out.close();
        return in;
    }

    private void createHeader(Sheet sheet, Workbook workbook, String teacher) {
        Row header = sheet.createRow(0);
        CellStyle cellStyle = setStyleForCell("header", workbook);
        //Create Cells for header
        Cell headerCell = header.createCell(0);
        headerCell.setCellValue("ID");
        headerCell.setCellStyle(cellStyle);
        headerCell = header.createCell(1);
        headerCell.setCellValue("Tên Thánh");
        headerCell.setCellStyle(cellStyle);
        headerCell = header.createCell(2);
        headerCell.setCellValue("Họ");
        headerCell.setCellStyle(cellStyle);
        headerCell = header.createCell(3);
        headerCell.setCellValue("Tên");
        headerCell.setCellStyle(cellStyle);
        headerCell = header.createCell(4);
        headerCell.setCellValue("Lớp");
        headerCell.setCellStyle(cellStyle);
        headerCell = header.createCell(5);
        headerCell.setCellValue("Lễ thường");
        headerCell.setCellStyle(cellStyle);
        headerCell = header.createCell(6);
        headerCell.setCellValue("Lễ Chúa Nhật");
        headerCell.setCellStyle(cellStyle);
        headerCell = header.createCell(7);
        headerCell.setCellValue("Đi học");
        headerCell.setCellStyle(cellStyle);

        CellStyle teacherStyle = setStyleForCell("teacher", workbook);

        headerCell = header.createCell(8);
        headerCell.setCellValue("GLV: " + teacher);
        headerCell.setCellStyle(teacherStyle);
    }

    private void createDataRow(Sheet sheet, CellStyle cellStyle, List<AttendanceResponse> aLst) {
        int rowNum = 1;

        for (AttendanceResponse ar: aLst) {
            Row row = sheet.createRow(rowNum);
            Cell cell = row.createCell(0);
            cell.setCellValue(ar.getId());
            cell.setCellStyle(cellStyle);
            cell = row.createCell(1);
            cell.setCellValue(ar.getHolyName());
            cell.setCellStyle(cellStyle);
            cell = row.createCell(2);
            cell.setCellValue(ar.getLastName());
            cell.setCellStyle(cellStyle);
            cell = row.createCell(3);
            cell.setCellValue(ar.getFirstName());
            cell.setCellStyle(cellStyle);
            cell = row.createCell(4);
            cell.setCellValue(ar.getGradeName());
            cell.setCellStyle(cellStyle);
            cell = row.createCell(5);
            cell.setCellValue(ar.getWeekDay());
            cell.setCellStyle(cellStyle);
            cell = row.createCell(6);
            cell.setCellValue(ar.getSunday());
            cell.setCellStyle(cellStyle);
            cell = row.createCell(7);
            cell.setCellValue(ar.getAttendClass());
            cell.setCellStyle(cellStyle);
            rowNum++;
        }
    }

    private CellStyle setStyleForCell(String type, Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        switch (type) {
            case "header":
                cellStyle.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
                Font headerFont = workbook.createFont();
                headerFont.setColor(IndexedColors.WHITE.getIndex());
                headerFont.setBold(true);
                cellStyle.setFont(headerFont);
                break;
            case "normal":
                cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                break;
            case "teacher":
                cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                Font teacherFont = workbook.createFont();
                teacherFont.setBold(true);
                cellStyle.setFont(teacherFont);
                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                cellStyle.setAlignment(HorizontalAlignment.CENTER);
                cellStyle.setBorderBottom(BorderStyle.MEDIUM);
                cellStyle.setBorderTop(BorderStyle.MEDIUM);
                cellStyle.setBorderRight(BorderStyle.MEDIUM);
                cellStyle.setBorderLeft(BorderStyle.MEDIUM);
                return cellStyle;
        }
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.DOUBLE);
        cellStyle.setBorderTop(BorderStyle.DOUBLE);
        cellStyle.setBorderRight(BorderStyle.DOUBLE);
        cellStyle.setBorderLeft(BorderStyle.DOUBLE);
        return cellStyle;
    }
}
