package org.example.managedBeans;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.example.dao.PatientDAO;
import org.example.models.entities.Patient;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Named
@SessionScoped
public class PatientBean implements Serializable {
    private Optional<Patient> patient;
    private List<Patient> patients;
    private List<Patient> filteredPatients;

    @Inject
    private PatientDAO patientDAO;

    @PostConstruct
    public void init() {
        patient = Optional.of(new Patient());
        patients = patientDAO.getAll();
    }

    public void create() {
        patientDAO.saveToDB(patient);
        patients = patientDAO.getAll();
        patient = Optional.of(new Patient());
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage("Patient created successfully"));
    }

    public void update() {
        patientDAO.saveToDB(patient);
        patients = patientDAO.getAll();
        patient = Optional.of(new Patient());
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage("Patient updated successfully"));
    }

    public void delete(Long id) {
        patientDAO.deleteByID(id);
        patients = patientDAO.getAll();
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage("Patient deleted successfully"));
    }

    public void loadPatient(Long id) {
        patient = patientDAO.getById(id);
        if (patient.isEmpty()) {
            patient = Optional.of(new Patient());
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Patient not found", null));
        }
    }

    // Getters and Setters
    public Optional<Patient> getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = Optional.ofNullable(patient); }
    public List<Patient> getPatients() { return patients; }
    public List<Patient> getFilteredPatients() { return filteredPatients; }
    public void setFilteredPatients(List<Patient> filteredPatients) {
        this.filteredPatients = filteredPatients;
    }
}