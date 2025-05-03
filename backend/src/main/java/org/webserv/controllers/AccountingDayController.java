package org.webserv.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.webserv.services.AccountingDayService;
import org.webserv.models.AccountingDay;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accounting-days")
public class AccountingDayController {

    @Autowired
    private AccountingDayService accountingDayService;

    // Get all accounting days for a given email
    @GetMapping("/{email}")
    public List<AccountingDay> getDays(@PathVariable String email) {
        return accountingDayService.getDays(email);
    }

    // Validate a specific accounting day
    @PostMapping("/validate")
    public AccountingDay validateDay(@RequestParam String email, @RequestParam String date) {
        return accountingDayService.validateDay(email, date);
    }

    // Get or create accounting days for a given user email
    @GetMapping("/get-or-create/{email}")
    public List<AccountingDay> getOrCreateDays(@PathVariable String email) {
        return accountingDayService.getOrCreateDaysForUser(email);
    }

    // Get status of accounting days for all accountants for today
    @GetMapping("/status/today")
    public List<AccountingDay> getTodayStatus() {
        // Get today's date
        LocalDate today = LocalDate.now();
        // Fetch all accounting days for today
        return accountingDayService.getAccountingDaysByDate(today.toString());
    }

    // Get admin statistics
    @GetMapping("/admin/stats")
    public Map<String, Object> getAdminStats() {
        return accountingDayService.getAdminStats();
    }
}
