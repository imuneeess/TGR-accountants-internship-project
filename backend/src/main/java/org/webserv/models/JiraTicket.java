package org.webserv.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document(collection = "jira_tickets")
public class JiraTicket {
    @Id
    private String id;
    private String issueKey;
    private String branchName;
    private Date createdAt;
    private boolean resolved;

    public JiraTicket() {}

    public JiraTicket(String issueKey, String branchName, Date createdAt, boolean resolved) {
        this.issueKey = issueKey;
        this.branchName = branchName;
        this.createdAt = createdAt;
        this.resolved = resolved;
    }

    // Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getIssueKey() { return issueKey; }
    public void setIssueKey(String issueKey) { this.issueKey = issueKey; }

    public String getBranchName() { return branchName; }
    public void setBranchName(String branchName) { this.branchName = branchName; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public boolean isResolved() { return resolved; }
    public void setResolved(boolean resolved) { this.resolved = resolved; }
}
