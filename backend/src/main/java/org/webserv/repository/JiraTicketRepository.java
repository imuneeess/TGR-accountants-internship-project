package org.webserv.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.webserv.models.JiraTicket;

public interface JiraTicketRepository extends MongoRepository<JiraTicket, String> {
    JiraTicket findByIssueKey(String issueKey);
}
