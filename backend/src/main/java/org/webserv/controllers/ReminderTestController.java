package org.webserv.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.webserv.scheduler.ReminderScheduler;

@RestController
public class ReminderTestController {

    @Autowired
    private ReminderScheduler scheduler;

    @GetMapping("/trigger-morning-reminder")
    public String triggerMorningReminder() {
        scheduler.triggerMorningReminderForTesting();
        return "Morning reminder triggered!";
    }

    @GetMapping("/trigger-evening-reminder")
    public String triggerEveningReminder() {
        scheduler.triggerEveningReminderForTesting();
        return "Evening reminder triggered!";
    }
}
