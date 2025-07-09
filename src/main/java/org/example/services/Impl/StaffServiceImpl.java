package org.example.services.Impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.example.dao.StaffDAO;
import org.example.enums.Role;
import org.example.models.entities.Staff;
import org.example.services.Impl.BaseService;
import org.example.services.ServiceException;
import org.example.services.StaffService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class StaffServiceImpl extends BaseService implements StaffService {

    @Inject
    private StaffDAO staffDAO;

    public Optional<Staff> getStaffById(Long id) {
        isIdValid(id);
        return staffDAO.getById(id);
    }

    public List<Staff> getAllStaff() {
        return staffDAO.getAll();
    }

    public List<Staff> getStaffByRole(Role role) {
        if (role == null) {
            throw new ServiceException("Role cannot be null");
        }
        return staffDAO.getStaffByRole(role);
    }

    public boolean saveStaff(Staff staff) {
        validateStaff(staff);
        return staffDAO.saveToDB(staff);
    }

    public void deleteStaffById(Long id) {
        isIdValid(id);
        staffDAO.deleteByID(id);
    }

    @Override
    public void updateStaff(Staff staff) {
        if (staff == null) {
            throw new ServiceException("Staff cannot be null");
        }
        if (staff.getStaffID() == null) {
            throw new ServiceException("Staff ID cannot be null for update");
        }

        // Validate all fields before updating
        validateStaff(staff);

        // Use the efficient bulk update method from DAO
        staffDAO.updateStaff(staff);
    }

    // === FIELD UPDATES ===

    public void updateStaffFirstName(Long id, String firstName) {
        isIdValid(id);
        isNonNullableNameValid(firstName);
        staffDAO.updateFirstNameField(id, firstName);
    }

    public void updateStaffMiddleName(Long id, String middleName) {
        isIdValid(id);
        isNullableNameValid(middleName);
        staffDAO.updateMiddleNameField(id, middleName);
    }

    public void updateStaffLastName(Long id, String lastName) {
        isIdValid(id);
        isNonNullableNameValid(lastName);
        staffDAO.updateLastNameField(id, lastName);
    }

    public void updateStaffEmail(Long id, String email) {
        isIdValid(id);
        isEmailValid(email);
        staffDAO.updateEmailField(id, email);
    }

    public void updateStaffContactNumber(Long id, String contactNumber) {
        isIdValid(id);
        isContactNumberValid(contactNumber);
        staffDAO.updateContactNumberField(id, contactNumber);
    }

    public void updateStaffDateOfBirth(Long id, LocalDate dob) {
        isIdValid(id);
        isDateOfBirthValid(dob);
        staffDAO.updateDateOfBirthField(id, dob);
    }

    // === HELPER VALIDATOR ===
    private void validateStaff(Staff staff) {
        if (staff == null) throw new ServiceException("Staff cannot be null");

        isNonNullableNameValid(staff.getFirstName());
        isNullableNameValid(staff.getMiddleName());
        isNonNullableNameValid(staff.getLastName());
        isContactNumberValid(staff.getContactNumber());
        isEmailValid(staff.getEmail());
        isDateOfBirthValid(staff.getDateOfBirth());

        if (staff.getRole() == null) {
            throw new ServiceException("Staff role cannot be null");
        }
    }
}
