package org.webserv.repository;

import org.webserv.models.JiraTicket;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JiraTicketRepository extends MongoRepository<JiraTicket, String> {
}
