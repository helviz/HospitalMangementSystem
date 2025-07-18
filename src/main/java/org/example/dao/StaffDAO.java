package org.example.dao;

import jakarta.enterprise.inject.Model;
import org.example.enums.Role;
import org.example.models.entities.Staff;
import org.example.utilities.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;


import java.time.LocalDate;
@Model
public class StaffDAO extends BaseDAO<Staff> implements Serializable {
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

    public List<Staff> getStaffByRole(Role role) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Staff> query = session.createQuery(
                    "FROM Staff WHERE role = :role AND deleted = false ORDER BY lastName, firstName",
                    Staff.class
            );
            query.setParameter("role", role);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println("Error fetching staff by role: " + e.getMessage());
            return List.of();
        }
    }

    public void updateStaff(Staff updatedStaff) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Staff existingStaff = session.get(Staff.class, updatedStaff.getStaffID());
            if (existingStaff != null) {
                // Update all fields at once
                existingStaff.setFirstName(updatedStaff.getFirstName());
                existingStaff.setMiddleName(updatedStaff.getMiddleName());
                existingStaff.setLastName(updatedStaff.getLastName());
                existingStaff.setEmail(updatedStaff.getEmail());
                existingStaff.setContactNumber(updatedStaff.getContactNumber());
                existingStaff.setDateOfBirth(updatedStaff.getDateOfBirth());
                existingStaff.getUser().setPassword(updatedStaff.getUser().getPassword());

                session.merge(updatedStaff);

                tx.commit();
            } else {
                tx.rollback();
                throw new RuntimeException("Staff not found with ID: " + updatedStaff.getStaffID());
            }
        } catch (Exception e) {
            System.out.println("Error updating patient: " + e.getMessage());
            throw new RuntimeException("Failed to update patient", e);
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
