package org.webserv.DTO;

public class CreateAccountantDTO {
    private String email;
    private String notificationEmail;

    // Constructors, getters, and setters
    public CreateAccountantDTO() {}

    public CreateAccountantDTO(String email, String notificationEmail) {
        this.email = email;
        this.notificationEmail = notificationEmail;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNotificationEmail() {
        return notificationEmail;
    }

    public void setNotificationEmail(String notificationEmail) {
        this.notificationEmail = notificationEmail;
    }
}
