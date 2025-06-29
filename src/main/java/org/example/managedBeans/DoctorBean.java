package org.example.managedBeans;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.example.dao.DoctorDAO;
import org.example.enums.Speciality;
import org.example.models.entities.Doctor;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Named
@SessionScoped
public class DoctorBean implements Serializable {
    private Optional<Doctor> doctor;
    private List<Doctor> doctors;
    private List<Doctor> filteredDoctors;

    @Inject
    private DoctorDAO doctorDAO;

    @PostConstruct
    public void init() {
        doctor = Optional.of(new Doctor());
        doctors = doctorDAO.getAll();
    }

    public void create() {
        doctorDAO.saveToDB(doctor);
        doctors = doctorDAO.getAll();
        doctor = Optional.of(new Doctor());
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage("Doctor created successfully"));
    }

    public void update() {
        doctorDAO.saveToDB(doctor);
        doctors = doctorDAO.getAll();
        doctor = Optional.of(new Doctor());
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage("Doctor updated successfully"));
    }

    public void delete(Long id) {
        doctorDAO.deleteByID(id);
        doctors = doctorDAO.getAll();
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage("Doctor deleted successfully"));
    }

    public void loadDoctor(Long id) {
        doctor = doctorDAO.getById(id);
        if (doctor.isEmpty()) {
            doctor = Optional.of(new Doctor());
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Doctor not found", null));
        }
    }

    public List<Doctor> getDoctorsBySpeciality(Speciality speciality) {
        return doctorDAO.getDoctorsBySpeciality(speciality);
    }

    // Getters and Setters
    public Optional<Doctor> getDoctor() { return doctor; }
    public void setDoctor(Doctor doctor) { this.doctor = Optional.ofNullable(doctor); }
    public List<Doctor> getDoctors() { return doctors; }
    public List<Doctor> getFilteredDoctors() { return filteredDoctors; }
    public void setFilteredDoctors(List<Doctor> filteredDoctors) {
        this.filteredDoctors = filteredDoctors;
    }
    public Speciality[] getSpecialities() { return Speciality.values(); }
}