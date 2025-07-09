package org.example.managedbeans;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.example.enums.Role;
import org.example.models.entities.Staff;
import org.example.models.entities.User;


import org.example.services.StaffService;
import org.hibernate.service.spi.ServiceException;

import java.io.Serializable;

@Named("staffFormBean")
@ViewScoped
public class StaffFormBean implements Serializable {

    private Staff staff;
    private String userPassword;
    private boolean editMode = false;
    private String formTitle;
    private String submitButtonText;

    @Inject
    private StaffService staffService;

    @PostConstruct
    public void init() {
        System.out.println("StaffFormBean initialized");
        staff = new Staff();
        staff.setUser(new User());
        setFormTitles();
    }

    public String saveStaff() {
        System.out.println("Save method called");

        try {
            if (staff != null) {
                if (staff.getUser() == null) {
                    staff.setUser(new User());
                }

                // Check if staff is new based on ID being null
                boolean isNew = (staff.getStaffID() == null);

                if (isNew) {
                    staff.getUser().setPassword(userPassword);
                }

                staff.getUser().setStaff(staff); // Ensure bidirectional link

                boolean result = staffService.saveStaff(staff);
                System.out.println("Save result: " + result);

                if (result) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO, "Success",
                                    "Staff has been successfully saved."));

                    // Reset form only if it's a new entry
                    if (isNew) {
                        resetForm();
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                                    "Failed to save staff."));
                }

                return null;
            } else {
                System.out.println("ERROR: Staff object is null!");
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                                "Staff data is missing."));
            }
        } catch (ServiceException e) {
            System.out.println("Exception in save: " + e.getMessage());
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                            "Failed to save staff: " + e.getMessage()));
        }

        return "/pages/staff/list.xhtml?faces-redirect=true";
    }


    public void loadStaff(Long staffId) {
        System.out.println("Load staff called with ID: " + staffId);
        if (staffId != null) {
            staff = staffService.getStaffById(staffId).orElse(new Staff());

            if (staff.getUser() == null) {
                staff.setUser(new User());
            }

            editMode = (staff.getStaffID() != null);
            setFormTitles();
        }
    }

    public void resetForm() {
        System.out.println("Reset form called");
        staff = new Staff();
        staff.setUser(new User());
        editMode = false;
        userPassword = null;
        setFormTitles();

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Form has been reset."));
    }

    private void setFormTitles() {
        if (editMode && staff != null && staff.getStaffID() != null) {
            formTitle = "Edit Staff";
            submitButtonText = "Update Staff";
        } else {
            formTitle = "Add New Staff";
            submitButtonText = "Save Staff";
        }
    }

    public String cancelForm() {
        staff = new Staff();


        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
                        "Form has been cancelled."));

        return "/pages/staff/list.xhtml?faces-redirect=true";
    }

    // === Getters and Setters ===

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
        setFormTitles();
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getFormTitle() {
        return formTitle;
    }

    public String getSubmitButtonText() {
        return submitButtonText;
    }

    public Role[] getRoles() {
        return Role.values();
    }


}
