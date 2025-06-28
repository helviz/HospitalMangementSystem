package org.example.dao;

import org.example.models.entities.Staff;
import org.example.utilities.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.function.Consumer;


import java.time.LocalDate;

public class StaffDAO extends BaseDAO<Staff>{
    public StaffDAO(){
        super(Staff.class);
    }

    private void updateStaffField(Long id, Consumer<Staff> updater) {
        try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
            Transaction tx = session.beginTransaction();
            Staff staff = session.get(Staff.class, id);
            if (staff != null) {
                updater.accept(staff); // Apply the field update
                session.update(staff);
                tx.commit();
            }
        }
    }

    @Override
    public void updateFirstNameField(Long id, String name) {
        updateStaffField(id, staff -> staff.setFirstName(name));
    }

    @Override
    public void updateMiddleNameField(Long id, String name) {
        updateStaffField(id, staff -> staff.setMiddleName(name));
    }

    @Override
    public void updateLastNameField(Long id, String name) {
        updateStaffField(id, staff -> staff.setLastName(name));
    }

    @Override
    public void updateEmailField(Long id, String email) {
        updateStaffField(id, staff -> staff.setEmail(email));
    }

    @Override
    public void updateContactNumberField(Long id, String contactNumber) {
        updateStaffField(id, staff -> staff.setContactNumber(contactNumber));
    }

    @Override
    public void updateDateOfBirthField(Long id, LocalDate dateOfBirth) {
        updateStaffField(id, staff -> staff.setDateOfBirth(dateOfBirth));
    }
}
