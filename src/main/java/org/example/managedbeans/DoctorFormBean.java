package org.example.managedbeans;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.example.enums.Speciality;
import org.example.models.entities.Doctor;
import org.example.models.entities.Doctor;
import org.example.models.entities.User;
import org.example.services.DoctorService;
import org.example.services.ServiceException;


import java.io.Serializable;

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

    @PostConstruct
    public void init() {
        System.out.println("DoctorFormBean initialized");
        initializeDoctor();
        updateFormTitles();
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

        try {
            ensureUserExists();

            // Always set the user password from the form
            doctor.getUser().setPassword(userPassword);

            // Set the bidirectional relationship correctly
            doctor.getUser().setDoctor(doctor);
            doctor.setUser(doctor.getUser());

            boolean saved = doctorService.saveDoctor(doctor);
            System.out.println("Save result: " + saved);
            System.out.println("Doctor details: " + doctor.toString());

            if (saved) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Doctor saved successfully."));

                // Always reset form after successful save
                resetForm();

            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to save doctor."));
            }

        } catch (ServiceException e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error saving doctor: " + e.getMessage()));
        }

        return "/pages/doctors/list.xhtml?faces-redirect=true";
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

//    public String saveDoctor() {
//        System.out.println("saveDoctor method called");
//
//        try {
//            if (doctor != null) {
//                // Debug: Print doctor details
//                System.out.println("Doctor details: " + doctor.toString());
//
//                // Check if doctorService is properly injected
//                if (doctorService == null) {
//                    System.out.println("ERROR: DoctorService is null!");
//                    FacesContext.getCurrentInstance().addMessage(null,
//                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
//                                    "Service injection failed. Please check your configuration."));
//                    return null;
//                }
//
//                boolean result = doctorService.saveDoctor(doctor);
//                System.out.println("Save result: " + result);
//
//                if (result) {
//                    FacesContext.getCurrentInstance().addMessage(null,
//                            new FacesMessage(FacesMessage.SEVERITY_INFO, "Success",
//                                    "Doctor has been successfully saved!"));
//                    resetForm();
//                } else {
//                    FacesContext.getCurrentInstance().addMessage(null,
//                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
//                                    "Failed to save doctor to database."));
//                }
//            } else {
//                System.out.println("ERROR: Doctor object is null!");
//                FacesContext.getCurrentInstance().addMessage(null,
//                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
//                                "Doctor data is missing."));
//            }
//        } catch (Exception e) {
//            System.out.println("Exception in saveDoctor method: " + e.getMessage());
//            e.printStackTrace();
//
//            FacesContext.getCurrentInstance().addMessage(null,
//                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
//                            "Failed to save doctor: " + e.getMessage()));
//        }
//
//        return "/pages/doctors/list.xhtml?faces-redirect=true";
//    }




    public String cancelForm() {
        doctor = new Doctor();


        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
                        "Form has been cancelled."));

        return "/pages/doctors/list.xhtml?faces-redirect=true";
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
