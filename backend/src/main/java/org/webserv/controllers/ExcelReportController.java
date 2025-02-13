package org.webserv.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.webserv.services.ExcelReportService;

import java.io.IOException;

@RestController
@RequestMapping("/admin")
public class ExcelReportController {

    @Autowired
    private ExcelReportService excelReportService;

    @GetMapping("/download-excel")
    public ResponseEntity<byte[]> downloadExcelReport() throws IOException {
        byte[] excelData = excelReportService.generateExcelReport();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Accounting_Reports.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelData);
    }
}
