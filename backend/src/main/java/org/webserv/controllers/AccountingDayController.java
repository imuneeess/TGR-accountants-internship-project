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

    @GetMapping("/{email}")
    public List<AccountingDay> getDays(@PathVariable String email) {
        return accountingDayService.getDays(email);
    }

    @PostMapping("/validate")
    public AccountingDay validateDay(@RequestParam String email, @RequestParam String date) {
        return accountingDayService.validateDay(email, date);
    }

    @GetMapping("/get-or-create/{email}")
    public List<AccountingDay> getOrCreateDays(@PathVariable String email) {
        return accountingDayService.getOrCreateDaysForUser(email);
    }

    @GetMapping("/status/today")
    public List<AccountingDay> getTodayStatus() {
        LocalDate today = LocalDate.now();
        return accountingDayService.getAccountingDaysByDate(today.toString());
    }

    @GetMapping("/admin/stats")
    public Map<String, Object> getAdminStats() {
        return accountingDayService.getAdminStats();
    }
}
