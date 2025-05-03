package org.webserv.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.webserv.models.AccountingDay;
import org.webserv.models.User;
import org.webserv.repository.AccountingDayRepository;
import org.webserv.repository.UserRepository;
import org.webserv.services.EmailService;

import java.time.LocalDate;
import java.util.List;

@Component
public class ReminderScheduler {

    private final AccountingDayRepository accountingDayRepo;
    private final UserRepository userRepo;
    private final EmailService emailService;

    public ReminderScheduler(AccountingDayRepository accountingDayRepo, UserRepository userRepo, EmailService emailService) {
        this.accountingDayRepo = accountingDayRepo;
        this.userRepo = userRepo;
        this.emailService = emailService;
    }

    // 🔔 Send at 10 AM every day
    @Scheduled(cron = "0 0 10 * * *")
    public void sendMorningReminders() {
        sendReminders("morning");
    }

    // 🔔 Re-send at 4 PM every day
    @Scheduled(cron = "0 0 16 * * *")
    public void sendEveningReminders() {
        sendReminders("evening");
    }

    private void sendReminders(String time) {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        List<User> accountants = userRepo.findByRole("ACCOUNTANT");

        for (User user : accountants) {
            String notifEmail = user.getNotificationEmail();
            if (notifEmail == null || notifEmail.isBlank()) continue;

            AccountingDay day = accountingDayRepo.findByEmailAndDate(user.getEmail(), yesterday.toString());
            if (day != null && !day.isValidated()) {
                emailService.sendEmail(
                        notifEmail,
                        "⏰ Reminder: Unvalidated Accounting Day",
                        "Dear Accountant,\n\nYou haven't validated your accounting day for " + yesterday +
                                ".\nPlease make sure to do so today.\n\nThis is a " + time + " reminder.\n\nRegards,\nAccounting System"
                );
                System.out.println("📧 Reminder sent to: " + notifEmail);
            }
        }
    }

    // Methods for manual testing (to trigger reminders)
    public void triggerMorningReminderForTesting() {
        sendReminders("manual morning");
    }

    public void triggerEveningReminderForTesting() {
        sendReminders("manual evening");
    }
}
