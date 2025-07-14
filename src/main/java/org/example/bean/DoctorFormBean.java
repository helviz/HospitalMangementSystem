package org.example.bean;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.example.enums.Speciality;
import org.example.models.entities.Doctor;
import org.example.models.entities.User;
import org.example.services.DoctorService;
import org.example.services.ServiceException;
import org.example.services.UserService;


import java.io.Serializable;
import java.util.Optional;

@Named("doctorFormBean")
@ViewScoped
public class DoctorFormBean implements Serializable {

    private Doctor doctor;
    private boolean editMode;
    private String userPassword;
    private Long editId;
    private String formTitle;
    private String submitButtonText;

    @Inject
    private DoctorService doctorService;

    @Inject
    private UserService userService;

    @Inject
    private NavigationBean navigationBean;





    @PostConstruct
    public void init() {
        Object flashDoctor = FacesContext.getCurrentInstance()
                .getExternalContext().getFlash().get("doctorToEdit");

        if (flashDoctor instanceof Doctor doctorToEdit) {
            this.doctor = doctorToEdit;
            this.editMode = true;
            this.formTitle = "Edit Doctor";
            this.submitButtonText = "Update Doctor";
        } else {
            this.doctor = new Doctor();
            this.editMode = false;
            this.formTitle = "Add New Doctor";
            this.submitButtonText = "Save Doctor";
        }
    }


    private void initializeDoctor() {
        this.doctor = new Doctor();
        this.doctor.setUser(new User());
        this.editMode = false;
        this.userPassword = null;
        this.editId = null;
    }

    private void ensureUserExists() {
        if (doctor.getUser() == null) {
            doctor.setUser(new User());
        }
    }

    public String saveDoctor() {
        System.out.println("Save method called");

        if (doctor == null) {
            System.out.println("ERROR: Doctor object is null!");
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Doctor data is missing."));
            return navigationBean.navigateToDoctorForm(); // fallback
        }

        try {
            ensureUserExists();

            String email = doctor.getEmail().trim().toLowerCase();

            // Check if email is already used
            Optional<User> existingUserOpt = userService.findByEmail(email);

            if (existingUserOpt.isPresent()) {
                User existingUser = existingUserOpt.get();
                Long currentUserId = doctor.getUser().getUserId();

                if (currentUserId == null || !existingUser.getUserId().equals(currentUserId)) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Email address is already in use."));
                    return null;
                }
            }

            // Set password if new or userPassword provided
            if (doctor.getDoctorID() == null || (userPassword != null && !userPassword.isEmpty())) {
                doctor.getUser().setPassword(userPassword);
            }

            // Set bidirectional relationship
            doctor.getUser().setDoctor(doctor);
            doctor.setUser(doctor.getUser());

            boolean saved = doctorService.saveDoctor(doctor);
            System.out.println("Save result: " + saved);
            System.out.println("Doctor details: " + doctor);

            if (saved) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Doctor saved successfully."));
                resetForm();
                return navigationBean.navigateToStaffList();
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to save doctor."));
                return null;
            }

        } catch (ServiceException e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error saving doctor: " + e.getMessage()));
            return null;
        }
    }





    public void loadDoctor(Long doctorId) {
        System.out.println("Load doctor called with ID: " + doctorId);

        if (doctorId != null) {
            doctor = doctorService.getDoctorById(doctorId).orElse(new Doctor());
            ensureUserExists();
            this.editMode = doctor.getDoctorID() != null;
            this.editId = doctorId;
        } else {
            initializeDoctor();
        }

        updateFormTitles();
    }

    public void resetForm() {
        System.out.println("Reset form called");
        initializeDoctor();
        updateFormTitles();

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Form has been reset."));
    }

    private void updateFormTitles() {
        if (editMode && doctor != null && doctor.getDoctorID() != null) {
            this.formTitle = "Edit Doctor";
            this.submitButtonText = "Update Doctor";
        } else {
            this.formTitle = "Add New Doctor";
            this.submitButtonText = "Save Doctor";
        }
    }


    public String cancelForm() {
        doctor = new Doctor();


        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
                        "Form has been cancelled."));

        return navigationBean.navigateToStaffList();
    }

    // === Getters and Setters ===

    public Doctor getDoctor() {
        if (doctor == null) {
            initializeDoctor();
        } else {
            ensureUserExists();
        }
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
        ensureUserExists();
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
        updateFormTitles();
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public Long getEditId() {
        return editId;
    }

    public void setEditId(Long editId) {
        this.editId = editId;
    }

    public String getFormTitle() {
        return formTitle;
    }

    public String getSubmitButtonText() {
        return submitButtonText;
    }

    public Speciality[] getSpecialities() {
        return Speciality.values();
    }
}
