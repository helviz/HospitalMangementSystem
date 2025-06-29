package org.example.managedBeans;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.example.dao.AuthenticationDAO;
import org.example.dao.UserDAO;
import org.example.models.entities.User;
import java.io.Serializable;

@Named
@SessionScoped
public class UserBean implements Serializable {
    private String email;
    private String password;
    private User loggedInUser;

    @Inject
    private AuthenticationDAO authDAO;

    @Inject
    private UserDAO userDAO;

    public String login() {
        User user = authDAO.authenticateUser(this.email, this.password);

        if (user != null) {
            loggedInUser = user;
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Login successful", null));

            switch (loggedInUser.getRole()) {
                case ADMIN:
                    return "/pages/admin/dashboard?faces-redirect=true";
                case DOCTOR:
                    return "/pages/doctor/dashboard?faces-redirect=true";
                case RECEPTION:
                    return "/pages/reception/dashboard?faces-redirect=true";
                default:
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unknown user role", null));
                    return null;
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid email or password", null));
            return null;
        }
    }

    public String logout() {
        loggedInUser = null;
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/login?faces-redirect=true";
    }

    // Getters and Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public User getLoggedInUser() { return loggedInUser; }
}