package org.webserv.DTO;

public class CreateUserDTO {
    private String email;
    private String role; // ADMIN or ACCOUNTANT
    private String notificationEmail;

    public CreateUserDTO() {}

    public CreateUserDTO(String email, String role, String notificationEmail) {
        this.email = email;
        this.role = role;
        this.notificationEmail = notificationEmail;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getNotificationEmail() {
        return notificationEmail;
    }

    public void setNotificationEmail(String notificationEmail) {
        this.notificationEmail = notificationEmail;
    }
}
