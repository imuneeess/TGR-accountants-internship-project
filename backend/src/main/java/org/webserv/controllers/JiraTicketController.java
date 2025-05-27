package org.webserv.controllers;

import org.webserv.models.JiraTicket;
import org.webserv.repository.JiraTicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@RestController
//@RequestMapping("/jira")
public class JiraTicketController {

    @Autowired
    private JiraTicketRepository jiraRepository;

    @GetMapping
    public List<JiraTicket> getAllJiraTickets() {
        return jiraRepository.findAll();
    }

    @PostMapping("/jira/create-ticket") // Change this from "/jira/create"
    public JiraTicket createJiraTicket(@RequestBody JiraTicket ticket) {
        return jiraRepository.save(ticket);
    }
}
