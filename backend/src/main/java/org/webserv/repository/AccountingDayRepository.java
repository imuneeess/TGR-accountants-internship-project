package org.webserv.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.webserv.models.AccountingDay;

import java.util.List;

public interface AccountingDayRepository extends MongoRepository<AccountingDay, String> {
    List<AccountingDay> findByEmail(String email);
    AccountingDay findByEmailAndDate(String email, String date);
    List<AccountingDay> findByDate(String date);
}
