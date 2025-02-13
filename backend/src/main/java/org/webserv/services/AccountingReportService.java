package org.webserv.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.webserv.models.AccountingReport;
import org.webserv.repository.AccountingReportRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountingReportService {

    @Autowired
    private AccountingReportRepository reportRepository;

    @Autowired
    private EmailService emailService;

    // Scheduled job to send report status at 10 AM and 4 PM daily
    @Scheduled(cron = "0 0 10,16 * * *") // Runs at 10:00 AM and 4:00 PM
    public void sendDailyReport() {
        System.out.println("Executing scheduled task: Sending daily report...");
        List<AccountingReport> pendingReports = reportRepository.findAll()
                .stream()
                .filter(report -> !report.isSubmitted())
                .collect(Collectors.toList());

        if (!pendingReports.isEmpty()) {
            StringBuilder emailContent = new StringBuilder("List of unsubmitted reports:\n");
            for (AccountingReport report : pendingReports) {
                emailContent.append("- ").append(report.getBranchName()).append(" (")
                        .append(report.getSubmissionDate()).append(")\n");
            }

            emailService.sendEmail("younesselouazzani12333@gmail.com",
                    "Daily Report - Unsubmitted Reports",
                    emailContent.toString());
        } else {
            System.out.println("No pending reports to send.");
        }
    }

    // Scheduled Reminder Email for Accountants at 11:30 AM
    @Scheduled(cron = "0 30 11 * * *") // Runs at 11:30 AM daily
    public void sendReminderEmails() {
        System.out.println("Executing scheduled task: Sending reminder emails...");
        List<AccountingReport> pendingReports = reportRepository.findAll()
                .stream()
                .filter(report -> !report.isSubmitted())
                .collect(Collectors.toList());

        if (!pendingReports.isEmpty()) {
            for (AccountingReport report : pendingReports) {
                emailService.sendEmail(
                        report.getSubmittedBy(),
                        "Reminder: Please Submit Your Report",
                        "Dear Accountant,\n\nYour report for " + report.getBranchName() + " is still pending. "
                                + "Please submit it as soon as possible.\n\nThank you."
                );
            }
        } else {
            System.out.println("No pending reports for reminders.");
        }
    }
}
