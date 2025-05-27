package org.webserv.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.webserv.models.JiraTicket;
import org.webserv.repository.JiraTicketRepository;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@ConditionalOnProperty(name = "jira.enabled", havingValue = "true")
@Service
public class JiraService {

    @Value("${jira.email}")
    private String email;

    @Value("${jira.token}")
    private String token;

    @Value("${jira.base-url}")
    private String baseUrl;

    private final JiraTicketRepository ticketRepo;
    private final RestTemplate restTemplate = new RestTemplate();

    public JiraService(JiraTicketRepository ticketRepo) {
        this.ticketRepo = ticketRepo;
    }

    public String fetchIssue(String issueKey) {
        String auth = Base64.getEncoder()
                .encodeToString((email + ":" + token).getBytes(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + auth);
        headers.set("Accept", "application/json");

        String url = baseUrl + "/rest/api/3/issue/" + issueKey;
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class
        );

        return response.getBody();
    }

    public boolean isTicketResolved(String issueKey) {
        try {
            String auth = Base64.getEncoder()
                    .encodeToString((email + ":" + token).getBytes(StandardCharsets.UTF_8));

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Basic " + auth);
            headers.set("Accept", "application/json");

            String url = baseUrl + "/rest/api/3/issue/" + issueKey;
            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    Map.class
            );

            Map fields = (Map) response.getBody().get("fields");
            Map status = (Map) fields.get("status");
            String statusName = (String) status.get("name");

            return statusName.equalsIgnoreCase("Done") || statusName.equalsIgnoreCase("Terminé");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public JiraTicket saveTicket(String issueKey, String branchName) {
        JiraTicket ticket = new JiraTicket(issueKey, branchName, new Date(), false);
        return ticketRepo.save(ticket);
    }

    public JiraTicket resolveTicket(String issueKey) {
        JiraTicket ticket = ticketRepo.findByIssueKey(issueKey);
        if (ticket != null) {
            ticket.setResolved(true);
            ticketRepo.save(ticket);
        }
        return ticket;
    }
}
