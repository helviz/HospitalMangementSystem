package org.example.managedBeans;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.example.dao.StaffDAO;
import org.example.dao.UserDAO;
import org.example.enums.Role;
import org.example.models.entities.Staff;
import org.example.models.entities.User;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Named
@SessionScoped
public class StaffBean implements Serializable {
    private Staff staff;  // Changed from Optional<Staff> to Staff
    private List<Staff> staffList;
    private List<Staff> filteredStaff;
    private User userAccount;

    @Inject
    private StaffDAO staffDAO;

    @Inject
    private UserDAO userDAO;

    @PostConstruct
    public void init() {
        staff = new Staff();
        userAccount = new User();
        staffList = staffDAO.getAll();
    }

    public void create() {
        try {
            // Create user account first
            userDAO.saveToDB(Optional.ofNullable(userAccount));

            // Link user to staff and save
            staff.setUser(userAccount);
            staffDAO.saveToDB(Optional.ofNullable(staff));

            // Reset form
            staff = new Staff();
            userAccount = new User();
            staffList = staffDAO.getAll();

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Staff member created successfully"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to create staff member", null));
        }
    }

    public void update() {
        try {
            userDAO.saveToDB(Optional.ofNullable(userAccount));
            staffDAO.saveToDB(Optional.ofNullable(staff));
            staffList = staffDAO.getAll();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Staff member updated successfully"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to update staff member", null));
        }
    }

    public void delete(Long id) {
        try {
            staffDAO.deleteByID(id);
            staffList = staffDAO.getAll();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Staff member deleted successfully"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to delete staff member", null));
        }
    }

    public void loadStaff(Long id) {
        Optional<Staff> staffOptional = staffDAO.getById(id);
        if (staffOptional.isPresent()) {
            staff = staffOptional.get();
            if (staff.getUser() != null) {
                userAccount = staff.getUser();
            } else {
                userAccount = new User();
            }
        } else {
            staff = new Staff();
            userAccount = new User();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Staff member not found", null));
        }
    }

    // Getters and Setters
    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public List<Staff> getStaffList() {
        return staffList;
    }

    public List<Staff> getFilteredStaff() {
        return filteredStaff;
    }

    public void setFilteredStaff(List<Staff> filteredStaff) {
        this.filteredStaff = filteredStaff;
    }

    public User getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(User userAccount) {
        this.userAccount = userAccount;
    }

    public Role[] getRoles() {
        return Role.values();
    }
}