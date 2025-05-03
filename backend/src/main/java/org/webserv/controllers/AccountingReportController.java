package org.webserv.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.webserv.models.AccountingDay;
import org.webserv.services.AccountingDayService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accounting-report")
public class AccountingReportController {

    @Autowired
    private AccountingDayService service;

    @GetMapping("/{email}")
    public List<AccountingDay> getAllDays(@PathVariable String email) {
        return service.getOrCreateDaysForUser(email);
    }

    @PostMapping("/validate")
    public AccountingDay validateDay(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String date = payload.get("date");
        return service.validateDay(email, date);
    }

    @GetMapping("/admin/stats")
    public Map<String, Object> getAdminStats() {
        return service.getAdminStats();
    }
}