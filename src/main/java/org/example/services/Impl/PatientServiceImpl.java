package org.example.services.Impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.example.dao.PatientDAO;
import org.example.models.entities.Patient;
import org.example.services.PatientService;
import org.example.services.ServiceException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PatientServiceImpl extends BaseService implements PatientService {

    @Inject
    private PatientDAO patientDAO;

    public Optional<Patient> getPatientById(Long id) {
        isIdValid(id);
        return patientDAO.getById(id);
    }

    public List<Patient> searchPatients(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new ServiceException("Search keyword cannot be empty");
        }
        return patientDAO.searchPatients(keyword.trim());
    }

    public List<Patient> getAllPatients() {
        return patientDAO.getAll();
    }

    public boolean savePatient(Patient patient) {
        validatePatient(patient);
        return patientDAO.saveToDB(patient);
    }

    public void deletePatientById(Long id) {
        isIdValid(id);
        patientDAO.deleteByID(id);
    }

    @Override
    @Transactional
    public void updatePatient(Patient patient) {
        if (patient == null) {
            throw new ServiceException("Patient cannot be null");
        }
        if (patient.getPatientID() == null) {
            throw new ServiceException("Patient ID cannot be null for update");
        }

        // Validate all fields before updating
        validatePatient(patient);

        // Use the efficient bulk update method from DAO
        patientDAO.updatePatient(patient);
    }

    // === FIELD UPDATES ===

    public void updatePatientFirstName(Long id, String firstName) {
        isIdValid(id);
        isNonNullableNameValid(firstName);
        patientDAO.updateFirstNameField(id, firstName);
    }

    public void updatePatientMiddleName(Long id, String middleName) {
        isIdValid(id);
        isNullableNameValid(middleName);
        patientDAO.updateMiddleNameField(id, middleName);
    }

    public void updatePatientLastName(Long id, String lastName) {
        isIdValid(id);
        isNonNullableNameValid(lastName);
        patientDAO.updateLastNameField(id, lastName);
    }

    public void updatePatientEmail(Long id, String email) {
        isIdValid(id);
        isEmailValid(email);
        patientDAO.updateEmailField(id, email);
    }

    public void updatePatientContactNumber(Long id, String contactNumber) {
        isIdValid(id);
        isContactNumberValid(contactNumber);
        patientDAO.updateContactNumberField(id, contactNumber);
    }

    public void updatePatientDateOfBirth(Long id, LocalDate dob) {
        isIdValid(id);
        isDateOfBirthValid(dob);
        patientDAO.updateDateOfBirthField(id, dob);
    }

    // === HELPER VALIDATOR ===
    private void validatePatient(Patient patient) {
        if (patient == null) throw new ServiceException("Patient cannot be null");

        isNonNullableNameValid(patient.getFirstName());
        isNullableNameValid(patient.getMiddleName());
        isNonNullableNameValid(patient.getLastName());
        isContactNumberValid(patient.getContactNumber());
        isEmailValid(patient.getEmail());
        isDateOfBirthValid(patient.getDateOfBirth());
    }
}
