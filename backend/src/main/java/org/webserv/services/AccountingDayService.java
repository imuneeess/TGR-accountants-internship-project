package org.webserv.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webserv.models.AccountingDay;
import org.webserv.repository.AccountingDayRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AccountingDayService {

    private final AccountingDayRepository repo;

    @Autowired
    public AccountingDayService(AccountingDayRepository repo) {
        this.repo = repo;
    }

    public List<AccountingDay> getDays(String email) {
        return repo.findByEmail(email).stream()
                .filter(day -> LocalDate.parse(day.getDate()).isAfter(LocalDate.now().minusDays(90)))
                .sorted(Comparator.comparing(AccountingDay::getDate).reversed())
                .collect(Collectors.toList());
    }

    public List<AccountingDay> getAccountingDaysByDate(String date) {
        return repo.findByDate(date);
    }

    public AccountingDay validateDay(String email, String date) {
        AccountingDay day = repo.findByEmailAndDate(email, date);
        if (day != null) {
            System.out.printf("✅ Validating %s for %s: Directly setting as validated%n", date, email);
            day.setValidated(true);
            return repo.save(day);
        }
        return null;
    }

    public List<AccountingDay> getOrCreateDaysForUser(String email) {
        List<AccountingDay> days = repo.findByEmail(email);
        LocalDate today = LocalDate.now();

        Set<String> existingDates = days.stream().map(AccountingDay::getDate).collect(Collectors.toSet());
        List<AccountingDay> newDays = new ArrayList<>();
        for (int i = 0; i < 365; i++) {
            LocalDate date = today.minusDays(i);
            if (!existingDates.contains(date.toString())) {
                AccountingDay newDay = new AccountingDay();
                newDay.setEmail(email);
                newDay.setDate(date.toString());
                newDay.setValidated(false);
                newDays.add(newDay);
            }
        }
        if (!newDays.isEmpty()) {
            repo.saveAll(newDays);
            days.addAll(newDays);
        }

        return days.stream()
                .sorted(Comparator.comparing(AccountingDay::getDate).reversed())
                .collect(Collectors.toList());
    }

    public Map<String, Object> getAdminStats() {
        List<AccountingDay> allDays = repo.findAll();
        long validated = allDays.stream().filter(AccountingDay::isValidated).count();
        long nonValidated = allDays.stream().filter(day -> !day.isValidated()).count();
        Set<String> uniqueEmails = allDays.stream().map(AccountingDay::getEmail).collect(Collectors.toSet());

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalAccountants", uniqueEmails.size());
        stats.put("validatedDays", validated);
        stats.put("nonValidatedDays", nonValidated);
        return stats;
    }
}
