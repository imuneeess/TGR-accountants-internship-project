package org.webserv.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JiraService {

    @Value("${jira.base.url:#{null}}") // Handle null values if not set
    private String jiraBaseUrl;

    @Value("${jira.username:#{null}}")
    private String jiraUsername;

    @Value("${jira.api.token:#{null}}")
    private String jiraApiToken;

    @Value("${jira.project.key:#{null}}")
    private String jiraProjectKey;

    public String createJiraTicket(String summary, String description) {
        if (jiraBaseUrl == null || jiraUsername == null || jiraApiToken == null || jiraProjectKey == null) {
            return "JIRA API credentials not available. Mock ticket created: MOCK-123";
        }

        // In the future, this is where you will implement the real API request.
        return "Mock ticket created: MOCK-123";
    }
}
