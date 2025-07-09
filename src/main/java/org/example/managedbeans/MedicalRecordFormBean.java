package org.example.managedbeans;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.example.models.entities.MedicalRecord;
import org.example.services.MedicalRecordService;

import java.io.Serializable;
import java.time.LocalDate;

@Named("medicalRecordFormBean")
@ViewScoped
public class MedicalRecordFormBean implements Serializable {

    private MedicalRecord medicalRecord;

    private Long patientId;
    private Long doctorId;

    @Inject
    private MedicalRecordService medicalRecordService;

    @PostConstruct
    public void init() {
        medicalRecord = new MedicalRecord();
    }

    public void saveMedicalRecord() {
        if (patientId != null && doctorId != null && medicalRecord != null) {
            boolean success = medicalRecordService.createMedicalRecord(
                    patientId,
                    doctorId,
                    medicalRecord.getDiagnosis(),
                    medicalRecord.getRecordDate() != null ? medicalRecord.getRecordDate() : LocalDate.now()
            );
            if (success) {
                medicalRecord = new MedicalRecord(); // reset form
                patientId = null;
                doctorId = null;
            }
        }
    }

    public void updateDiagnosis(Long recordId, String newDiagnosis) {
        medicalRecordService.updateMedicalRecordDiagnosis(recordId, newDiagnosis);
    }

    // Getters and setters

    public MedicalRecord getMedicalRecord() {
        return medicalRecord;
    }

    public void setMedicalRecord(MedicalRecord medicalRecord) {
        this.medicalRecord = medicalRecord;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }
}
