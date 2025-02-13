package org.webserv.repository;

import org.webserv.models.AccountingReport;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountingReportRepository extends MongoRepository<AccountingReport, String> {
}
