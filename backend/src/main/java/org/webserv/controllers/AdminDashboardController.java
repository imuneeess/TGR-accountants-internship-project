package org.webserv.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.webserv.models.AccountingReport;
import org.webserv.repository.AccountingReportRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
public class AdminDashboardController {

    @Autowired
    private AccountingReportRepository reportRepository;

    @GetMapping("/reports")
    public List<AccountingReport> getAllReports(
            @RequestParam(required = false) String branchName,
            @RequestParam(required = false) Boolean submitted,
            @RequestParam(required = false) String date) {

        List<AccountingReport> reports = reportRepository.findAll();

        if (branchName != null) {
            reports = reports.stream()
                    .filter(report -> report.getBranchName().equalsIgnoreCase(branchName))
                    .collect(Collectors.toList());
        }

        if (submitted != null) {
            reports = reports.stream()
                    .filter(report -> report.isSubmitted() == submitted)
                    .collect(Collectors.toList());
        }

        if (date != null) {
            reports = reports.stream()
                    .filter(report -> report.getSubmissionDate().toString().equals(date))
                    .collect(Collectors.toList());
        }

        return reports;
    }
}
