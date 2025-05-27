package org.webserv.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.webserv.services.JiraService;
import org.webserv.models.JiraTicket;

//@RestController
//@RequestMapping("/jira")
public class JiraController {

    @Autowired
    private JiraService jiraService;

    @GetMapping("/issue/{key}")
    public String getJiraIssue(@PathVariable String key) {
        return jiraService.fetchIssue(key);
    }

    @PostMapping("/track")
    public JiraTicket createLocalTicket(@RequestParam String key, @RequestParam String branch) {
        return jiraService.saveTicket(key, branch);
    }

    @PutMapping("/resolve/{key}")
    public JiraTicket resolve(@PathVariable String key) {
        return jiraService.resolveTicket(key);
    }
}
