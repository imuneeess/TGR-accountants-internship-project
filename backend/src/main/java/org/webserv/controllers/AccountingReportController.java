package org.webserv.controllers;

import org.webserv.models.AccountingReport;
import org.webserv.repository.AccountingReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reports")
@CrossOrigin(origins = "http://localhost:5173")
public class AccountingReportController {

    @Autowired
    private AccountingReportRepository reportRepository;

    @GetMapping
    public List<AccountingReport> getAllReports() {
        try {
            List<AccountingReport> reports = reportRepository.findAll();
            System.out.println("Fetched " + reports.size() + " reports.");
            return reports;
        } catch (Exception e) {
            System.err.println("Error fetching reports: " + e.getMessage());
            return List.of(); // Return an empty list on failure
        }
    }

    @PostMapping("/submit")
    public AccountingReport submitReport(@RequestBody AccountingReport report) {
        try {
            if (report.getBranchName() == null || report.getSubmittedBy() == null) {
                throw new IllegalArgumentException("Branch name and submittedBy fields are required.");
            }
            AccountingReport savedReport = reportRepository.save(report);
            System.out.println("Report submitted: " + savedReport);
            return savedReport;
        } catch (Exception e) {
            System.err.println("Error submitting report: " + e.getMessage());
            return null; // Handle error more effectively in a real application
        }
    }
}
