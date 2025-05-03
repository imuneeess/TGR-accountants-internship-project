package org.webserv.services;

import org.springframework.stereotype.Service;
import org.webserv.models.AccountingDay;
import org.webserv.repository.AccountingDayRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AccountingDayService {

    private final AccountingDayRepository repo;

    public AccountingDayService(AccountingDayRepository repo) {
        this.repo = repo;
    }

    // Get all accounting days for a given email
    public List<AccountingDay> getDays(String email) {
        List<AccountingDay> result = repo.findByEmail(email);
        return result;
    }

    // Get accounting days by date
    public List<AccountingDay> getAccountingDaysByDate(String date) {
        return repo.findByDate(date); // This method will return all the accounting days for a given date
    }

    // Validate a specific accounting day
    public AccountingDay validateDay(String email, String date) {
        AccountingDay day = repo.findByEmailAndDate(email, date);

        if (day != null) {
            day.setValidated(true);
            return repo.save(day);
        } else {
            return null;
        }
    }

    // Get or create accounting days for a user
    public List<AccountingDay> getOrCreateDaysForUser(String email) {
        List<AccountingDay> days = repo.findByEmail(email);
        LocalDate today = LocalDate.now();

        if (days == null || days.isEmpty()) {
            List<AccountingDay> newDays = new ArrayList<>();
            for (int i = 0; i < 365; i++) {
                LocalDate date = today.minusDays(i);
                AccountingDay day = new AccountingDay();
                day.setEmail(email);
                day.setDate(date.toString());
                day.setValidated(false);
                newDays.add(day);
            }
            repo.saveAll(newDays);
            days = newDays;
        } else {
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
        }

        return days.stream()
                .sorted(Comparator.comparing(AccountingDay::getDate).reversed())
                .collect(Collectors.toList());
    }

    // Get statistics for the admin dashboard
    public Map<String, Object> getAdminStats() {
        List<AccountingDay> allDays = repo.findAll();
        long total = allDays.size();
        long validated = allDays.stream().filter(AccountingDay::isValidated).count();
        long nonValidated = allDays.stream().filter(day -> !day.isValidated()).count(); // Count non-validated days

        Set<String> uniqueEmails = allDays.stream()
                .map(AccountingDay::getEmail)
                .collect(Collectors.toSet());

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalAccountants", uniqueEmails.size());
        stats.put("validatedDays", validated);
        stats.put("nonValidatedDays", nonValidated);  // Correctly display the number of non-validated days
        return stats;
    }
}
