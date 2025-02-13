package org.webserv.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document(collection = "accounting_reports")
public class AccountingReport {
    @Id
    private String id;
    private String branchName;
    private Date submissionDate;
    private boolean submitted; // Renamed from isSubmitted to submitted
    private String submittedBy;

    public AccountingReport() {}

    public AccountingReport(String branchName, Date submissionDate, boolean submitted, String submittedBy) {
        this.branchName = branchName;
        this.submissionDate = submissionDate;
        this.submitted = submitted;
        this.submittedBy = submittedBy;
    }

    // Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getBranchName() { return branchName; }
    public void setBranchName(String branchName) { this.branchName = branchName; }

    public Date getSubmissionDate() { return submissionDate; }
    public void setSubmissionDate(Date submissionDate) { this.submissionDate = submissionDate; }

    public boolean isSubmitted() { return submitted; }
    public void setSubmitted(boolean submitted) { this.submitted = submitted; }

    public String getSubmittedBy() { return submittedBy; }
    public void setSubmittedBy(String submittedBy) { this.submittedBy = submittedBy; }
}
