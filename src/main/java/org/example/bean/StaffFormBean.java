package org.example.bean;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.example.enums.Role;
import org.example.models.entities.Staff;
import org.example.models.entities.User;


import org.example.services.StaffService;
import org.example.services.UserService;
import org.hibernate.service.spi.ServiceException;

import java.io.Serializable;
import java.util.Optional;

@Named("staffFormBean")
@ViewScoped
public class StaffFormBean implements Serializable {

    private Staff staff;
    private String userPassword;
    private boolean editMode = false;
    private String formTitle;
    private String submitButtonText;
    private Role selectedRole;

    @Inject
    private StaffService staffService;

    @Inject
    private NavigationBean navigationBean;

    @Inject
    private UserService userService;


    
    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        Object flashStaff = context.getExternalContext().getFlash().get("staffToEdit");

        if (flashStaff instanceof Staff staffToEdit) {
            this.staff = staffToEdit;
            this.formTitle = "Edit Staff";
            this.submitButtonText = "Update";
            this.selectedRole = staff.getRole();
        } else {
            this.staff = new Staff(); // default for create mode
            this.formTitle = "Add New Staff";
            this.submitButtonText = "Save";
        }
    }


    public String saveStaff() {
        System.out.println("Save method called");

        if (staff == null) {
            System.out.println("ERROR: Staff object is null!");
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Staff data is missing."));
            return navigationBean.navigateToStaffForm(); // fallback
        }

        try {
            // Ensure user is set
            if (staff.getUser() == null) {
                staff.setUser(new User());
            }

            String email = staff.getEmail().trim().toLowerCase();

            // Check if email is already used
            Optional<User> existingUserOpt = userService.findByEmail(email);

            if (existingUserOpt.isPresent()) {
                User existingUser = existingUserOpt.get();
                Long currentUserId = staff.getUser().getUserId();

                if (currentUserId == null || !existingUser.getUserId().equals(currentUserId)) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Email address is already in use."));
                    return null;
                }
            }

            boolean isNew = (staff.getStaffID() == null);

            // Assign role and password if new or password is provided
            staff.setRole(selectedRole);
            if (isNew || (userPassword != null && !userPassword.isEmpty())) {
                staff.getUser().setPassword(userPassword);
            }

            // Set bidirectional relationship
            staff.getUser().setStaff(staff);
            staff.setUser(staff.getUser());

            boolean saved = staffService.saveStaff(staff);
            System.out.println("Save result: " + saved);
            System.out.println("Staff details: " + staff);

            if (saved) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Staff has been successfully saved."));

                if (isNew) {
                    resetForm(); // Clear form only if newly created
                }

                return navigationBean.navigateToStaffList();
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to save staff."));
                return null;
            }

        } catch (ServiceException e) {
            System.out.println("Exception in save: " + e.getMessage());
            e.printStackTrace();

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to save staff: " + e.getMessage()));
            return null;
        }
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

        return navigationBean.navigateToStaffList();
    }

    public String navigateToRoleForm() {
        if (selectedRole == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Please select a role."));
            return null;
        }

        switch (selectedRole) {
            case DOCTOR:
                return navigationBean.navigateToDoctorForm();
            case NURSE:
                return navigationBean.navigateToStaffForm();
            case RECEPTION:
                return navigationBean.navigateToStaffForm();
            default:
                return navigationBean.navigateToStaffList();
        }
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

    public void setSelectedRole(Role selectedRole){
        this.selectedRole = selectedRole;
    }

    public Role getSelectedRole(){
        return this.selectedRole;
    }


}
