package org.example.services.Impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.example.dao.MedicalRecordDAO;
import org.example.models.entities.MedicalRecord;
import org.example.services.Impl.BaseService;
import org.example.services.MedicalRecordService;
import org.example.services.ServiceException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class MedicalRecordServiceImpl extends BaseService implements MedicalRecordService {

    @Inject
    private MedicalRecordDAO medicalRecordDAO;

    public Optional<MedicalRecord> getMedicalRecordById(Long recordId) {
        isIdValid(recordId);
        return medicalRecordDAO.getMedicalRecordById(recordId);
    }

    public boolean createMedicalRecord(Long patientId, Long doctorId, String diagnosis, LocalDate recordDate) {
        isIdValid(patientId);
        isIdValid(doctorId);
        if (diagnosis == null || diagnosis.trim().isEmpty()) {
            throw new ServiceException("Diagnosis cannot be empty");
        }
        return medicalRecordDAO.createMedicalRecord(patientId, doctorId, diagnosis, recordDate);
    }

    public List<MedicalRecord> getMedicalRecordsByPatient(Long patientId) {
        isIdValid(patientId);
        return medicalRecordDAO.getMedicalRecordsByPatient(patientId);
    }

    public List<MedicalRecord> getMedicalRecordsByDoctor(Long doctorId) {
        isIdValid(doctorId);
        return medicalRecordDAO.getMedicalRecordsByDoctor(doctorId);
    }

    public boolean updateMedicalRecordDiagnosis(Long recordId, String newDiagnosis) {
        isIdValid(recordId);
        if (newDiagnosis == null || newDiagnosis.trim().isEmpty()) {
            throw new ServiceException("New diagnosis cannot be empty");
        }
        return medicalRecordDAO.updateMedicalRecord(recordId, newDiagnosis);
    }

    public boolean deleteMedicalRecord(Long recordId) {
        isIdValid(recordId);
        return medicalRecordDAO.deleteMedicalRecord(recordId);
    }

    public List<MedicalRecord> getMedicalRecordsByPatientAndDateRange(Long patientId, LocalDate startDate, LocalDate endDate) {
        isIdValid(patientId);
        if (startDate == null || endDate == null) {
            throw new ServiceException("Start and end dates cannot be null");
        }
        if (endDate.isBefore(startDate)) {
            throw new ServiceException("End date cannot be before start date");
        }
        return medicalRecordDAO.getMedicalRecordsByPatientAndDateRange(patientId, startDate, endDate);
    }

    public List<MedicalRecord> searchMedicalRecordsByDiagnosis(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new ServiceException("Search keyword cannot be empty");
        }
        return medicalRecordDAO.searchMedicalRecordsByDiagnosis(keyword.trim());
    }

    public List<MedicalRecord> getAllMedicalRecords() {
        return medicalRecordDAO.getAll();
    }
}
