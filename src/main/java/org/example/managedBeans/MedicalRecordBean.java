package org.example.managedBeans;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.example.dao.DoctorDAO;
import org.example.dao.MedicalRecordDAO;
import org.example.dao.PatientDAO;
import org.example.models.entities.Doctor;
import org.example.models.entities.MedicalRecord;
import org.example.models.entities.Patient;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Named
@SessionScoped
public class MedicalRecordBean implements Serializable {
    private MedicalRecord medicalRecord;
    private List<MedicalRecord> medicalRecords;
    private List<MedicalRecord> filteredRecords;
    private List<Patient> patients;
    private List<Doctor> doctors;

    @Inject
    private MedicalRecordDAO medicalRecordDAO;

    @Inject
    private PatientDAO patientDAO;

    @Inject
    private DoctorDAO doctorDAO;

    @PostConstruct
    public void init() {
        medicalRecord = new MedicalRecord();
        medicalRecord.setRecordDate(LocalDate.now());
        medicalRecords = medicalRecordDAO.getAll();
        patients = patientDAO.getAll();
        doctors = doctorDAO.getAll();
    }

    public void create() {
        if (medicalRecordDAO.createMedicalRecord(
                medicalRecord.getPatient().getPatientID(),
                medicalRecord.getDoctor().getDoctorID(),
                medicalRecord.getDiagnosis(),
                medicalRecord.getRecordDate())) {

            medicalRecords = medicalRecordDAO.getAll();
            medicalRecord = new MedicalRecord();
            medicalRecord.setRecordDate(LocalDate.now());
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Medical record created successfully"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to create medical record", null));
        }
    }

    public void update() {
        if (medicalRecordDAO.updateMedicalRecord(
                medicalRecord.getRecordID(),
                medicalRecord.getDiagnosis())) {

            medicalRecords = medicalRecordDAO.getAll();
            medicalRecord = new MedicalRecord();
            medicalRecord.setRecordDate(LocalDate.now());
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Medical record updated successfully"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to update medical record", null));
        }
    }

    public void delete(Long id) {
        if (medicalRecordDAO.deleteMedicalRecord(id)) {
            medicalRecords = medicalRecordDAO.getAll();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Medical record deleted successfully"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to delete medical record", null));
        }
    }

    public void loadMedicalRecord(Long id) {
        medicalRecord = medicalRecordDAO.getMedicalRecordById(id).orElse(new MedicalRecord());
        if (medicalRecord.getRecordID() == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Medical record not found", null));
        }
    }

    // Getters and Setters
    public MedicalRecord getMedicalRecord() { return medicalRecord; }
    public void setMedicalRecord(MedicalRecord medicalRecord) { this.medicalRecord = medicalRecord; }
    public List<MedicalRecord> getMedicalRecords() { return medicalRecords; }
    public List<MedicalRecord> getFilteredRecords() { return filteredRecords; }
    public void setFilteredRecords(List<MedicalRecord> filteredRecords) {
        this.filteredRecords = filteredRecords;
    }
    public List<Patient> getPatients() { return patients; }
    public List<Doctor> getDoctors() { return doctors; }
}