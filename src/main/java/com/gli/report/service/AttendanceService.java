package com.gli.report.service;

import com.gli.report.entity.*;
import com.gli.report.model.*;
import com.gli.report.repository.AttendanceRepo;
import com.gli.report.repository.CommentSemesterRepo;
import com.gli.report.repository.StudentGradeRepo;
import com.gli.report.repository.StudentRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
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
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepo attendanceRepo;
    private final StudentGradeRepo studentGradeRepo;
    private final StudentRepo studentRepo;
    private final ScholasticService scholasticService;
    private final CommentSemesterRepo commentSemesterRepo;
    private final GradeService gradeService;
    private final ExportService exportService;

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

        //Get all student by class ids and map by class
        List<StudentGrade> sgLst = studentGradeRepo.findAllStudentByGrade(request.getGradeIds());
        Map<Grade, List<StudentGrade>> mapByGrade = sgLst.stream().collect(Collectors.groupingBy(StudentGrade::getGrade));
        
        //Get all attendances and remove duplicate
//        List<Attendance> arrayList = attendanceRepo.findAllByGradeIdAndTime(request.getGradeIds(), request.getStartDate(), request.getEndDate());
        List<Attendance> arrayList = attendanceRepo.findAllByStudentIdAndTime(sgLst.stream().map(s->s.getStudent().getId()).collect(Collectors.toList()),
                request.getStartDate(), request.getEndDate());
        List<AttendanceResponse> result = new ArrayList<>();
        Set<Attendance> uniqueElements = new HashSet<>(arrayList);
        //Map all attendance by student id
        Map<String, List<Attendance>> mapByStudent = uniqueElements.stream().collect(Collectors.groupingBy(a -> a.getStudent().getId()));

        //Get rule Mass Day
        List<DayOfWeek> ruleMassDay = gradeService.getRuleMassByGradeId(request.getGradeIds().get(0));

        //Sort
        TreeMap<Grade, List<StudentGrade>> sorted = new TreeMap<>(mapByGrade);

        sorted.forEach((grade, sLst) -> {
            Collator esCollator = Collator.getInstance(new Locale("vi", "VN"));
            sLst.sort((s1, s2) -> {
                return esCollator.compare(s1.getStudent().getFirstName(), s2.getStudent().getFirstName());
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
                List<AttendanceDetail> details = new ArrayList<>();
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
                        AttendanceDetail detail = new AttendanceDetail(a.getAttendanceDate(), ruleMassDay.contains(a.getAttendanceDate().getDayOfWeek()));
                        details.add(detail);
                    }
                }
                details.sort(Comparator.comparing(AttendanceDetail::getDate));
                res.setDetails(details);
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
        TreeMap<String, List<AttendanceResponse>> sorted = new TreeMap<>(mapByGradeName);
        Workbook workbook = new XSSFWorkbook();
        sorted.forEach((gName, aLst) -> {
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
        headerCell = header.createCell(8);
        headerCell.setCellValue("Tổng lễ");
        headerCell.setCellStyle(cellStyle);

        CellStyle teacherStyle = setStyleForCell("teacher", workbook);

        headerCell = header.createCell(9);
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
            cell = row.createCell(8);
            cell.setCellValue(ar.getWeekDay() + ar.getSunday());
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

    public List<StudentInfo> search(SearchRequest request) {
        int latestScholastic = scholasticService.findLatest();
        List<Object[]> searchObjects = studentRepo.searchByNameAndScholasticId(request.getName(), latestScholastic, request.getGradeId());
        return mapperObject(searchObjects);
    }

    private List<StudentInfo> mapperObject(List<Object[]> rootObjects) {
        List<StudentInfo> result = new ArrayList<>();
        for (Object[] ob: rootObjects) {
            StudentInfo si = new StudentInfo();
            si.setId(String.valueOf(ob[0]));
            StringBuilder fullName = new StringBuilder();
            fullName.append(String.valueOf(ob[1]))
                    .append(" ")
                    .append(String.valueOf(ob[2]))
                    .append(" ")
                    .append(String.valueOf(ob[3]));
            si.setFullName(fullName.toString());
            si.setFather(String.valueOf(ob[4]));
            si.setMother(String.valueOf(ob[5]));
            StringBuilder teacherFullName = new StringBuilder();
            teacherFullName.append(String.valueOf(ob[6]))
                           .append(" ")
                           .append(String.valueOf(ob[7]))
                           .append(" ")
                           .append(String.valueOf(ob[8]));
            si.setTeacher(teacherFullName.toString());
            List<String> phoneNumbers = new ArrayList<>();
            if (!ObjectUtils.isEmpty(ob[9])) phoneNumbers.add(String.valueOf(ob[9]));
            if (!ObjectUtils.isEmpty(ob[10])) phoneNumbers.add(String.valueOf(ob[10]));
            si.setPhoneNumbers(phoneNumbers);
            si.setTeacherPhoneNumber(String.valueOf(ob[11]));
            si.setGrade(String.valueOf(ob[12]));
            si.setUnit(String.valueOf(ob[13]));
            si.setDiocese(String.valueOf(ob[14]));
            si.setNote(ob[15] == null ? "" : String.valueOf(ob[15]));
            si.setBod(ob[16] == null ? "" : String.valueOf(ob[16]).split(" ")[0]);
            si.setStatus(ob[17] == null ? null : Integer.parseInt(String.valueOf(ob[17])));
//            List<Object[]> commentLst = commentSemesterRepo.getCommentByStudentAndScholastic(si.getId(), scholasticService.findLatest());
//            if (!ObjectUtils.isEmpty(commentLst)) {
//                Map<String, String> cmts = new HashMap<>();
//                for (Object[] cmt: commentLst) {
//                    cmts.put(String.valueOf(cmt[0]), String.valueOf(cmt[1]));
//                }
//                si.setComments(cmts);
//            }
            result.add(si);
        }
        return result;
    }

    public ByteArrayInputStream exportExcelDetail(AttendanceRequest request) throws IOException {
        List<AttendanceResponse> data = getAttendanceByTimeV2(request);
        Map<String, List<AttendanceResponse>> mapByGradeName = data.stream().collect(Collectors.groupingBy(AttendanceResponse::getGradeName));
        TreeMap<String, List<AttendanceResponse>> sorted = new TreeMap<>(mapByGradeName);
        Workbook workbook = new XSSFWorkbook();
        List<String> headerTexts = new ArrayList<>(Arrays.asList("ID","Tên Thánh", "Họ Tên", "Ngày tham dự Thánh Lễ", "Ngày Lễ của Ngành"));
        sorted.forEach((gName, aLst) -> {
            Sheet sheet = workbook.createSheet(gName);
            //Create data for header
            exportService.createHeader(sheet, workbook, aLst.get(0).getTeacher(), headerTexts);

            //Create data for each row
            CellStyle normalStyle = setStyleForCell("normal", workbook);
            createDataRowDetail(sheet, normalStyle, aLst);
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

    private void createDataRowDetail(Sheet sheet, CellStyle cellStyle, List<AttendanceResponse> aLst) {
        int rowNum = 1;
        for (AttendanceResponse ar: aLst) {
            for (AttendanceDetail detail: ar.getDetails()) {
                Row row = sheet.createRow(rowNum);
                Cell cell = row.createCell(0);
                cell.setCellValue(ar.getId());
                cell.setCellStyle(cellStyle);
                cell = row.createCell(1);
                cell.setCellValue(ar.getHolyName());
                cell.setCellStyle(cellStyle);
                cell = row.createCell(2);
                cell.setCellValue(ar.getFullName());
                cell.setCellStyle(cellStyle);
                cell = row.createCell(3);
                cell.setCellValue(detail.getDate().toString());
                cell.setCellStyle(cellStyle);
                cell = row.createCell(4);
                cell.setCellValue(detail.isValidDate() ? "x" : "");
                cell.setCellStyle(cellStyle);
                rowNum++;
            }
        }
    }

}
