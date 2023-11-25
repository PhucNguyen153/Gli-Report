package com.gli.report.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ExportService {


    public void createHeader(Sheet sheet, Workbook workbook, String teacher, List<String> headerTexts) {
        Row header = sheet.createRow(0);
        CellStyle cellStyle = setStyleForCell("header", workbook);
        int i = 0;
        Cell headerCell;
        for (String sub: headerTexts) {
            headerCell = header.createCell(i);
            headerCell.setCellValue(sub);
            headerCell.setCellStyle(cellStyle);
            i++;
        }

        CellStyle teacherStyle = setStyleForCell("teacher", workbook);

        headerCell = header.createCell(i);
        headerCell.setCellValue("GLV: " + teacher);
        headerCell.setCellStyle(teacherStyle);
    }

    private void createDataRow() {

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
