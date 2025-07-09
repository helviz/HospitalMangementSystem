package org.example.managedbeans;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.example.models.entities.MedicalRecord;
import org.example.services.MedicalRecordService;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Named("medicalRecordTableBean")
@ViewScoped
public class MedicalRecordTableBean implements Serializable {

    private List<MedicalRecord> records;
    private MedicalRecord selectedRecord;

    private Long patientId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String diagnosisKeyword;

    @Inject
    private MedicalRecordService medicalRecordService;

    @PostConstruct
    public void init() {
        records = medicalRecordService.getAllMedicalRecords();
    }

    public void refreshRecords() {
        records = medicalRecordService.getAllMedicalRecords();
    }

    public void searchByPatientAndDate() {
        if (patientId != null && startDate != null && endDate != null) {
            records = medicalRecordService.getMedicalRecordsByPatientAndDateRange(patientId, startDate, endDate);
        }
    }

    public void searchByDiagnosis() {
        if (diagnosisKeyword != null && !diagnosisKeyword.trim().isEmpty()) {
            records = medicalRecordService.searchMedicalRecordsByDiagnosis(diagnosisKeyword.trim());
        }
    }

    public void deleteSelectedRecord() {
        if (selectedRecord != null && selectedRecord.getRecordID() != null) {
            medicalRecordService.deleteMedicalRecord(selectedRecord.getRecordID());
            refreshRecords();
            selectedRecord = null;
        }
    }

    // Getters and setters

    public List<MedicalRecord> getRecords() {
        return records;
    }

    public void setRecords(List<MedicalRecord> records) {
        this.records = records;
    }

    public MedicalRecord getSelectedRecord() {
        return selectedRecord;
    }

    public void setSelectedRecord(MedicalRecord selectedRecord) {
        this.selectedRecord = selectedRecord;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getDiagnosisKeyword() {
        return diagnosisKeyword;
    }

    public void setDiagnosisKeyword(String diagnosisKeyword) {
        this.diagnosisKeyword = diagnosisKeyword;
    }
}
