package org.example.bean;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.example.models.entities.User;
import org.example.services.ServiceException;
import org.example.services.UserService;

import java.io.Serializable;


@Named("UserManagedBean")
@SessionScoped
public class UserBean implements Serializable {

    @Inject
    private UserService userService;

    @Inject
    private NavigationBean navigationBean;

    private String email;
    private String password;
    private String newEmail;
    private String newPassword;
    private Long userId; // For updates or deletes
    private User currentUser; // Holds authenticated user info

    public String login() {
        try {
            currentUser = userService.authenticate(email, password);
            return "/pages/dashboard/dashboard.xhtml?faces-redirect=true";
        } catch (ServiceException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
        }
        return null;
    }

    public void register() {
        try {
            currentUser = userService.registerUser(email, password);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Registration successful", null));
            // Redirect or update UI here
        } catch (ServiceException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
        }
    }

    public void updateEmail() {
        try {
            userService.updateUserEmail(userId, newEmail);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Email updated successfully", null));
        } catch (ServiceException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
        }
    }

    public void updatePassword() {
        try {
            userService.updateUserPassword(userId, newPassword);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Password updated successfully", null));
        } catch (ServiceException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
        }
    }

    public void deleteUser() {
        try {
            userService.deleteUser(userId);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "User deleted successfully", null));
        } catch (ServiceException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
        }
    }


    public String logout(){
        return navigationBean.logout();
    }

    // =======================
    // Getters and Setters
    // =======================
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
