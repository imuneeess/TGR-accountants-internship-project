package org.webserv.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.webserv.services.JiraService;
import java.util.Map;

@RestController
@RequestMapping("/jira")
public class JiraController {

    @Autowired
    private JiraService jiraService;

    // ✅ Accept both JSON and Query Params
    @PostMapping("/create")
    public ResponseEntity<String> createJiraTicket(
            @RequestParam(required = false) String summary,
            @RequestParam(required = false) String description,
            @RequestBody(required = false) Map<String, String> body) {

        // If body is present, extract values
        if (body != null) {
            summary = body.getOrDefault("summary", summary);
            description = body.getOrDefault("description", description);
        }

        if (summary == null || description == null) {
            return ResponseEntity.badRequest().body("Error: Missing summary or description");
        }

        String response = jiraService.createJiraTicket(summary, description);
        return ResponseEntity.ok(response);
    }
}
