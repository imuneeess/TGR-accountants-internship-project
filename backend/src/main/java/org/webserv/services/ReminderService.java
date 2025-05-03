package org.webserv.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.webserv.models.AccountingDay;
import org.webserv.models.User;
import org.webserv.repository.AccountingDayRepository;
import org.webserv.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReminderService {

    @Autowired
    private AccountingDayRepository accountingDayRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private JavaMailSender mailSender;

    // ⏰ Runs every day at 10:00 AM
    @Scheduled(cron = "0 0 10 * * *")
    public void sendUnvalidatedDayReminders() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        String dateStr = yesterday.toString();

        System.out.println("📬 Sending reminder emails for: " + dateStr);

        List<AccountingDay> unvalidatedDays = accountingDayRepo.findByDate(dateStr).stream()
                .filter(day -> !day.isValidated())
                .collect(Collectors.toList());

        for (AccountingDay day : unvalidatedDays) {
            User user = userRepo.findByEmail(day.getEmail()).orElse(null);
            if (user != null && user.getNotificationEmail() != null && !user.getNotificationEmail().isBlank()) {
                sendEmail(user.getNotificationEmail(), day.getDate());
            }
        }
    }

    private void sendEmail(String toEmail, String date) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("⏰ Rappel : Journée comptable non validée");
        message.setText("Bonjour,\n\nVous n'avez pas validé votre journée comptable du " + date + ". Merci de le faire dès que possible.\n\nCordialement,\nLe système de suivi.");
        mailSender.send(message);

        System.out.println("✅ Reminder sent to: " + toEmail);
    }
}