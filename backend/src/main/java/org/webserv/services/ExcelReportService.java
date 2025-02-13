package org.webserv.services;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.webserv.models.AccountingReport;
import org.webserv.repository.AccountingReportRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelReportService {

    private final AccountingReportRepository reportRepository;

    public ExcelReportService(AccountingReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public byte[] generateExcelReport() throws IOException {
        List<AccountingReport> reports = reportRepository.findAll();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Accounting Reports");

        // Create Header Row
        Row headerRow = sheet.createRow(0);
        String[] columns = {"Branch Name", "Submission Date", "Submitted", "Submitted By"};
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(createHeaderStyle(workbook));
        }

        // Populate Data
        int rowNum = 1;
        for (AccountingReport report : reports) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(report.getBranchName());
            row.createCell(1).setCellValue(report.getSubmissionDate().toString());
            row.createCell(2).setCellValue(report.isSubmitted() ? "Yes" : "No");
            row.createCell(3).setCellValue(report.getSubmittedBy());
        }

        // Auto-size columns
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Convert to byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        return outputStream.toByteArray();
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }
}
