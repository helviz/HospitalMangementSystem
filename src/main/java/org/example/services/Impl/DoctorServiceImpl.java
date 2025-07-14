package org.example.services.Impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.example.dao.DoctorDAO;
import org.example.enums.Speciality;
import org.example.models.entities.Doctor;
import org.example.services.DoctorService;
import org.example.services.ServiceException;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class DoctorServiceImpl extends BaseService implements DoctorService {

    @Inject
    private DoctorDAO doctorDAO;

    public Optional<Doctor> getDoctorById(Long id) {
        isIdValid(id);
        return doctorDAO.getById(id);
    }

    public List<Doctor> getAllDoctors() {
        return doctorDAO.getAll();
    }

    public List<Doctor> getDoctorsBySpeciality(Speciality speciality) {
        if (speciality == null) {
            throw new ServiceException("Speciality cannot be null");
        }
        return doctorDAO.getDoctorsBySpeciality(speciality);
    }

    public boolean saveDoctor(Doctor doctor) {
        validateDoctor(doctor);
        return doctorDAO.saveToDB(doctor);
    }

    public void deleteDoctorById(Long id) {
        isIdValid(id);
        doctorDAO.deleteByID(id);
    }

    @Override
    public void updateDoctor(Doctor doctor) {
        if (doctor == null) {
            throw new ServiceException("Doctor cannot be null");
        }
        if (doctor.getDoctorID() == null) {
            throw new ServiceException("Doctor ID cannot be null for update");
        }

        // Validate all fields before updating
        validateDoctor(doctor);

        // Use the efficient bulk update method from DAO
        doctorDAO.updateDoctor(doctor);
    }

    public Map<Speciality, List<Doctor>> getDoctorsGroupedBySpeciality() {
        return doctorDAO.getAll().stream()
                .collect(Collectors.groupingBy(Doctor::getSpeciality, LinkedHashMap::new, Collectors.toList()));
    }



    // === FIELD UPDATES ===

    public void updateDoctorFirstName(Long id, String firstName) {
        isIdValid(id);
        isNonNullableNameValid(firstName);
        doctorDAO.updateFirstNameField(id, firstName);
    }

    public void updateDoctorMiddleName(Long id, String middleName) {
        isIdValid(id);
        isNullableNameValid(middleName);
        doctorDAO.updateMiddleNameField(id, middleName);
    }

    public void updateDoctorLastName(Long id, String lastName) {
        isIdValid(id);
        isNonNullableNameValid(lastName);
        doctorDAO.updateLastNameField(id, lastName);
    }

    public void updateDoctorEmail(Long id, String email) {
        isIdValid(id);
        isEmailValid(email);
        doctorDAO.updateEmailField(id, email);
    }

    public void updateDoctorContactNumber(Long id, String contactNumber) {
        isIdValid(id);
        isContactNumberValid(contactNumber);
        doctorDAO.updateContactNumberField(id, contactNumber);
    }

    public void updateDoctorDateOfBirth(Long id, LocalDate dob) {
        isIdValid(id);
        isDateOfBirthValid(dob);
        doctorDAO.updateDateOfBirthField(id, dob);
    }

    // === HELPER VALIDATOR ===
    private void validateDoctor(Doctor doctor) {
        if (doctor == null) throw new ServiceException("Doctor cannot be null");

        isNonNullableNameValid(doctor.getFirstName());
        isNullableNameValid(doctor.getMiddleName());
        isNonNullableNameValid(doctor.getLastName());
        isContactNumberValid(doctor.getContactNumber());
        isEmailValid(doctor.getEmail());
        isDateOfBirthValid(doctor.getDateOfBirth());

        if (doctor.getSpeciality() == null) {
            throw new ServiceException("Doctor speciality cannot be null");
        }
    }
}
